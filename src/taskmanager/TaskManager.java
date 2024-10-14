package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.TreeSet;

public interface TaskManager {
    ArrayList<Task> allTasks();

    ArrayList<Epic> allEpics();

    ArrayList<Subtask> allSubtasks();

    void removeTasks();

    void removeEpics();

    void removeSubtasks();

    Task getTaskPerId(int id);

    Epic getEpicPerId(int id);

    Subtask getSubtaskPerId(int id);

    void add(Task task);

    void add(Epic epic);

    void add(Subtask subtask);

    ArrayList<String> subtasksForEpic(int id);

    void removeTaskPerId(int id);

    void removeEpicPerId(int id);

    void removeSubtaskPerId(int id);

    void updateTask(int id, Task task);

    void updateSubtask(int id, Subtask subtask);

    void updateEpic(int id, Epic epic);

    void changeStatus(Epic epic);

    ArrayList<Task> getHistory();
}
