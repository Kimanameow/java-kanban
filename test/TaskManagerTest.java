import taskmanager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

abstract class TaskManagerTest<T extends TaskManager> {

    abstract void removeTasks();

    abstract void removeEpics();

    abstract void removeSubtasks();

    abstract void getTaskPerId();

    abstract void getEpicPerId();

    abstract void getSubtaskPerId();

    abstract void addNewTask();

    abstract void addNewEpic();

    abstract void addNewSubtask();

    abstract void subtasksForEpic();

    abstract void removeTaskPerId();

    abstract void removeEpicPerId();

    abstract void removeSubtaskPerId();

    abstract void updateTask();

    abstract void updateSubtask();

    abstract void updateEpic();

    abstract void changeStatus();

    abstract void getHistory();
}
