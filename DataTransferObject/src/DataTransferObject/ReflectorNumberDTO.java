package DataTransferObject;

public class ReflectorNumberDTO {
    private String ReflectorNumber;
    private boolean isBack;
    public ReflectorNumberDTO(String ReflectorNumber, boolean isBack) {
        this.ReflectorNumber = ReflectorNumber;
        this.isBack = isBack;
    }
    public String getReflectorNumber() { return ReflectorNumber; }
    public boolean getIsBack() { return isBack; }
}
