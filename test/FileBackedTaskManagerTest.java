import org.junit.Test;
import taskmanager.FileBackedTaskManager;
import tasks.*;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    @Test
    public void testSaveAndLoadEmptyFile() {
        File file = new File("taskmanager");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        manager.save();

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);
        loadedManager.loadFromFile(new File("taskmanager"));
        assertTrue(loadedManager.allSubtasks().isEmpty());
        assertTrue(loadedManager.allEpics().isEmpty());
        assertTrue(loadedManager.allSubtasks().isEmpty());
    }

    @Test
    public void sameManagersFromLoadFile() {
        File file = new File("Taskmanager");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        Task task1 = new Task("Task1", "Description1");
        Task task2 = new Task("Task2", "Description2");
        Epic epic1 = new Epic("Epic1", "Description1");
        manager.add(task1);
        manager.add(task2);
        manager.add(epic1);

        manager.loadFromFile(file);
        assertEquals(2, manager.allTasks().size());
        assertEquals(1, manager.allEpics().size());
    }

}
