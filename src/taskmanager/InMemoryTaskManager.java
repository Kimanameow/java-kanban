package taskmanager;

import history.HistoryManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;

    @Override
    public ArrayList<Task> allTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> allEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> allSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeTasks() {
        tasks.clear();
    }

    @Override
    public void removeEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void removeSubtasks() {
        subtasks.clear();
        for (Epic e : epics.values()) {
            changeStatus(e);
            e.getListOfSubtasks().clear();
        }
    }


    @Override
    public Task getTaskPerId(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicPerId(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskPerId(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void add(Task task) {
        task.setId(nextId);
        tasks.put(nextId, task);
        nextId++;
    }

    @Override
    public void add(Epic epic) {
        epic.setId(nextId);
        epics.put(nextId, epic);
        nextId++;
        changeStatus(epic);
    }

    @Override
    public void add(Subtask subtask) {
        subtask.setId(nextId);
        subtasks.put(nextId, subtask);
        nextId++;

        Epic epic = epics.get(subtask.getIdOfEpic());
        if (epic == null) {
            epic = new Epic("EpicForThisSubtask", subtask.getIdOfEpic());
            add(epic);
        }
        epic.getListOfSubtasks().add(subtask.getId());
        changeStatus(epic);
    }

    @Override
    public ArrayList<String> subtasksForEpic(int id) {
        ArrayList<String> listOfSubtasks = new ArrayList<>();
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            if (!subtasks.isEmpty()) {
                for (Integer subtasksId : epic.getListOfSubtasks()) {
                    Subtask subtask = subtasks.get(subtasksId);
                    listOfSubtasks.add(subtask.getName());
                }
            }
        }
        return listOfSubtasks;
    }

    @Override
    public void removeTaskPerId(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicPerId(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer thisId : epic.getListOfSubtasks()) {
                subtasks.remove(thisId);
            }
        }
    }

    @Override
    public void removeSubtaskPerId(int id) {
        if (!subtasks.containsKey(id)) {
            return;
        }
        int idOfEpic = subtasks.get(id).getIdOfEpic();
        subtasks.remove(id);
        if (epics.containsKey(idOfEpic)) {
            changeStatus(epics.get(idOfEpic));
        }
    }

    @Override
    public void updateTask(int id, Task task) {
        if (tasks.containsKey(id)) {
            tasks.put(id, task);
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        if (subtasks.containsKey(id)) {
            subtasks.put(id, subtask);
            if (epics.containsKey(subtask.getIdOfEpic())) {
                changeStatus(epics.get(subtask.getIdOfEpic()));
            }
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        if (epics.containsKey(id)) {
            epics.put(id, epic);
        }
    }

    @Override
    public void changeStatus(Epic epic) {
        boolean checkNew = true;
        boolean checkDone = true;

        for (Integer id : epic.getListOfSubtasks()) {
            if (subtasks.get(id).getStatus() != StatusOfTask.NEW) {
                checkNew = false;
                break;
            }
        }
        for (Integer id : epic.getListOfSubtasks()) {
            if (subtasks.get(id).getStatus() != StatusOfTask.DONE) {
                checkDone = false;
                break;
            }
        }
        if (epic.getListOfSubtasks().isEmpty() || checkNew) {
            epic.setStatus(StatusOfTask.NEW);
        } else if (checkDone) {
            epic.setStatus(StatusOfTask.DONE);
        } else {
            epic.setStatus(StatusOfTask.IN_PROGRESS);
        }
    }

    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}
