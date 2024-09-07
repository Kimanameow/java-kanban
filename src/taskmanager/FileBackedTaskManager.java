package taskmanager;

import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public void save() {
        try {
            FileWriter writer = new FileWriter("taskmanager");
            for (Task task : allTasks()) {
                writer.write(task.getId() + "," + "TASK" + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "\n");
            }
            for (Epic epic : allEpics()) {
                writer.write(epic.getId() + "," + "EPIC" + "," + epic.getName() + "," + epic.getStatus() + "," + epic.getDescription() + "\n");
            }
            for (Subtask subtask : allSubtasks()) {
                writer.write(subtask.getId() + "," + "SUBTASK" + ',' + subtask.getName() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getIdOfEpic() + "\n");
            }
        } catch (Exception m) {
            throw new ManagerSaveException();
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        try (BufferedReader br = new BufferedReader(new FileReader("taskmanager"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String type = parts[1];
                String name = parts[2];
                String status = parts[3];
                String description;
                int idOfEpic = 0;
                if (parts.length > 4) {
                    description = parts[4];
                    if (type.equals("SUBTASK")) {
                        idOfEpic = Integer.parseInt(parts[5]);
                    }
                } else {
                    description = "";
                }
                if (type.equals("SUBTASK")) {
                    Subtask subtask = new Subtask(name, description, id, StatusOfTask.valueOf(status), idOfEpic);
                    manager.add(subtask);
                } else if (type.equals("TASK")) {
                    Task task = new Task(name, description, id, StatusOfTask.valueOf(status));
                    manager.add(task);
                } else if (type.equals("EPIC")) {
                    Epic epic = new Epic(name, description, id, StatusOfTask.valueOf(status));
                    manager.add(epic);
                }
            }
            return manager;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        save();
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeEpicPerId(int id) {
        super.removeEpicPerId(id);
        save();
    }

    @Override
    public void removeTaskPerId(int id) {
        super.removeTaskPerId(id);
        save();
    }

    @Override
    public void removeSubtaskPerId(int id) {
        super.removeSubtaskPerId(id);
        save();
    }
}