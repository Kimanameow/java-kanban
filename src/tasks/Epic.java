package tasks;

import taskmanager.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Epic extends Task {

    private ArrayList<Integer> listOfSubtasks = new ArrayList<>();
    private static final TypeOfTask type = TypeOfTask.EPIC;
    Duration duration;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public Epic(String name, String description, int id, StatusOfTask status) {
        super(name, description, id, status);
    }

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

    @Override
    public String toString() {
        return super.toString();
    }

    public void findTimeFromSubtasks() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        if (!getListOfSubtasks().isEmpty()) {
            LocalDateTime startTime = LocalDateTime.MAX;
            LocalDateTime endTime = LocalDateTime.MIN;
            for (int id : getListOfSubtasks()) {
                Subtask subtask = manager.allSubtasks().get(id);
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endTime)) {
                    startTime = subtask.getEndTime();
                }
            }
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}