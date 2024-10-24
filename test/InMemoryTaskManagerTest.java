import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.InMemoryTaskManager;
import taskmanager.Managers;
import taskmanager.NotAvailableTimeException;
import taskmanager.TaskManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    @BeforeEach
    public void createManagerForEachTest() {
        taskManager = Managers.getDefault();
    }

    @Test
    void taskWithNextIdAndOurId() {
        Task task1 = new Task("Task1", "Description", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        taskManager.add(task1);
        int id1 = task1.getId();
        Task task2 = new Task("Task1", "Description", 10, StatusOfTask.NEW, LocalDateTime.now().plusDays(10), 10);
        taskManager.add(task2);
        assertNotEquals(id1, 258);
        assertTrue(taskManager.allTasks().contains(task1));
        assertTrue(taskManager.allTasks().contains(task2));
    }

    @Test
    void addAndFindTasksPerId() {
        Task task1 = new Task("Task1", "Description", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        Epic epic1 = new Epic("Task1", "Description", 100, StatusOfTask.NEW, LocalDateTime.now(), 26);

        taskManager.add(task1);
        taskManager.add(epic1);

        assertTrue(taskManager.allTasks().contains(task1));
        assertTrue(taskManager.allEpics().contains(epic1));

        assertNotNull(taskManager.getTaskPerId(task1.getId()));
        assertNotNull(taskManager.getEpicPerId(epic1.getId()));
    }

    @Test
    public void catchTimeException() {
        LocalDateTime sbtsk1Time = LocalDateTime.now();
        Epic epic1 = new Epic("Name", "Descr");
        taskManager.add(epic1);
        Subtask subtask1 = new Subtask("Name", "Descr", 50, StatusOfTask.NEW, epic1.getId(), 20, sbtsk1Time);
        Subtask subtask2 = new Subtask("name", "Descr", 20, StatusOfTask.NEW, epic1.getId(), 20, sbtsk1Time);
        taskManager.add(subtask1);
        assertThrows(NotAvailableTimeException.class, () -> taskManager.add(subtask2));
    }

    @Test
    public void testSortedSet() {
        LocalDateTime task1Time = LocalDateTime.now().minusHours(2);
        LocalDateTime task2Time = LocalDateTime.now().plusDays(10);
        Task task1 = new Task("Name", "Des", 10, StatusOfTask.NEW, task1Time, 1000);
        Task task2 = new Task("Name", "Des", 10, StatusOfTask.NEW, task2Time, 1000);
        taskManager.add(task1);
        taskManager.add(task2);

        assertEquals(2, taskManager.getPrioritizedTasks().size());
        assertEquals(task1, taskManager.getPrioritizedTasks().getFirst());
    }
}


