package MyMachine;
import MyExceptions.MachineLogicException;
import java.util.ArrayList;
import java.util.List;

public class Rotor {
        private final int id;
        private int notch;
        private char notchRightPosition;
        private char initialPosition;
        private List<Position> rotor;

        public static class Position {
                public char right;
               public char left;
                public Position(char right, char left) {
                        this.right = Character.toUpperCase(right);
                        this.left = Character.toUpperCase(left);
                }
        }
        public int getId() { return id; }
        public int getNotch() { return notch; }
        public char getNotchRightPosition() {
                return notchRightPosition; }
        public List<Position> getRotorPositions() { return rotor; }
        public void setFirstPosition(char value) {
                this.initialPosition = value;
        }

        public void setNotchRightPosition(char value) {
                this.notchRightPosition = value;
        }
        public int getCurrentNotchRightPosition() throws MachineLogicException {
                for (int i = 0; i < rotor.size(); i++) {
                        if (rotor.get(i).right == notchRightPosition) {
                                return i;
                        }
                }
                throw new MachineLogicException("unknown notch position in rotor number: " + this.id);
        }
        public enum Direction {
                FORWARD, BACKWARD;
        }
        public Rotor(int id, int notch) {
                this.notch = notch;
                this.id = id;
                this.rotor = new ArrayList<>();
        }
        public void setRotorFirstPosition() {
                int StartOfList = 0;
                while (rotor.get(StartOfList).right != initialPosition) {
                        rotor.add(rotor.remove(StartOfList));
                }
        }
        public int RotorOutput(int rotorInput, Direction direction) throws MachineLogicException {
                if (direction == Direction.FORWARD) {
                        for (int i = 0; i < rotor.size(); i++) {
                                if (rotor.get(rotorInput).right == rotor.get(i).left)
                                        return i;
                        }
                }
                else {
                        for (int i = 0; i < rotor.size(); i++) {
                                if (rotor.get(i).right == rotor.get(rotorInput).left)
                                        return i;
                        }
                }
                throw new MachineLogicException("unknown rotor input in rotor number: " + this.id);
        }
}



