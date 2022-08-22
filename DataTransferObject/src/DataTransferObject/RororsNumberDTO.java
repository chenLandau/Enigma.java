package DataTransferObject;

import java.util.List;

public class RororsNumberDTO {
    private List<Integer> rotorsNumber;
    private boolean isBack;

    public RororsNumberDTO(List<Integer> rotorsNumber, boolean isBack) {
        this.rotorsNumber = rotorsNumber;
        this.isBack = isBack;
    }

    public List<Integer> getRotorsNumber() { return rotorsNumber; }
    public boolean getIsBack() { return isBack; }
}
