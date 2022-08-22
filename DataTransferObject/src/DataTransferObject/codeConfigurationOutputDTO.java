package DataTransferObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class codeConfigurationOutputDTO {
    private List<Integer> rotorsNumber;
    private List<Character> rotorsPosition;
    private List<Integer> notchPosition;
    private String reflectorNumber;
    private List<String> plugBoardConnection;
    private Set<StringDecodeDataDTO> stringsDecoding;


    public codeConfigurationOutputDTO(List<Integer> rotorsNumber, List<Character> rotorsPosition, List<Integer> notchPosition, String reflectorNumber, List<String> plugBoardConnection){
        this.rotorsNumber = rotorsNumber;
        this.rotorsPosition = rotorsPosition;
        this.notchPosition = notchPosition;
        this.reflectorNumber = reflectorNumber;
        this.plugBoardConnection = plugBoardConnection;
        this.stringsDecoding = new HashSet<>();
    }

    public List<Integer> getRotorsNumber() { return rotorsNumber; }
    public List<Character> getRotorsPosition() { return rotorsPosition; }
    public List<Integer> getNotchPosition() { return notchPosition; }
    public String getReflectorNumber() { return reflectorNumber; }
    public List<String> getPlugBoardConnection() { return plugBoardConnection; }
    public Set<StringDecodeDataDTO> getStringsDecoding(){ return stringsDecoding; }


}
