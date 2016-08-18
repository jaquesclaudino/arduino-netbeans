package atmel;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

// TODO define position attribute
@TemplateRegistration(folder = "Project/Atmel", displayName = "#Atmel_displayName", description = "AtmelDescription.html", iconBase = "atmel/atmel.png", content = "AtmelProject.zip")
@Messages("Atmel_displayName=Atmel Project")
public class AtmelWizardIterator implements WizardDescriptor./*Progress*/InstantiatingIterator {
    
    private int index;
    private WizardDescriptor.Panel[] panels;
    private WizardDescriptor wiz;
    
    public AtmelWizardIterator() {
    }
    
    public static AtmelWizardIterator createIterator() {
        return new AtmelWizardIterator();
    }
    
    private WizardDescriptor.Panel[] createPanels() {
        return new WizardDescriptor.Panel[]{
            new AtmelWizardPanel(),};
    }
    
    private String[] createSteps() {
        return new String[]{
            NbBundle.getMessage(AtmelWizardIterator.class, "LBL_CreateProjectStep")
        };
    }
    
    public Set/*<FileObject>*/ instantiate(/*ProgressHandle handle*/) throws IOException {
        Set<FileObject> resultSet = new LinkedHashSet<>();
        File dirF = FileUtil.normalizeFile((File) wiz.getProperty("projdir"));
        dirF.mkdirs();
        
        FileObject template = Templates.getTemplate(wiz);
        FileObject dir = FileUtil.toFileObject(dirF);
        unZipFile(template.getInputStream(), dir, wiz);

        // Always open top dir as a project:
        resultSet.add(dir);
        // Look for nested projects to open as well:
        Enumeration<? extends FileObject> e = dir.getFolders(true);
        while (e.hasMoreElements()) {
            FileObject subfolder = e.nextElement();
            if (ProjectManager.getDefault().isProject(subfolder)) {
                resultSet.add(subfolder);
            }
        }
        
        File parent = dirF.getParentFile();
        if (parent != null && parent.exists()) {
            ProjectChooser.setProjectsFolder(parent);
        }
        
        return resultSet;
    }
    
    @Override
    public void initialize(WizardDescriptor wiz) {
        this.wiz = wiz;
        index = 0;
        panels = createPanels();
        // Make sure list of steps is accurate.
        String[] steps = createSteps();
        for (int i = 0; i < panels.length; i++) {
            Component c = panels[i].getComponent();
            if (steps[i] == null) {
                // Default step name to component name of panel.
                // Mainly useful for getting the name of the target
                // chooser to appear in the list of steps.
                steps[i] = c.getName();
            }
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                // Step #.
                // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                // Step name (actually the whole list for reference).
                jc.putClientProperty("WizardPanel_contentData", steps);
            }
        }
    }
    
    @Override
    public void uninitialize(WizardDescriptor wiz) {
        this.wiz.putProperty("projdir", null);
        this.wiz.putProperty("name", null);
        this.wiz = null;
        panels = null;
    }
    
    @Override
    public String name() {
        return MessageFormat.format("{0} of {1}",
                new Object[]{new Integer(index + 1), new Integer(panels.length)});
    }
    
    @Override
    public boolean hasNext() {
        return index < panels.length - 1;
    }
    
    @Override
    public boolean hasPrevious() {
        return index > 0;
    }
    
    @Override
    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }
    
    @Override
    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }
    
    @Override
    public WizardDescriptor.Panel current() {
        return panels[index];
    }

    // If nothing unusual changes in the middle of the wizard, simply:
    @Override
    public final void addChangeListener(ChangeListener l) {
    }
    
    @Override
    public final void removeChangeListener(ChangeListener l) {
    }
    
    private static void unZipFile(InputStream source, FileObject projectRoot, WizardDescriptor wiz) throws IOException {
        try {
            ZipInputStream str = new ZipInputStream(source);
            ZipEntry entry;
            while ((entry = str.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    FileUtil.createFolder(projectRoot, entry.getName());
                } else {
                    FileObject fo = FileUtil.createData(projectRoot, entry.getName());

                    /*try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File("/home/jaques/arduino-netbeans.log"),true))) {
                     pw.println(entry.getName());
                     }*/
                    if ("nbproject/project.xml".equals(entry.getName())) {
                        // Special handling for setting name of Ant-based projects; customize as needed:
                        filterProjectXML(fo, str, projectRoot.getName());
                    } else if ("Makefile".equals(entry.getName())) {
                        filterMakefile(fo, str, wiz);
                    } else if("main.cpp".equals(entry.getName())){
                        filtermain(fo, str, wiz);
                    }else {
                        writeFile(str, fo);
                    }
                }
            }
        } finally {
            source.close();
        }
    }
    
    private static void writeFile(ZipInputStream str, FileObject fo) throws IOException {
        OutputStream out = fo.getOutputStream();
        try {
            FileUtil.copy(str, out);
        } finally {
            out.close();
        }
    }
    
    private static void filterProjectXML(FileObject fo, ZipInputStream str, String name) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(str, baos);
            Document doc = XMLUtil.parse(new InputSource(new ByteArrayInputStream(baos.toByteArray())), false, false, null, null);
            NodeList nl = doc.getDocumentElement().getElementsByTagName("name");
            if (nl != null) {
                for (int i = 0; i < nl.getLength(); i++) {
                    Element el = (Element) nl.item(i);
                    if (el.getParentNode() != null && "data".equals(el.getParentNode().getNodeName())) {
                        NodeList nl2 = el.getChildNodes();
                        if (nl2.getLength() > 0) {
                            nl2.item(0).setNodeValue(name);
                        }
                        break;
                    }
                }
            }
            OutputStream out = fo.getOutputStream();
            try {
                XMLUtil.write(doc, out, "UTF-8");
            } finally {
                out.close();
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            writeFile(str, fo);
        }
        
    }
    
    private static void filtermain(FileObject fo, ZipInputStream str, WizardDescriptor wiz) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(str, baos);
            
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File(fo.getPath()), true))) {
                
                Date date = new Date();
                SimpleDateFormat  format = new SimpleDateFormat("MMMMM dd, yyyy, hh:mm aaa");
                pw.println("/* \n"
                        + " * File:   main.cpp\n"
                        + " * Author: ${user}\n"
                        + " * \n"
                        + " * Created on "+format.format(date) + "\n"
                        + " */");
                pw.println();
                pw.println(baos.toString());
            }
            
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            writeFile(str, fo);
        }
    }
    
    private static void filterMakefile(FileObject fo, ZipInputStream str, WizardDescriptor wiz) throws IOException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileUtil.copy(str, baos);
            
            try (PrintWriter pw = new PrintWriter(new FileOutputStream(new File(fo.getPath()), true))) {
                
                MMCDevice mmc = (MMCDevice) wiz.getProperty("mmc");
                pw.println("MMC_MODEL = " + mmc.getMmc());
                
                pw.println("MMC_MODEL_AVRDURE = " + mmc.getCode());
                
                ProgrammerDevice programmer = (ProgrammerDevice) wiz.getProperty("programmer");
                pw.println("PROGRAMMER = " + programmer.getCode());
                
                String port = (String) wiz.getProperty("port");
                pw.print("PORT = " + port);
                pw.println();
                pw.println(baos.toString());
            }
            
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
            writeFile(str, fo);
        }
        
    }
    
}
