import org.junit.Test;
import taskmanager.FileBackedTaskManager;
import tasks.Epic;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    @Test
    public void testSaveAndLoadEmptyFile() {
        File file = new File("taskmanager");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.save();

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);
        FileBackedTaskManager.loadFromFile(new File("taskmanager"));
        assertTrue(loadedManager.allSubtasks().isEmpty());
        assertTrue(loadedManager.allEpics().isEmpty());
        assertTrue(loadedManager.allSubtasks().isEmpty());
    }

    @Test
    public void sameManagersFromLoadFile() throws IOException {
        File temp = File.createTempFile("tasks", "csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(temp);
        Task task1 = new Task("Task1", "Description1");
        Task task2 = new Task("Task2", "Description2");
        Epic epic1 = new Epic("Epic1", "Description1");
        manager.add(task1);
        manager.add(task2);
        manager.add(epic1);

        FileBackedTaskManager backedManager = FileBackedTaskManager.loadFromFile(temp);
        assertEquals(manager.allTasks(), backedManager.allTasks());
        assertEquals(manager.allEpics(), backedManager.allEpics());
    }
}
