package tasks;

import java.util.ArrayList;


public class Epic extends Task {

    private ArrayList<Integer> listOfSubtasks = new ArrayList<>();
    private static final TypeOfTask type = TypeOfTask.EPIC;

    public Epic(String name, String description, int id, StatusOfTask status) {
        super(name, description, id, status);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, int id) {
        super(name, id);
    }

    public ArrayList<Integer> getListOfSubtasks() {
        return listOfSubtasks;
    }

    public TypeOfTask getType() {
        return TypeOfTask.SUBTASK;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}