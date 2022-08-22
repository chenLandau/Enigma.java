package UserInterface;
import DataTransferObject.*;
import Engine.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final static Integer InvalidInteger = -1 ;
    private static EngineOperations engine = new Engine();

    public static void main(String[] args)  {
        printMenu();
        int userInput;
        boolean Exit = false;
        boolean isExistCodeConfiguration = false;
        boolean isExistNewCodeConfiguration = false;

        while (!Exit) {
            userInput = getUserInput(1,8);

            switch (userInput) {
                case 1: {
                    firstCommandExecution();
                    isExistCodeConfiguration = false;
                    isExistNewCodeConfiguration = false;
                    break;
                }
                case 2: {
                    secondCommandExecution(isExistCodeConfiguration,isExistNewCodeConfiguration);
                    break;
                }
                case 3: {
                    isExistCodeConfiguration = thirdCommandExecution();
                    break;
                }
                case 4: {
                    fourthCommandExecution();
                    isExistCodeConfiguration = true;
                    break;
                }
                case 5: {
                    isExistNewCodeConfiguration = fifthCommandExecution(isExistCodeConfiguration);
                    break;
                }
                case 6: {
                    sixthCommandExecution();
                    break;
                }
                case 7:{
                    seventhCommandExecution();
                    break;
                }
                case 8: {
                    Exit = true;
                    System.out.print("Bye Bye!");
                    break;
                }
            }
        }
    }
    public static void printMenu() {
        System.out.println("Please choose one of the following options:");
        System.out.println("1. Reading the machine details file");
        System.out.println("2. Displaying the machine specifications");
        System.out.println("3. Selecting an initial code configuration (manually)");
        System.out.println("4. Selection of initial code configuration (automatically)");
        System.out.println("5. Processing input");
        System.out.println("6. Performing a current code reset");
        System.out.println("7. History and statistics");
        System.out.println("8. Exit");
    }
    public static void printSubmenu(){
        System.out.println("Please choose one of the following options:");
        System.out.println("1. Return to the main menu.");
        System.out.println("2. Continue current command.");
    }

    public static void firstCommandExecution() {
        Scanner scanner = new Scanner(System.in);
        boolean isValid = false;

        while (!isValid) {
            try {
                System.out.println("Please enter a full path to the XML file you want to load: ");
                String filePath = scanner.nextLine();
                engine.readCteMachineFromXml(filePath);
                isValid = true;
            }
            catch (Exception e) {
                System.out.println(e.getClass().getSimpleName() + ": The file is not valid! " + e.getMessage());
            }
        }
        System.out.print("File loaded successfully! You are back to the main menu! Please enter your choice: ");
    }

    public static void secondCommandExecution(boolean isExistCodeConfiguration,boolean isExistNewCodeConfiguration) {
        PrintMachineSpecifications(engine.displayingMachineSpecifications());
        if(isExistCodeConfiguration) {
            System.out.print("Original code configuration: ");
            PrintCurrentCodeConfiguration(engine.getMachineHistory().get(0));
        }
        if(isExistNewCodeConfiguration){
            System.out.print("Current code configuration: ");
            PrintCurrentCodeConfiguration(engine.getCurrentCodeConfiguration());
        }
        System.out.print("You are back to the main menu! Please enter your choice: ");
    }

    public static boolean thirdCommandExecution(){
        try {
            RororsNumberDTO RotorsNumber = getRotorsNumber(engine.createRotorsCountDTO());
            if(RotorsNumber.getIsBack()) {
                System.out.print("You are back to the main menu! Please enter your choice: ");
                return false;
            }
            PrimaryRotorsPositionDTO primaryRotorsPosition = getPrimaryRotorsPosition(RotorsNumber.getRotorsNumber().size());
            if(primaryRotorsPosition.getIsBack()){
                System.out.print("You are back to the main menu! Please enter your choice: ");
                return false;
            }
            ReflectorNumberDTO reflectorNumber = getReflectorNumber(engine.createReflectorsDTO());
            if(reflectorNumber.getIsBack()){
                System.out.print("You are back to the main menu! Please enter your choice: ");
                return false;
            }
            PlugBoardDTO plugBoardConnection = getPlugBoardConnections();
            if(plugBoardConnection.getIsBack()){
                System.out.print("You are back to the main menu! Please enter your choice: ");
                return false;
            }
            engine.initializeManualCodeConfiguration(new codeConfigurationInputDTO(RotorsNumber.getRotorsNumber(), primaryRotorsPosition.getPrimaryRotorsPosition(), reflectorNumber.getReflectorNumber(), plugBoardConnection.getPlugBoardConnection()));
            System.out.print("Code configuration is valid. You are back to the main menu! Please enter your choice: ");
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        return true;
    }

    public static void fourthCommandExecution() {
        try {
            engine.InitializeAutomaticallyCodeConfiguration();
        } catch (Exception e) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
        PrintCurrentCodeConfiguration(engine.getMachineHistory().get(0));
        System.out.print("Initial code configuration done! You are back to the main menu! Please enter your choice: ");
    }

    public static boolean fifthCommandExecution(boolean isExistCodeConfiguration) {
        Scanner scanner = new Scanner(System.in);
        boolean isExistNewCodeConfiguration = false;
        if(!isExistCodeConfiguration) {
            System.out.println("Initial code configuration must be defined first (command 3 or 4) "); //change
        }
        else {
            System.out.print("Please enter an input to process: ");
            String userInputToProcess = scanner.nextLine();
            try {
                System.out.println(engine.stringInputProcessor(userInputToProcess.toUpperCase()));
                isExistNewCodeConfiguration = true;
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
        System.out.print("You are back to the main menu! Please enter your choice: ");
        return isExistNewCodeConfiguration;
    }
    public static void sixthCommandExecution() {
        engine.resetMachine();
        System.out.print("Current code configuration has been reset successfully! You are back to the main menu! Please enter your choice: ");
    }
    public static void seventhCommandExecution(){
        PrintHistoryAndStatistics(engine.getMachineHistory());
        System.out.print("You are back to the main menu! Please enter your choice: ");
    }
    public static int getUserInput(int minOrderNumber,int maxOrderNumber) {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        int userInputInteger = InvalidInteger;
        boolean validInput = false;
        do {
            try {
                userInput = scanner.nextLine();
                userInputInteger = Integer.parseInt(userInput);
                if (minOrderNumber <= userInputInteger && userInputInteger <= maxOrderNumber) {
                    validInput = true;
                } else {
                    throw new InputMismatchException();
                }
            } catch (Exception e) {
                System.out.print("Invalid Input! please enter your choice: ");
            }
        } while (!validInput);

        return userInputInteger;
    }
    public static RororsNumberDTO getRotorsNumber(RotorsCountDTO rotorsCount) {
        String rotorsNumberStr;
        String[] rotorsNumberArr;
        Scanner scanner = new Scanner(System.in);
        List<Integer> rotorsNumberList = new ArrayList<>();
        int userInput;
        boolean isValid = false;
        do {
            System.out.print("Please enter " + rotorsCount.getRotorsCount() + " Rotor's number in the desired order: ");
            rotorsNumberStr = scanner.nextLine();
            rotorsNumberArr = rotorsNumberStr.split(",");
            try {
                for (String str : rotorsNumberArr) {
                    rotorsNumberList.add(Integer.parseInt(str));
                }
            } catch (Exception NumberFormatException) {
                System.out.println("Please enter only numbers!");
                printSubmenu();
                userInput = getUserInput(1,2);
                if(userInput == 1) {
                    rotorsNumberList.clear();
                    return new RororsNumberDTO(null, true);
                }
                continue;
            }

            try {
                engine.checkRotorsValidation(rotorsNumberList);
                System.out.println("Input is valid.");
                isValid = true;
            } catch (Exception e) {
                rotorsNumberList.clear();
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                printSubmenu();
                userInput = getUserInput(1,2);
                if(userInput == 1) {
                    return new RororsNumberDTO(null, true);
                }
            }
        } while (!isValid);

        return new RororsNumberDTO(rotorsNumberList,false);
    }
    public static PrimaryRotorsPositionDTO getPrimaryRotorsPosition(int requestedPositionAmount) {
        String rotorsPositionsStr;
        Scanner scanner = new Scanner(System.in);
        List<Character> rotorsPositionList = new ArrayList<>();
        int userInput;
        boolean isValid = false;
        do {
            System.out.print("Please enter the primary rotor's position, according to the order of their insertion: ");
            rotorsPositionsStr = scanner.nextLine();
            for(int i = 0; i < rotorsPositionsStr.length(); i++)
                rotorsPositionList.add(Character.toUpperCase(rotorsPositionsStr.charAt(i)));

            try {
                engine.checkRotorsPositionValidity(rotorsPositionList, requestedPositionAmount);
                System.out.println("Input is valid.");
                isValid = true;
            } catch (Exception e) {
                rotorsPositionList.clear();
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                printSubmenu();
                userInput=getUserInput(1,2);
                if(userInput == 1) {
                    return new PrimaryRotorsPositionDTO(null, true);
                }
            }
        } while (!isValid);

        return new PrimaryRotorsPositionDTO(rotorsPositionList, false);
    }
    public static ReflectorNumberDTO getReflectorNumber(ReflectorsDTO reflectorsData) {
        Scanner scanner = new Scanner(System.in);
        boolean isValid = false;
        int userInput;
        do {
            System.out.println("Please enter Reflector number: ");
            for (int i = 1; i <= reflectorsData.getReflectorAmount(); i ++) {
                System.out.println(i + ". " + reflectorsData.getNumeralNumbers().get(i));
            }
            userInput = scanner.nextInt();
            if (userInput < 1 || userInput > reflectorsData.getReflectorAmount()) {
                System.out.println("Invalid input! There is an unrecognized reflector id.");
                printSubmenu();
                userInput = getUserInput(1,2);
                if(userInput == 1)
                    return new ReflectorNumberDTO(null, true);
            } else {
                System.out.println("Input is valid.");
                isValid = true;
            }
        } while (!isValid);
        return new ReflectorNumberDTO(reflectorsData.getNumeralNumbers().get(userInput), false);
    }
    public static PlugBoardDTO getPlugBoardConnections() {
        String plugConnectionsStr;
        Scanner stringScanner = new Scanner(System.in);
        String pair;
        List<String> plugConnectionsList = new ArrayList<>();
        boolean isValid = false;
        int userInput;

        do {
            System.out.print("Please insert the pairs of plugs you want to connect: ");
            plugConnectionsStr = stringScanner.nextLine();

            if (plugConnectionsStr.length() % 2 != 0) {
                System.out.println("plug pairs length is not even!");
                printSubmenu();
                userInput = getUserInput(1,2);
                if(userInput == 1)
                    return new PlugBoardDTO(null, true);
                else continue;
            } else if (plugConnectionsStr.length() == 0) {
                return new PlugBoardDTO(null, false);
            }

            for (int i = 0; i < plugConnectionsStr.length() ; i+=2) {
                pair = plugConnectionsStr.substring(i, i + 2);
                plugConnectionsList.add(pair.toUpperCase());
            }
            try {
                engine.checkPlugConnectionsValidity(plugConnectionsList);
                System.out.println("Plugs Connections are valid.");
                isValid = true;
            } catch (Exception e) {
                plugConnectionsList.clear();
                System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                printSubmenu();
                userInput = getUserInput(1,2);
                if(userInput == 1) {
                    return new PlugBoardDTO(null, true);
                }
            }
        } while (!isValid);

        return new PlugBoardDTO(plugConnectionsList, false);
    }
   public static String BuildRotorsNumberString(List<Integer> rotorsNumber, List<Integer> notchPosition) {
        StringBuilder rotorsNumberStr;
       rotorsNumberStr = new StringBuilder("<");
       for(int i =0; i < rotorsNumber.size(); i++){
           rotorsNumberStr.append(rotorsNumber.get(i).toString()).append("(").append(notchPosition.get(i).toString()).append(")");
           if(i == rotorsNumber.size() - 1)
               rotorsNumberStr.append(">");
           else
               rotorsNumberStr.append(",");
       }
       return rotorsNumberStr.toString();
   }
    public static String BuildRotorsPositionString(List<Character> rotorsPosition) {
        StringBuilder rotorsPositionStr;
        rotorsPositionStr = new StringBuilder("<");
        for(int i =0; i < rotorsPosition.size(); i++) {
            rotorsPositionStr.append(rotorsPosition.get(i).toString());
        }
        rotorsPositionStr.append(">");
        return rotorsPositionStr.toString();
    }
    public static String BuildPlugBoardString(List<String> plugBoardPosition) {
        StringBuilder plugsConnection;
        char firstCharPair, secondCharPair;
        plugsConnection = new StringBuilder("<");
        for(int i=0; i< plugBoardPosition.size(); i++) {
            firstCharPair = plugBoardPosition.get(i).charAt(0);
            secondCharPair = plugBoardPosition.get(i).charAt(1);
            if(i == plugBoardPosition.size() - 1)
                plugsConnection.append(firstCharPair).append("|").append(secondCharPair).append(">");
            else
                plugsConnection.append(firstCharPair).append("|").append(secondCharPair).append(",");
        }
        return plugsConnection.toString();
    }
    public static void PrintMachineSpecifications(machineSpecificationsDTO machineSpecifications ) {
        System.out.println("Machine specifications: ");
        System.out.println("amount of rotors in use/total amount of rotors: " + machineSpecifications.getRotorsInUseAmount() + "/" + machineSpecifications.getRotorsAmount());
        System.out.println("Reflectors amount: " + machineSpecifications.getReflectorAmount());
        System.out.println("Decrypted messages amount: " + machineSpecifications.getDecryptedMessagesAmount());
    }
    public static void PrintCurrentCodeConfiguration(codeConfigurationOutputDTO currentCodeConfiguration) {
        StringBuilder CodeConfiguration = new StringBuilder();
        String RotorsNumber = BuildRotorsNumberString(currentCodeConfiguration.getRotorsNumber(),currentCodeConfiguration.getNotchPosition());
        String RotorsFirstPosition = BuildRotorsPositionString(currentCodeConfiguration.getRotorsPosition());
        CodeConfiguration.append(RotorsNumber).append(RotorsFirstPosition);
        CodeConfiguration.append("<").append(currentCodeConfiguration.getReflectorNumber()).append(">");

        if(currentCodeConfiguration.getPlugBoardConnection() != null){
            String plugBoardConnection = BuildPlugBoardString(currentCodeConfiguration.getPlugBoardConnection());
            CodeConfiguration.append(plugBoardConnection);
        }
        System.out.println(CodeConfiguration);
    }
    public static void PrintHistoryAndStatistics(List<codeConfigurationOutputDTO> machineHistory) {
        String time;
        if(machineHistory.size() == 0)
            System.out.println("There is no machine history.");

        for(int i = machineHistory.size() - 1 ;i >= 0;i--) {
            System.out.println("Code configuration: ");
            PrintCurrentCodeConfiguration(machineHistory.get(i));
            for (StringDecodeDataDTO str: machineHistory.get(i).getStringsDecoding()) {
                time = String.format("%,d", str.getDecodingTime());
                System.out.println("#. <" + str.getInputString() +"> --> <" + str.getOutputString() + "> (" + time + " nano second)");
            }
            System.out.println();
        }
    }
}

