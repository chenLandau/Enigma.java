package Engine;

import DataTransferObject.*;
import MyExceptions.MachineLogicException;
import MyExceptions.XmlException;
import jaxbGenerated.CTEEnigma;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public interface EngineOperations {
    CTEEnigma deserializeFrom(InputStream in) throws JAXBException;
    void readCteMachineFromXml(String FilePath) throws JAXBException, FileNotFoundException, XmlException, MachineLogicException;
    List<codeConfigurationOutputDTO> getMachineHistory();
    codeConfigurationOutputDTO getCurrentCodeConfiguration();
    RotorsCountDTO createRotorsCountDTO();
    ReflectorsDTO createReflectorsDTO();
    void checkRotorsValidation(List<Integer> rotorsNumberList) throws MachineLogicException;
    void checkRotorsPositionValidity(List<Character> rotorsPositionList, int requestedPositionAmount) throws MachineLogicException;
    void checkPlugConnectionsValidity(List<String> plugConnections) throws MachineLogicException;
    void initializeManualCodeConfiguration(codeConfigurationInputDTO codeConfiguration) throws MachineLogicException;
    void InitializeAutomaticallyCodeConfiguration() throws MachineLogicException;
    machineSpecificationsDTO displayingMachineSpecifications();
    codeConfigurationOutputDTO createCurrentCodeConfigurationOutput() throws MachineLogicException;
    String stringInputProcessor(String input) throws MachineLogicException;
    void resetMachine();
}
