package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class Epic extends Task {

    private ArrayList<Integer> listOfSubtasks = new ArrayList<>();
    private static final TypeOfTask type = TypeOfTask.EPIC;
    private LocalDateTime endTime;


    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, int id) {
        super(name, id);
    }

    public Epic(String name, String description, int id, StatusOfTask statusOfTask, LocalDateTime startTime, int minutes) {
        super(name, description, id, statusOfTask, startTime, minutes);
    }

    public ArrayList<Integer> getListOfSubtasks() {
        return listOfSubtasks;
    }

    public TypeOfTask getType() {
        return TypeOfTask.EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void addIdOfSubtaskToList(int id) {
        listOfSubtasks.add(id);
    }
}