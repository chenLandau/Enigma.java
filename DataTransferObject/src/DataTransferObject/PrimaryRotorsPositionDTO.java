package DataTransferObject;

import java.util.List;

public class PrimaryRotorsPositionDTO {
    List<Character> primaryRotorsPosition;
    boolean isBack;

    public PrimaryRotorsPositionDTO(List<Character> rotorsNumber, boolean isBack) {
        this.primaryRotorsPosition = rotorsNumber;
        this.isBack = isBack;
    }

    public List<Character> getPrimaryRotorsPosition() { return primaryRotorsPosition; }
    public boolean getIsBack() { return isBack; }
}
