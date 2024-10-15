import taskmanager.TaskManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    void removeTasks() {
        Task task1 = new Task("Task", "Description");
        taskManager.add(task1);
        taskManager.removeTasks();
        assertTrue(taskManager.allTasks().isEmpty());
    }

    void removeEpics() {
        Epic e1 = new Epic("Name", "descr");
        taskManager.add(e1);
        taskManager.removeEpics();
        assertTrue(taskManager.allEpics().isEmpty());
    }

    void removeSubtasks() {
        Epic epic1 = new Epic("name", "descr");
        taskManager.add(epic1);
        Subtask sbtsk1 = new Subtask("Name", "descr", epic1.getId());
        taskManager.add(sbtsk1);
        taskManager.removeSubtasks();
        assertTrue(taskManager.allSubtasks().isEmpty());
    }

    void getTaskPerId() {
        Task task1 = new Task("Name", "descr");
        taskManager.add(task1);
        assertEquals(taskManager.getTaskPerId(task1.getId()), task1);
    }

    void getEpicPerId() {
        Epic epic1 = new Epic("name", "descr");
        taskManager.add(epic1);
        assertEquals(taskManager.getEpicPerId(epic1.getId()), epic1);
    }

    void getSubtaskPerId() {
        Epic epic1 = new Epic("name", "descr");
        taskManager.add(epic1);
        Subtask sbtsk1 = new Subtask("Name", "descr", epic1.getId());
        taskManager.add(sbtsk1);
        assertEquals(taskManager.getSubtaskPerId(sbtsk1.getId()), sbtsk1);
    }

    void addNewTask() {
        Task task1 = new Task("TASK", " Descr", 1, StatusOfTask.IN_PROGRESS);
        taskManager.add(task1);
        final Task savedTask = taskManager.getTaskPerId(1);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = taskManager.allTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    void addNewEpic() {
        Epic epic1 = new Epic("Epic 1", 2);
        taskManager.add(epic1);
        assertTrue(taskManager.allEpics().contains(epic1));
    }

    void addNewSubtask() {
        Epic epic1 = new Epic("Epic1", 25);
        taskManager.add(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", 3, epic1.getId());
        taskManager.add(subtask1);
        assertTrue(taskManager.allSubtasks().contains(subtask1));
    }

    void subtasksForEpic() {
        Epic e1 = new Epic("Name", "descr");
        taskManager.add(e1);
        Subtask subtask1 = new Subtask("Name", "Descr", e1.getId());
        taskManager.add(subtask1);
        assertNotNull(taskManager.subtasksForEpic(e1.getId()));
        assertEquals(1, taskManager.subtasksForEpic(e1.getId()).size());
    }

    void removeTaskPerId() {
        Task task1 = new Task("Task", "Description");
        taskManager.add(task1);
        int thisId = task1.getId();
        taskManager.removeTaskPerId(thisId);
        assertFalse(taskManager.allTasks().contains(task1));
    }

    void removeEpicPerId() {
        Epic epic1 = new Epic("Epic", "Descriptions");
        taskManager.add(epic1);
        taskManager.removeEpicPerId(epic1.getId());
        assertFalse(taskManager.allEpics().contains(epic1));
    }

    void removeSubtaskPerId() {
        Epic e1 = new Epic("Name", "descr");
        taskManager.add(e1);
        Subtask subtask1 = new Subtask("Name", "Descr", e1.getId());
        taskManager.add(subtask1);
        taskManager.removeSubtaskPerId(subtask1.getId());
        assertFalse(taskManager.allSubtasks().contains(subtask1));
    }

    void updateTask() {
        Task task1 = new Task("name", "descr");
        taskManager.add(task1);
        Task newTask = new Task("name", task1.getId());
        taskManager.updateTask(task1.getId(), newTask);
        assertEquals(1, taskManager.allTasks().size());
    }

    void updateSubtask() {
        Epic e1 = new Epic("Name", "descr");
        taskManager.add(e1);
        Subtask subtask1 = new Subtask("Name", "Descr", e1.getId());
        taskManager.add(subtask1);
        Subtask newSubtask = new Subtask("Name", "Descr", e1.getId());
        taskManager.updateSubtask(newSubtask.getId(), newSubtask);
        assertEquals(1, taskManager.allSubtasks().size());
    }

    void updateEpic() {
        Epic e1 = new Epic("Name", "descr");
        taskManager.add(e1);
        Epic e2 = new Epic("Name", e1.getId());
        taskManager.updateEpic(e1.getId(), e2);
        assertEquals(1, taskManager.allEpics().size());
    }

    void changeStatus() {
        Epic e1 = new Epic("Name", "descr");
        taskManager.add(e1);
        Subtask subtask1 = new Subtask("Name", 10, StatusOfTask.DONE, e1.getId());
        taskManager.add(subtask1);
        assertEquals(StatusOfTask.DONE, e1.getStatus());
    }

    void getHistory() {
        Epic e1 = new Epic("Name", "descr");
        taskManager.add(e1);
        taskManager.getEpicPerId(e1.getId());
        assertFalse(taskManager.getHistory().isEmpty());
        assertEquals(1, taskManager.getHistory().size());
    }
}
