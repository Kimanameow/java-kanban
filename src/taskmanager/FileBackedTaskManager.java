package taskmanager;

import tasks.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {
    protected File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,duration,startTime,epic\n");
            for (Task task : allTasks()) {
                writer.write(task.toString() + "\n");
            }
            for (Epic epic : allEpics()) {
                writer.write(epic.toString() + "\n");
            }
            for (Subtask subtask : allSubtasks()) {
                writer.write(subtask.toString() + "\n");
            }
        } catch (IOException m) {
            throw new ManagerSaveException("Saving error");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String type = parts[1];
                String name = parts[2];
                String status = parts[3];
                String description = parts[4];
                Duration duration = Duration.ofMinutes(Long.parseLong(parts[5]));
                LocalDateTime startTime = LocalDateTime.parse(parts[6], formatter);
                int idOfEpic = 0;
                if (id > manager.nextId) {
                    manager.nextId = id;
                }
                if (type.equals("SUBTASK")) {
                    idOfEpic = Integer.parseInt(parts[7]);
                }
                switch (TypeOfTask.valueOf(type)) {
                    case SUBTASK -> {
                        Subtask subtask = new Subtask(name, description, id, StatusOfTask.valueOf(status), idOfEpic, (int) duration.toMinutes(), startTime);
                        manager.subtasks.put(id, subtask);
                        manager.epics.get(idOfEpic).addIdOfSubtaskToList(id);
                    }
                    case TASK -> {
                        Task task = new Task(name, description, id, StatusOfTask.valueOf(status), startTime, (int) duration.toMinutes());
                        manager.tasks.put(id, task);
                    }
                    case EPIC -> {
                        Epic epic = new Epic(name, description, id, StatusOfTask.valueOf(status), startTime, (int) duration.toMinutes());
                        manager.epics.put(id, epic);
                    }
                }
            }
            return manager;
        } catch (IOException e) {
            throw new ManagerSaveException("Saving error!");
        }
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        findTimeFromSubtasks(epics.get(subtask.getIdOfEpic()));
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
        int epicId = subtasks.get(id).getIdOfEpic();
        super.removeSubtaskPerId(id);
        findTimeFromSubtasks(epics.get(epicId));
        save();
    }

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        findTimeFromSubtasks(epics.get(subtask.getIdOfEpic()));
        save();
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        save();
    }
}

