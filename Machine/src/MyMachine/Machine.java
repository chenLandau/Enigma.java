package MyMachine;
import MyExceptions.MachineLogicException;
import jaxbGenerated.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Machine {
    private final List<Character> ABC;
    private int rotorsCount;
    private final List<Rotor> rotorsInUse;
    private Reflector reflectorInUse;
    private Map<Integer,Rotor> totalRotors;
    private Map<Reflector.RomanNumber,Reflector> totalReflectors;
    private final PlugBoard plugBoard;
    private int decryptedMessagesAmount;
    public Machine(){
        this.totalRotors = new HashMap<>();
        this.rotorsInUse = new ArrayList<>();
        this.totalReflectors = new HashMap<>();
        this.plugBoard = new PlugBoard();
        this.ABC = new ArrayList<>();
        this.decryptedMessagesAmount = 0;
    }
    public List<Character> getABC() { return ABC; }
    public int getRotorsCount() { return rotorsCount; }
    public List<Rotor> getRotorsInUse() { return rotorsInUse; }
    public Map<Reflector.RomanNumber,Reflector> getTotalReflectors() { return totalReflectors; }
    public Map<Integer,Rotor> getTotalRotors(){ return totalRotors; }
    public int getDecryptedMessagesAmount() { return decryptedMessagesAmount; }

    public void setAlphabet(String value) {
        ABC.clear();
        for (int i = 0; i<value.length(); i++) {
            this.ABC.add(value.charAt(i));
        }
    }
    public void setRotorsMap( Map<Integer,Rotor> value) { this.totalRotors = value; }
    public void setRotorsCount(int value) { this.rotorsCount = value; }
    public void setReflectorsMap(Map<Reflector.RomanNumber,Reflector> value) {
        this.totalReflectors = value;
    }
    public void setRotorsInUse(List<Integer> RotorsNumber){
        this.rotorsInUse.clear();
        Rotor currentRotor;
        int toBegin = 0;
        for (Integer integer : RotorsNumber) {
            currentRotor = totalRotors.get(integer);
            this.rotorsInUse.add(toBegin, currentRotor);
        }
    }
    public void setRotorsPositions(List<Character> firstPositions){
        int j = firstPositions.size();
        for (Rotor rotor : rotorsInUse) {
            rotor.setFirstPosition(firstPositions.get(j - 1));
            rotor.setRotorFirstPosition();
            j--;
        }
    }
    public void setReflector(String reflectorNumber) throws MachineLogicException {
       this.reflectorInUse = totalReflectors.get(Reflector.RomanNumber.fromStrToEnumRomanNumber(reflectorNumber));
    }
    public void setPlugs(List<String> plugsConnections) {
        char firstCharPair,secondCharPair;
        if(plugsConnections != null){
            for (String connection: plugsConnections) {
                firstCharPair = connection.charAt(0);
                secondCharPair = connection.charAt(1);
                this.plugBoard.connectPlug(firstCharPair,secondCharPair);
            }
        }
    }
    public void setDecryptedMessagesAmount(int amount){
        this.decryptedMessagesAmount = amount;
    }
    public void chargeCteMachineToMyMachine(CTEEnigma cteEnigma) throws MachineLogicException {
        setAlphabet(cteEnigma.getCTEMachine().getABC().trim());
        setRotorsCount(cteEnigma.getCTEMachine().getRotorsCount());
        setRotorsMap(chargeRotorsFromCteMachine(cteEnigma.getCTEMachine().getCTERotors()));
        setReflectorsMap(chargeReflectorsFromCteMachine(cteEnigma.getCTEMachine().getCTEReflectors()));
        setDecryptedMessagesAmount(0);
    }
    public Map<Integer,Rotor> chargeRotorsFromCteMachine(CTERotors cteRotors){
        Map<Integer,Rotor> myRotorMap = new HashMap<>();
        Rotor curr;
        for (CTERotor cteRotor: cteRotors.getCTERotor()) {
            curr = new Rotor(cteRotor.getId(),cteRotor.getNotch());
            for (CTEPositioning position: cteRotor.getCTEPositioning()) {
                curr.getRotorPositions().add(new Rotor.Position(position.getRight().trim().charAt(0),position.getLeft().trim().charAt(0)));
            }
            myRotorMap.put(curr.getId(),curr);
            curr.setNotchRightPosition(curr.getRotorPositions().get(curr.getNotch() - 1).right);
        }
        return myRotorMap;
    }
    public Map<Reflector.RomanNumber,Reflector> chargeReflectorsFromCteMachine(CTEReflectors cteReflectors) throws MachineLogicException {
        Map<Reflector.RomanNumber,Reflector> myReflectors = new HashMap<>();
        Reflector curr;
        for(CTEReflector cteReflector: cteReflectors.getCTEReflector()) {
            curr = new Reflector(Reflector.RomanNumber.fromStrToEnumRomanNumber(cteReflector.getId()),cteReflector.getCTEReflect().size()); //check size
            for (CTEReflect cteReflect : cteReflector.getCTEReflect()) {
                curr.reflectors.add(new Reflector.reflect(cteReflect.getInput(), cteReflect.getOutput()));
            }
            myReflectors.put(curr.getId(),curr);
        }
        return myReflectors;
    }
    public void resetMachine(){
        for (Rotor rotor: rotorsInUse) {
            rotor.setRotorFirstPosition();
        }
    }

    public char charInputProcessor(char input) throws MachineLogicException {
        int rotorIndex;
        Rotor currentRotor;
        Rotor nextRotor;
        int output = ABC.indexOf(plugBoard.plugBoardWiringRes(input));

        for(rotorIndex = 0; rotorIndex<  rotorsInUse.size() ; rotorIndex++) {
            currentRotor = rotorsInUse.get(rotorIndex);

            if(rotorIndex == 0) {
                currentRotor.getRotorPositions().add(currentRotor.getRotorPositions().remove(0));
            }
            if((rotorIndex!= rotorsInUse.size() - 1) && currentRotor.getNotchRightPosition() == currentRotor.getRotorPositions().get(0).right) { //might be problem with last rotor notch
                nextRotor = rotorsInUse.get(rotorIndex + 1);
                nextRotor.getRotorPositions().add( nextRotor.getRotorPositions().remove(0));
            }
            output = currentRotor.RotorOutput(output, Rotor.Direction.FORWARD);
        }

        output = reflectorInUse.reflectorOutput(output + 1) - 1;

        for(rotorIndex = rotorsInUse.size() -1 ; rotorIndex >= 0; rotorIndex--) {
            currentRotor = rotorsInUse.get(rotorIndex);
            output = currentRotor.RotorOutput(output,Rotor.Direction.BACKWARD);
        }
        return plugBoard.plugBoardWiringRes(ABC.get(output));
    }
}


