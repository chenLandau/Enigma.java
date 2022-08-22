package DataTransferObject;

import java.util.List;

public class codeConfigurationInputDTO {
    private List<Integer> rotorsNumber;
    private List<Character> rotorsPosition;
    private String reflectorNumber;
    private List<String> plugBoardConnection;
    public codeConfigurationInputDTO(List<Integer> rotorsNumber, List<Character> rotorsPosition, String reflectorNumber, List<String> plugBoardConnection){
        this.rotorsNumber = rotorsNumber;
        this.rotorsPosition = rotorsPosition;
        this.reflectorNumber = reflectorNumber;
        this.plugBoardConnection = plugBoardConnection;
    }

    public List<Integer> getRotorsNumber() { return rotorsNumber; }
    public List<Character> getRotorsPosition() { return rotorsPosition; }
    public String getReflectorNumber() { return reflectorNumber; }
    public List<String> getPlugBoardConnection() { return plugBoardConnection; }

}
