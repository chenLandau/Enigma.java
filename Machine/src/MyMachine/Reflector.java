package MyMachine;
import MyExceptions.MachineLogicException;
import java.util.HashSet;
import java.util.Set;

public class Reflector {
    private final RomanNumber id;
    public enum RomanNumber {
        I, II, III, IV, V;
        public static RomanNumber fromIntegerToEnumRomanNumber(int id) throws MachineLogicException {
            switch (id) {
                case 1:
                    return RomanNumber.I;
                case 2:
                    return RomanNumber.II;
                case 3:
                    return RomanNumber.III;
                case 4:
                    return RomanNumber.IV;
                case 5:
                    return RomanNumber.V;
            }
            throw new MachineLogicException("Reflector number is not recognized in the system.");
        }

        public static RomanNumber fromStrToEnumRomanNumber(String id) throws MachineLogicException {
            switch (id) {
                case "I":
                    return RomanNumber.I;
                case "II":
                    return RomanNumber.II;
                case "III":
                    return RomanNumber.III;
                case "IV":
                    return RomanNumber.IV;
                case "V":
                    return RomanNumber.V;
            }
            throw new MachineLogicException("Reflector number is not recognized in the system.");
        }
    }

    public static class reflect{
        int input;
        int output;
        public reflect(int input, int output) {
            this.input = input;
            this.output = output;
        }
    }
    public RomanNumber getId() { return this.id; }
    public Set<reflect> reflectors;

    public Reflector(RomanNumber id,int size) {
        this.id = id;
        reflectors = new HashSet<>(size);
    }

    public int reflectorOutput(int reflectorInput) throws MachineLogicException {
        for (reflect reflector:reflectors) {
            if(reflector.input == reflectorInput)
                return reflector.output;
            else if(reflector.output == reflectorInput)
                return reflector.input;
        }
        throw new MachineLogicException("unknown reflector input in reflector number: " + this.id.toString());
    }
}