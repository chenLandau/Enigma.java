package Engine;

import DataTransferObject.*;
import MyExceptions.MachineLogicException;
import MyExceptions.XmlException;
import MyMachine.Machine;
import MyMachine.Reflector;
import ValidityChecker.ValidityChecker;
import jaxbGenerated.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Engine implements EngineOperations {
    private final static String JAXB_XML_CTE_ENIGMA_PACKAGE_NAME= "jaxbGenerated";
    private Machine enigma;
    private codeConfigurationInputDTO codeConfigurationInput;
    private codeConfigurationOutputDTO currentCodeConfiguration;
    private List<codeConfigurationOutputDTO> machineHistory;
    public Engine() {
        this.enigma = new Machine();
        machineHistory = new ArrayList<>();
    }
    public codeConfigurationOutputDTO getCurrentCodeConfiguration() { return currentCodeConfiguration; }
    public List<codeConfigurationOutputDTO> getMachineHistory() { return machineHistory; }
    @Override
    public CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_CTE_ENIGMA_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }
    @Override
    public void readCteMachineFromXml(String FilePath)  throws JAXBException, FileNotFoundException, XmlException, MachineLogicException {
        CTEEnigma cteEnigma;

        if(!FilePath.endsWith(".xml"))
            throw new XmlException("The file is not an xml file.");

        InputStream inputStream = new FileInputStream(FilePath);
        cteEnigma = deserializeFrom(inputStream);
        ValidityChecker.checkCteEnigmaValidity(cteEnigma);
        enigma.chargeCteMachineToMyMachine(cteEnigma);
        this.machineHistory.clear();
    }

    @Override
    public void checkRotorsValidation(List<Integer> rotorsNumber) throws MachineLogicException {
        ValidityChecker.checkRotorsCountValidity(enigma.getRotorsCount(),rotorsNumber.size());
        ValidityChecker.checkRotorsNumberValidity(enigma.getTotalRotors().keySet(), rotorsNumber);
        ValidityChecker.checkRotorsNumberAppearance(rotorsNumber);
    }
    @Override
    public void checkRotorsPositionValidity(List<Character> positionList, int requestedPositionAmount) throws MachineLogicException {
        ValidityChecker.checkPositionValidityInAlphabet(enigma.getABC(), positionList);
        ValidityChecker.checkPositionAmountValidity(positionList.size(),requestedPositionAmount);
    }
    @Override
    public void checkPlugConnectionsValidity(List<String> plugConnections) throws MachineLogicException {
        ValidityChecker.checkConnectionsValidity(enigma.getABC(), plugConnections);
    }
    @Override
    public void initializeManualCodeConfiguration(codeConfigurationInputDTO codeConfiguration) throws MachineLogicException {
        enigma.setRotorsInUse(codeConfiguration.getRotorsNumber());
        enigma.setRotorsPositions(codeConfiguration.getRotorsPosition());
        enigma.setReflector(codeConfiguration.getReflectorNumber());
        enigma.setPlugs(codeConfiguration.getPlugBoardConnection());
        this.codeConfigurationInput = codeConfiguration;
        this.machineHistory.add(0,createCurrentCodeConfigurationOutput());
    }
    @Override
    public void InitializeAutomaticallyCodeConfiguration() throws MachineLogicException {
        initializeManualCodeConfiguration(new codeConfigurationInputDTO(lotteryRotorsNumber(),lotteryRotorsPosition(),lotteryReflectorNumber(),lotteryPlugsConnections()));
    }
    @Override
    public RotorsCountDTO createRotorsCountDTO() {
             return new RotorsCountDTO(this.enigma.getRotorsCount());
    }
    @Override
    public ReflectorsDTO createReflectorsDTO(){
        Map<Integer,String> reflectorsData = new HashMap<>();
        for(Map.Entry<Reflector.RomanNumber, Reflector> pair: enigma.getTotalReflectors().entrySet()) {
            reflectorsData.put(pair.getKey().ordinal() + 1, pair.getKey().toString());
        }
        return new ReflectorsDTO(enigma.getTotalReflectors().size(),reflectorsData);
    }
    @Override
    public machineSpecificationsDTO displayingMachineSpecifications(){
        return new machineSpecificationsDTO(enigma.getRotorsCount(),enigma.getRotorsInUse().size(),enigma.getTotalReflectors().size(),enigma.getDecryptedMessagesAmount());
    }
    @Override
    public codeConfigurationOutputDTO createCurrentCodeConfigurationOutput() throws MachineLogicException {
        List<Integer> notchPosition = new ArrayList<>();
        List<Character> rotorsPosition = new ArrayList<>();
        int currentNotchPosition;

        char currentRotorPosition;
        int toBegin = 0;

        for (int i = 0; i < enigma.getRotorsInUse().size(); i++) {
            currentNotchPosition = enigma.getRotorsInUse().get(i).getCurrentNotchRightPosition();
            currentRotorPosition = enigma.getRotorsInUse().get(i).getRotorPositions().get(0).right;
            notchPosition.add(toBegin,currentNotchPosition);
            rotorsPosition.add(toBegin,currentRotorPosition);
        }
        return new codeConfigurationOutputDTO(codeConfigurationInput.getRotorsNumber(),rotorsPosition,notchPosition,codeConfigurationInput.getReflectorNumber(),codeConfigurationInput.getPlugBoardConnection());
    }
    @Override
    public String stringInputProcessor(String input) throws MachineLogicException {
        StringBuilder decodedString = new StringBuilder();
        ValidityChecker.checkStringValidity(enigma.getABC(),input);

        long start = System.nanoTime();
        for(int i = 0; i < input.length() ;i++) {
            decodedString.append(enigma.charInputProcessor(input.charAt(i)));
        }
        long end = System.nanoTime();
        enigma.setDecryptedMessagesAmount(enigma.getDecryptedMessagesAmount() + 1);
        machineHistory.get(0).getStringsDecoding().add(new StringDecodeDataDTO(input,decodedString.toString(),end-start));
        this.currentCodeConfiguration = createCurrentCodeConfigurationOutput();
        return decodedString.toString();
    }
    @Override
    public void resetMachine() {
        enigma.resetMachine();
    }
    public List<Integer> lotteryRotorsNumber(){
        List<Integer> rotorsNumber = new ArrayList<>();
        int range = enigma.getRotorsCount();
        int i = 0;

        while(i < enigma.getRotorsCount()){
            int rand = (int)(Math.random() * range) + 1;
            if(!rotorsNumber.contains(rand)) {
                rotorsNumber.add(rand);
                i++;
            }
        }
        return rotorsNumber;
    }
    public List<Character> lotteryRotorsPosition(){
        List<Character> rotorsPosition = new ArrayList<>();
        int range = enigma.getABC().size();
        char position;
        int i = 0;

        while(i < enigma.getRotorsCount()) {
            int rand = (int)(Math.random() * range);
            position = enigma.getABC().get(rand);
            if(!rotorsPosition.contains(position)) {
                rotorsPosition.add(position);
                i++;
            }
        }
        return rotorsPosition;
    }
    public String lotteryReflectorNumber() throws MachineLogicException {
        int range = enigma.getTotalReflectors().size();
        int rand = (int)(Math.random() * range) + 1;

        return Reflector.RomanNumber.fromIntegerToEnumRomanNumber(rand).toString();
    }
    public List<String> lotteryPlugsConnections() {
        List<String> plugBoardConnection = new ArrayList<>();
        Set<Character> plugsChars = new HashSet<>();
        int plugsAmountRange = enigma.getABC().size() / 2 + 1;
        int plugsAmountRand = (int)(Math.random() * plugsAmountRange);
        int range = enigma.getABC().size();
        int i = 0;
        StringBuilder connection = new StringBuilder();;
        char firstCharInPair;
        char secondCharInPair;

        while( i < plugsAmountRand) {
            int firstCharRand = (int)(Math.random() * range);
            int secondCharRand = (int)(Math.random() * range);

            if(firstCharRand == secondCharRand)
                continue;

            firstCharInPair = enigma.getABC().get(firstCharRand);
            secondCharInPair = enigma.getABC().get(secondCharRand);

            if(!plugsChars.contains(firstCharInPair) && !plugsChars.contains(secondCharInPair)){
                plugsChars.add(firstCharInPair);
                plugsChars.add(secondCharInPair);
                connection.append(firstCharInPair).append(secondCharInPair);
                plugBoardConnection.add(connection.toString());
                connection.delete(0,connection.length());
                i++;
            }
        }
        return plugBoardConnection;
    }

}


