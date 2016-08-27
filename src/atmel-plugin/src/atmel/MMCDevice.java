package atmel;

/**
 * MMC
 * @author ejoseph
 */
public class MMCDevice {
    
    private String code;
    
    private String name;
    
    private String mmc;

    public MMCDevice(String code, String name, String mmc) {
        this.code = code;
        this.name = name;
        this.mmc = mmc;
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

    public String getMmc() {
        return mmc;
    }

    public void setMmc(String mmc) {
        this.mmc = mmc;
    }

    @Override
    public String toString() {
        return getName();
    }
  
}
