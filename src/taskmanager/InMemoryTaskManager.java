package taskmanager;

import history.HistoryManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected int nextId = 1;

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
        if (!findTaskWithTheSameTime(task, allTasks())) {
            task.setId(nextId);
            tasks.put(nextId, task);
            nextId++;
        } else throw new NotAvailableTimeException("Задача на данное время уже существует");
    }

    @Override
    public void add(Epic epic) {
        if (!findTaskWithTheSameTime(epic, allEpics())) {
            if (epic.getId() == 0) {
                epic.setId(nextId);
                nextId++;
            }
            epics.put(epic.getId(), epic);
            changeStatus(epic);
        } else throw new NotAvailableTimeException("Задача на данное время уже существует");
    }

    @Override
    public void add(Subtask subtask) {
        if (!findTaskWithTheSameTime(subtask, allSubtasks())) {
            if (subtask.getId() == 0) {
                subtask.setId(nextId);
                nextId++;
            }
            subtasks.put(subtask.getId(), subtask);

            Epic epic1 = epics.get(subtask.getIdOfEpic());
            if (epic1 == null) {
                throw new EpicNotFoundException("Эпика не существует");
            }
            epic1.getListOfSubtasks().add(subtask.getId());
            changeStatus(epic1);
        } else throw new NotAvailableTimeException("Задача на данное время уже существует");
    }

    @Override
    public ArrayList<String> subtasksForEpic(int id) {
        ArrayList<String> listOfSubtasks = new ArrayList<>();
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            if (!subtasks.isEmpty()) {
                epic.getListOfSubtasks().stream().map(subtasksId -> subtasks.get(subtasksId).getName()).forEach(listOfSubtasks::add);
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
            epic.getListOfSubtasks().forEach(subtasks::remove);
        }
    }

    @Override
    public void removeSubtaskPerId(int id) {
        if (!subtasks.containsKey(id)) {
            return;
        }
        int idOfEpic = subtasks.get(id).getIdOfEpic();
        subtasks.remove(id);
        epics.get(idOfEpic).getListOfSubtasks().remove(Integer.valueOf(id));
        changeStatus(epics.get(idOfEpic));
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
        if (subtasks.isEmpty()) {
            epic.setStatus(StatusOfTask.NEW);
        } else {
            boolean checkNew;
            boolean checkDone;
            checkNew = epic.getListOfSubtasks().stream().allMatch(id -> subtasks.get(id).getStatus() == StatusOfTask.NEW);
            checkDone = epic.getListOfSubtasks().stream().allMatch(id -> subtasks.get(id).getStatus() == StatusOfTask.DONE);
            if (epic.getListOfSubtasks().isEmpty() || checkNew) {
                epic.setStatus(StatusOfTask.NEW);
            } else if (checkDone) {
                epic.setStatus(StatusOfTask.DONE);
            } else {
                epic.setStatus(StatusOfTask.IN_PROGRESS);
            }
        }
    }

    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    public <T extends Task> TreeSet<T> getPrioritizedTasks(ArrayList<T> tasks) {
        TreeSet<T> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        tasks.stream().filter(task -> task.getStartTime() != null).forEach(prioritizedTasks::add);
        return prioritizedTasks;
    }

    public void findTimeFromSubtasks(Epic epic) {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        if (!epic.getListOfSubtasks().isEmpty()) {
            LocalDateTime startTime = LocalDateTime.MAX;
            LocalDateTime endTime = LocalDateTime.MIN;
            for (int id : epic.getListOfSubtasks()) {
                Subtask subtask = manager.getSubtaskPerId(id);
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
            epic.setDuration(Duration.between(startTime, endTime));
        }
    }

    public <T extends Task> boolean findTaskWithTheSameTime(T task, ArrayList<T> tasks1) {
        TreeSet<T> tasks = new TreeSet<>(getPrioritizedTasks(tasks1));
        if (tasks.isEmpty()) {
            return false;
        }
        return tasks.stream().anyMatch(t -> findSameTimeTask(task, t));
    }

    public boolean findSameTimeTask(Task task1, Task task2) {
        return task1.getStartTime().isBefore(task2.getEndTime()) && task2.getStartTime().isBefore(task1.getEndTime());
    }
}

