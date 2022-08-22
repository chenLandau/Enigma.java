package DataTransferObject;

import java.util.Map;

public class ReflectorsDTO {
    private int reflectorAmount;
    private Map<Integer,String> numeralNumbersMap;
    public ReflectorsDTO(int reflectorAmount, Map<Integer,String> numeralNumbers) {
        this.reflectorAmount = reflectorAmount;
        this.numeralNumbersMap = numeralNumbers;
    }
    public int getReflectorAmount() { return reflectorAmount; }
    public Map<Integer, String> getNumeralNumbers() { return numeralNumbersMap; }
}
