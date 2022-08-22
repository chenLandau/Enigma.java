package MyMachine;
import java.util.HashMap;
import java.util.Map;

public class PlugBoard {
    private Map<Character,Character> plugBoard;
    public PlugBoard() {
       this.plugBoard = new HashMap<>();
    }
    public void connectPlug(char first,char second) {
       plugBoard.put(first,second);
       plugBoard.put(second,first);
    }
    public char plugBoardWiringRes(char input) {
        char output = input;
        for(Map.Entry<Character, Character> pair: plugBoard.entrySet()) {
            if(pair.getKey() == input)
                output = pair.getValue();
        }
        return output;
    }
}
