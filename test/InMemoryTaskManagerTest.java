import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.Managers;
import taskmanager.NotAvailableTimeException;
import taskmanager.TaskManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {
    TaskManager task;

    @BeforeEach
    public void createManagerForEachTest() {
        task = Managers.getDefault();
    }

    @Test
    void taskWithNextIdAndOurId() {
        Task task1 = new Task("Task1", "Description");
        task.add(task1);
        int id1 = task1.getId();
        Task task2 = new Task("Task2", 258);
        task.add(task2);
        assertNotEquals(id1, 258);
        assertTrue(task.allTasks().contains(task1));
        assertTrue(task.allTasks().contains(task2));
    }

    @Test
    void addAndFindTasksPerId() {
        Task task1 = new Task("Task1", 1);
        Epic epic1 = new Epic("Epic", 2);
        Subtask subtask1 = new Subtask("Subtask", 3, 2);

        task.add(task1);
        task.add(epic1);
        task.add(subtask1);

        assertTrue(task.allTasks().contains(task1));
        assertTrue(task.allEpics().contains(epic1));
        assertTrue(task.allSubtasks().contains(subtask1));

        assertNotNull(task.getTaskPerId(1));
        assertNotNull(task.getEpicPerId(2));
        assertNotNull(task.getSubtaskPerId(3));
    }

    @Test
    void addNewTask() {
        Task task1 = new Task("TASK", " Descr", 1, StatusOfTask.IN_PROGRESS);
        task.add(task1);
        final Task savedTask = task.getTaskPerId(1);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = task.allTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic1 = new Epic("Epic 1", 2);
        task.add(epic1);
        assertTrue(task.allEpics().contains(epic1));
    }

    @Test
    void addNewSubtask() {
        Epic epic1 = new Epic("Epic1", 25);
        task.add(epic1);
        Subtask subtask1 = new Subtask("Subtask 1", 3, epic1.getId());
        task.add(subtask1);
        assertTrue(task.allSubtasks().contains(subtask1));
    }

    @Test
    void subtasksForEpic() {
        Epic e1 = new Epic("Name", "descr");
        task.add(e1);
        Subtask subtask1 = new Subtask("Name", "Descr", e1.getId());
        task.add(subtask1);
        assertNotNull(task.subtasksForEpic(e1.getId()));
        assertEquals(1, task.subtasksForEpic(e1.getId()).size());
    }

    @Test
    void updateTask() {
        Task task1 = new Task("name", "descr");
        task.add(task1);
        Task newTask = new Task("name", task1.getId());
        task.updateTask(task1.getId(), newTask);
        assertEquals(1, task.allTasks().size());
    }

    @Override
    void updateSubtask() {

    }

    @Test
    void updateEpic() {
        Epic e1 = new Epic("Name", "descr");
        task.add(e1);
        Epic e2 = new Epic("Name", e1.getId());
        task.updateEpic(e1.getId(), e2);
        assertEquals(1, task.allEpics().size());
    }

    @Test
    void changeStatus() {
        Epic e1 = new Epic("Name", "descr");
        task.add(e1);
        Subtask subtask1 = new Subtask("Name", 10, StatusOfTask.DONE, e1.getId());
        task.add(subtask1);
        assertEquals(StatusOfTask.DONE, e1.getStatus());
    }

    @Test
    void getHistory() {
        Epic e1 = new Epic("Name", "descr");
        task.add(e1);
        task.getEpicPerId(e1.getId());
        assertFalse(task.getHistory().isEmpty());
    }

    @Test
    void removeTaskPerId() {
        Task task1 = new Task("Task", "Description");
        task.add(task1);
        int thisId = task1.getId();
        task.removeTaskPerId(thisId);
        assertFalse(task.allTasks().contains(task1));
    }

    @Test
    void removeEpicPerId() {
        Epic epic1 = new Epic("Epic", "Descriptions");
        task.add(epic1);
        task.removeEpicPerId(epic1.getId());
        assertFalse(task.allEpics().contains(epic1));
    }

    @Test
    void removeSubtaskPerId() {
        Epic e1 = new Epic("Name", "descr");
        task.add(e1);
        Subtask subtask1 = new Subtask("Name", "Descr", e1.getId());
        task.add(subtask1);
        task.removeSubtaskPerId(subtask1.getId());
        assertFalse(task.allSubtasks().contains(subtask1));
    }

    @Test
    void removeTasks() {
        Task task1 = new Task("Task", "Description");
        task.add(task1);
        task.removeTasks();
        assertTrue(task.allTasks().isEmpty());
    }

    @Test
    void removeEpics() {
        Epic e1 = new Epic("Name", "descr");
        task.add(e1);
        task.removeEpics();
        assertTrue(task.allEpics().isEmpty());
    }

    @Test
    void removeSubtasks() {
        Epic epic1 = new Epic("name", "descr");
        task.add(epic1);
        Subtask sbtsk1 = new Subtask("Name", "descr", epic1.getId());
        task.add(sbtsk1);
        task.removeSubtasks();
        assertTrue(task.allSubtasks().isEmpty());
    }

    @Test
    void getTaskPerId() {
        Task task1 = new Task("Name", "descr");
        task.add(task1);
        assertEquals(task.getTaskPerId(task1.getId()), task1);
    }

    @Test
    void getEpicPerId() {
        Epic epic1 = new Epic("name", "descr");
        task.add(epic1);
        assertEquals(task.getEpicPerId(epic1.getId()), epic1);
    }

    @Test
    void getSubtaskPerId() {
        Epic epic1 = new Epic("name", "descr");
        task.add(epic1);
        Subtask sbtsk1 = new Subtask("Name", "descr", epic1.getId());
        task.add(sbtsk1);
        assertEquals(task.getSubtaskPerId(sbtsk1.getId()), sbtsk1);
    }

    @Test
    public void catchTimeException() {
        LocalDateTime sbtsk1Time = LocalDateTime.now();
        Epic epic1 = new Epic("Name", "Descr");
        task.add(epic1);
        Subtask subtask1 = new Subtask("Name", "Descr", 50, StatusOfTask.NEW, epic1.getId(), 20, sbtsk1Time);
        Subtask subtask2 = new Subtask("name", "Descr", 20, StatusOfTask.NEW, epic1.getId(), 20, sbtsk1Time);
        task.add(subtask1);
        assertThrows(NotAvailableTimeException.class, () -> task.add(subtask2));
    }
}


