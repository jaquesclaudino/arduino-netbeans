package atmel;

/**
 * Programmer Device
 * @author ejoseph
 */
public class ProgrammerDevice {
    
    private String code;
    
    private String name;

    public ProgrammerDevice(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
    
    
   
}
