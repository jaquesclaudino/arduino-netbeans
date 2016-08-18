package atmel;

/**
 *
 * @author ejoseph
 */
public class ArduinoDevice {

    private String name;

    private String code;

    private String programmer;

    private long baudrate;

    private String pinFolder;

    public ArduinoDevice(String name, String code, String programmer, long baudrate, String pinFolder) {
        this.name = name;
        this.code = code;
        this.programmer = programmer;
        this.baudrate = baudrate;
        this.pinFolder = pinFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProgrammer() {
        return programmer;
    }

    public void setProgrammer(String programmer) {
        this.programmer = programmer;
    }

    public long getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(long baudrate) {
        this.baudrate = baudrate;
    }

    public String getPinFolder() {
        return pinFolder;
    }

    public void setPinFolder(String pinFolder) {
        this.pinFolder = pinFolder;
    }

    @Override
    public String toString() {
        return getName();
    }
    
}
