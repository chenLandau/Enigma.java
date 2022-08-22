package DataTransferObject;

public class StringDecodeDataDTO {
       private String inputString;
       private String outputString;
       private Long decodingTime;

    public StringDecodeDataDTO(String inputString, String outputString, Long decodingTime) {
            this.inputString = inputString;
            this.outputString = outputString;
            this.decodingTime = decodingTime;
    }

    public String getInputString() { return inputString; }
    public String getOutputString() { return outputString; }
    public Long getDecodingTime() { return decodingTime; }

}
