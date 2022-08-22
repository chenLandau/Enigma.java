package DataTransferObject;

import java.util.List;

public class PlugBoardDTO {
    private List<String> plugBoardConnection;
    private boolean isBack;

    public PlugBoardDTO(List<String> plugBoardConnection, boolean isBack) {
        this.plugBoardConnection = plugBoardConnection;
        this.isBack = isBack;
    }

    public List<String> getPlugBoardConnection() { return plugBoardConnection; }
    public boolean getIsBack() { return isBack; }
}
