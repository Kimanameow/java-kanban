package taskmanager;

import tasks.*;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public void save() {
        try {
            FileWriter writer = new FileWriter("taskmanager");
            writer.write("id,type,name,status,description,epic\n");
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

    public FileBackedTaskManager loadFromFile(File file) {
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
                switch (TypeOfTask.valueOf(type)) {
                    case SUBTASK -> {
                        Subtask subtask = new Subtask(name, description, id, StatusOfTask.valueOf(status), idOfEpic);
                        manager.subtasks.put(id, subtask);
                        manager.epics.get(idOfEpic).getListOfSubtasks().add(id);
                    }
                    case TASK -> {
                        Task task = new Task(name, description, id, StatusOfTask.valueOf(status));
                        manager.tasks.put(id, task);
                    }
                    case EPIC -> {
                        Epic epic = new Epic(name, description, id, StatusOfTask.valueOf(status));
                        manager.epics.put(id, epic);
                    }
                }
            }
            return manager;
        } catch (IOException e) {
            throw new ManagerSaveException("Saving error");
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

    @Override
    public void updateTask(int id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        save();
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        super.updateEpic(id, epic);
        save();
    }
}