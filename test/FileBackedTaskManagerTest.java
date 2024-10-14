import org.junit.Test;
import taskmanager.FileBackedTaskManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest extends TaskManagerTest {
    File file = new File("taskmanager");
    FileBackedTaskManager manager = new FileBackedTaskManager(file);
    Task task1 = new Task("name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
    Epic epic1 = new Epic("Epic1", "Description1", 30, StatusOfTask.NEW, LocalDateTime.now(), 10);
    Subtask subtask1 = new Subtask("Name", "Descr", 20, StatusOfTask.DONE, epic1.getId(), 10, LocalDateTime.now());


    @Test
    public void testSaveAndLoadEmptyFile() {
        manager.add(task1);
        manager.removeTaskPerId(task1.getId());

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);
        FileBackedTaskManager.loadFromFile(new File("taskmanager"));
        assertTrue(loadedManager.allSubtasks().isEmpty());
        assertTrue(loadedManager.allEpics().isEmpty());
        assertTrue(loadedManager.allSubtasks().isEmpty());
    }

    @Test
    public void sameManagersFromLoadFile() throws IOException {
        manager.add(task1);
        manager.add(epic1);

        FileBackedTaskManager backedManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(manager.allTasks(), backedManager.allTasks());
        assertEquals(manager.allEpics(), backedManager.allEpics());
    }

    @Test
    public void removeTasks() {
        manager.add(task1);
        manager.removeTasks();

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(newManager.allTasks().isEmpty());
    }

    @Test
    public void removeEpics() {
        manager.add(epic1);
        manager.removeEpics();

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(newManager.allEpics().isEmpty());
    }

    @Test
    public void removeSubtasks() {
      /*  manager.add(epic1);
        manager.add(subtask1);
        manager.removeSubtasks();

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(newManager.allSubtasks().isEmpty()); */
    }

    @Test
    public void getTaskPerId() {
        manager.add(task1);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(task1, newManager.getTaskPerId(task1.getId()));
    }

    @Test
    public void getEpicPerId() {
        manager.add(epic1);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(epic1, newManager.getEpicPerId(epic1.getId()));
    }

    @Test
    public void getSubtaskPerId() {
        manager.add(epic1);
        manager.add(subtask1);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(subtask1, newManager.getSubtaskPerId(subtask1.getId()));
    }

    @Test
    public void addNewTask() {
        manager.add(task1);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, newManager.allTasks().size());
    }

    @Test
    public void addNewEpic() {
        manager.add(epic1);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, newManager.allEpics().size());
    }

    @Test
    public void addNewSubtask() {
        manager.add(epic1);
        manager.add(subtask1);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, newManager.allSubtasks().size());
    }

    @Test
    public void subtasksForEpic() {
        manager.add(epic1);
        manager.add(subtask1);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, newManager.subtasksForEpic(subtask1.getIdOfEpic()).size());
    }

    @Test
    public void removeTaskPerId() {
        manager.add(task1);
        manager.removeTaskPerId(task1.getId());

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(newManager.allTasks().isEmpty());
    }

    @Test
    public void removeEpicPerId() {
        manager.add(epic1);
        manager.removeEpicPerId(epic1.getId());

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(newManager.allEpics().isEmpty());
    }

    @Test
    public void removeSubtaskPerId() {
        manager.add(epic1);
        manager.add(subtask1);
        manager.removeSubtaskPerId(subtask1.getId());

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(newManager.allSubtasks().isEmpty());
    }

    @Test
    public void updateTask() {
        manager.add(task1);
        Task newTask = new Task("Name1", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.updateTask(task1.getId(), newTask);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(newManager.allTasks().contains(newTask));
    }

    @Override
    public void updateSubtask() {
        manager.add(epic1);
        manager.add(subtask1);
        Subtask newSubtask = new Subtask("name", "descr", subtask1.getId(), StatusOfTask.DONE, epic1.getId(), 10, LocalDateTime.now());
        manager.updateSubtask(subtask1.getId(), newSubtask);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(newManager.allSubtasks().contains(newSubtask));
    }

    @Test
    public void updateEpic() {
        manager.add(epic1);
        Epic newTask = new Epic("Name1", "descr", 20, StatusOfTask.NEW, LocalDateTime.now(), 10);
        manager.updateEpic(epic1.getId(), newTask);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(newManager.allEpics().contains(newTask));
    }

    @Test
    public void changeStatus() {
        manager.add(epic1);
        manager.add(subtask1);

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(StatusOfTask.DONE, newManager.getEpicPerId(epic1.getId()).getStatus());
    }

    @Test
    public void getHistory() {
        /*manager.add(epic1);
        manager.getEpicPerId(epic1.getId());

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, newManager.getHistory().size());*/
    }
}
