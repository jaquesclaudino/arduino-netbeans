package atmel;

import java.io.File;

/**
 *
 * @author ejoseph
 */
public class BaseDirectories {

    private final String KEYCORE = "Arduino.h";

    private final String LIBCORE = "EEPROM.h";

    private File folder;

    private String arduinoCoreDir;

    private String pinDir;

    private String avrdudeDir;

    private String avrdudeConf;

    private String arduinoLibDir;

    private String arduinoLibDirSub;

    public BaseDirectories() {
        arduinoCoreDir = new String();
        pinDir = new String();
        avrdudeConf = new String();
        avrdudeConf = new String();
        arduinoLibDir = new String();
        arduinoLibDirSub = new String("");
    }

    public BaseDirectories(String baseDir, String platform) {
        folder = new File(baseDir);
        arduinoCoreDir = new String();
        pinDir = new String();
        findDir(platform);
        findAvrdudeDir();
    }

    private void findAvrdudeDir() {
        try {
            if (OSValidator.isWindows()) {
                avrdudeDir = recursiveFindFile(folder, "avrdude.exe");
                avrdudeDir = avrdudeDir.replace("\\", "/");
                avrdudeConf = avrdudeDir.replace("bin", "etc").replace(".exe", ".conf");
                avrdudeConf = avrdudeConf.replace("\\", "/");
            } else if (OSValidator.isUnix() || OSValidator.isMac()) {
                avrdudeDir = recursiveFindFile(folder, "avrdude");
                avrdudeConf = avrdudeDir.replace("bin", "etc") + ".conf";
            }
        } catch (Exception ex) {
        }
    }

    private void findDir(String platform) {

        try {
            String resp = recursiveFindFile(folder, KEYCORE);
            String base_dir = folder.getAbsolutePath();
            arduinoCoreDir = resp.substring(base_dir.length());
            arduinoCoreDir = arduinoCoreDir.substring(0, arduinoCoreDir.length() - KEYCORE.length() - 1);
            resp = recursiveFindFolder(folder, platform);
            pinDir = resp.substring(base_dir.length());
            if (OSValidator.isWindows()) {
                arduinoCoreDir = arduinoCoreDir.replace("\\", "/");
                pinDir = pinDir.replace("\\", "/");
            }
            resp = recursiveFindFile(folder, LIBCORE);
            File libfile = new File(resp);
            String parent = libfile.getParent();
            if(parent.endsWith("/src")){
                arduinoLibDirSub = "/src";
            }else{
                arduinoLibDirSub = "";
            }
            arduinoLibDir = parent.substring(base_dir.length());
            arduinoLibDir = arduinoLibDir.substring(0,arduinoLibDir.indexOf("/EEPROM"));

        } catch (Exception ex) {
        }
    }

    private String recursiveFindFolder(File folder, String name) {
        if (folder.isDirectory()) {
            if (folder.getName().equals(name)) {
                return folder.getAbsolutePath();
            }
            for (File file : folder.listFiles()) {
                String resp = recursiveFindFolder(file, name);
                if (resp != null) {
                    return resp;
                }
            }
        }
        return null;
    }

    private String recursiveFindFile(File folder, String name) {
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                String resp = recursiveFindFile(file, name);
                if (resp != null) {
                    return resp;
                }
            }
        } else if (folder.isFile() && folder.getName().equals(name)) {
            return folder.getAbsolutePath();
        }
        return null;
    }

    public String getArduinoCoreDir() {
        return arduinoCoreDir;
    }

    public String getPinDir() {
        return pinDir;
    }

    public String getAvrdudeDir() {
        return avrdudeDir;
    }

    public String getAvrdudeConf() {
        return avrdudeConf;
    }

    public String getArduinoLibDir() {
        return arduinoLibDir;
    }

    public void setArduinoLibDir(String arduinoLibDir) {
        this.arduinoLibDir = arduinoLibDir;
    }

    public String getArduinoLibDirSub() {
        return arduinoLibDirSub;
    }

    public void setArduinoLibDirSub(String arduinoLibDirSub) {
        this.arduinoLibDirSub = arduinoLibDirSub;
    }

}
