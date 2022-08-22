package ValidityChecker;

import MyExceptions.MachineLogicException;
import MyExceptions.XmlException;
import MyMachine.Reflector;
import jaxbGenerated.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidityChecker {
    public static void checkCteEnigmaValidity(CTEEnigma cteEnigma) throws XmlException, MachineLogicException {
        String abc = cteEnigma.getCTEMachine().getABC().trim();
        int abcSize = abc.length();
        int rotorCount = cteEnigma.getCTEMachine().getRotorsCount();
        int physicalRotorSize = cteEnigma.getCTEMachine().getCTERotors().getCTERotor().size();
        if (abcSize % 2 != 0) {
            throw new XmlException("The alphabet size of the machine is not even!");
        }
        if(rotorCount > physicalRotorSize)
            throw new XmlException("There are not enough rotors in the machine.");
        if(rotorCount < 2)
            throw new XmlException("The machine rotors count must be greater than 2.");

        checkMachineRotorsValidity(abcSize, cteEnigma.getCTEMachine().getCTERotors());
        checkMachineReflectorsValidity(abcSize, cteEnigma.getCTEMachine().getCTEReflectors());
    }

    public static void checkMachineRotorsValidity(Integer abcSize, CTERotors cteRotors) throws XmlException{
        Set<Integer> rotorsId = new HashSet<>();

        for (CTERotor rotor: cteRotors.getCTERotor()) {
            if(rotor.getNotch() > abcSize)
                throw new XmlException("The notch must be set within the rotor size range.");
            if(rotorsId.contains(rotor.getId()))
                throw new XmlException("Each rotor must have a unique Id.");
            else
                rotorsId.add(rotor.getId());
            checkRotorMapping(rotor);
        }

        for (int i = 1; i <= rotorsId.size(); i++) {
            if (!rotorsId.contains(i))
                throw new XmlException("rotors Id are not a running counter starting from 1.");
        }
    }
    public static void checkRotorMapping(CTERotor cteRotor) throws XmlException {
        Set<String> rotorsMapping = new HashSet<>();
        for (CTEPositioning position: cteRotor.getCTEPositioning()) {
            if(rotorsMapping.contains(position.getRight()))
                throw new XmlException("There is a double mapping in rotor number " + cteRotor.getId());
            else
                rotorsMapping.add(position.getRight());
        }
        for (CTEPositioning position: cteRotor.getCTEPositioning()) {
            if(!rotorsMapping.remove(position.getLeft()))
                throw new XmlException("There is a double mapping in rotor number " + cteRotor.getId());
        }
    }
    public static void checkMachineReflectorsValidity(Integer abcSize, CTEReflectors cteReflectors) throws XmlException, MachineLogicException {
        Set<Reflector.RomanNumber> reflectorsId = new HashSet<>();

        for (CTEReflector reflector : cteReflectors.getCTEReflector()) {
            Reflector.RomanNumber romanNumber = Reflector.RomanNumber.fromStrToEnumRomanNumber(reflector.getId());
            if (reflector.getCTEReflect().size() != abcSize / 2)
                throw new XmlException("The amount of mappings in the reflectors should be half the size of the abc machine.");
            if (reflectorsId.contains(romanNumber))
                throw new XmlException("Each rotor must have a unique Id.");
            else
                reflectorsId.add(romanNumber);

            checkReflectorMapping(reflector);
        }
        for (int i = 1; i <= reflectorsId.size(); i++) {
            if (!reflectorsId.contains(Reflector.RomanNumber.fromIntegerToEnumRomanNumber(i))) {
                throw new XmlException("Reflector's Id are not a running counter starting from I.");
            }
        }
    }
        public static void checkReflectorMapping(CTEReflector cteReflector) throws XmlException {
            Set<Integer> reflectorMapping = new HashSet<>();

            for (CTEReflect position: cteReflector.getCTEReflect()) {
                if(position.getInput() == position.getOutput())
                    throw new XmlException("A signal cannot be mapped to itself in the reflector!");
                if(reflectorMapping.contains(position.getInput()) || reflectorMapping.contains(position.getOutput()))
                    throw new XmlException("There is a double mapping in reflector number" + cteReflector.getId());
                else
                    reflectorMapping.add(position.getInput());
            }
    }
    public static void checkRotorsNumberValidity(Set<Integer> totalRotorsNumber, List<Integer> rotorsNumber) throws MachineLogicException {
        for (Integer rotorNumber: rotorsNumber) {
            if (!totalRotorsNumber.contains(rotorNumber))
                throw new MachineLogicException("Rotor number " + rotorNumber + " is Unknown.");
        }
    }
    public static void checkRotorsCountValidity(int rotorsCount,int rotorsInUseCount) throws MachineLogicException {
        if(rotorsInUseCount !=rotorsCount)
            throw new MachineLogicException("Mismatch between the number of rotors the machine requires and the number of rotors in use! please enter " + rotorsCount + " rotors.");
        if(rotorsInUseCount > 99)
            throw new MachineLogicException("There are too many rotors in the machine! Maximum  99 rotors can be selected.");
    }

    public static void checkRotorsNumberAppearance(List<Integer> rotorsNumber) throws MachineLogicException {
        Set<Integer> isExist = new HashSet<>();
        for(Integer rotorNumber: rotorsNumber)
        {
            if(isExist.contains(rotorNumber))
                throw new MachineLogicException("Each Rotor Can appears only once!");
            else
                isExist.add(rotorNumber);
        }
    }
    public static void checkPositionValidityInAlphabet(List<Character> Alphabet, List<Character> positionList) throws MachineLogicException {
        for (Character position : positionList) {
            if (!Alphabet.contains(position)) {
                throw new MachineLogicException("Position '" + position + "' doesn't belong to the machine's alphabet.");
            }
        }
    }
    public static void checkPositionAmountValidity(int positionAmount, int requestedPositionAmount) throws MachineLogicException {
        if(positionAmount !=requestedPositionAmount)
            throw new MachineLogicException("Mismatch between the number of position the machine requires and the number of position entered! please enter "
                    + requestedPositionAmount + " positions.");
    }
    public static void checkConnectionsValidity(List<Character> Alphabet, List<String> plugConnections) throws MachineLogicException {
       Set<Character> connectionsAlphabet = new HashSet<>();
       char firstCharPair,secondCharPair;

        for (String connection: plugConnections) {
            firstCharPair = connection.charAt(0);
            secondCharPair = connection.charAt(1);

            if(!Alphabet.contains(firstCharPair) || !Alphabet.contains(secondCharPair))
                throw new MachineLogicException("The plug: '" + connection + "' is invalid! One or more of the letters do not belong to the machine's alphabet.");
            else if(connectionsAlphabet.contains(firstCharPair) || connectionsAlphabet.contains(secondCharPair))
                throw new MachineLogicException("The plug: '" + connection + "' is invalid! it is not possible to use a letter in more than one plug.");
            else {
                connectionsAlphabet.add(firstCharPair);
                connectionsAlphabet.add(secondCharPair);
            }
        }
    }
    public static void checkStringValidity(List<Character> ABC, String input) throws MachineLogicException {
        StringBuilder charsNotValid = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if(!ABC.contains(input.charAt(i))) {
                charsNotValid.append(input.charAt(i)).append(" ");
            }
        }
        if (charsNotValid.toString().length() != 0) {
            throw new MachineLogicException("Invalid decode string! The following characters do not belong to the alphabet of the machine: " + charsNotValid );
        }
    }
}


