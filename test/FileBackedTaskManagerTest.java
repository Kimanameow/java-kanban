import org.junit.Test;
import taskmanager.FileBackedTaskManager;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    @Test
    public void testSaveAndLoadEmptyFile() {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        manager.save();

        FileBackedTaskManager loadedManager = new FileBackedTaskManager();
        loadedManager.loadFromFile(new File("taskmanager"));
        assertTrue(loadedManager.allSubtasks().isEmpty());
        assertTrue(loadedManager.allEpics().isEmpty());
        assertTrue(loadedManager.allSubtasks().isEmpty());
    }

  /*  @Test
    public void sameManagersFromLoadFile(){
        FileBackedTaskManager manager = new FileBackedTaskManager();
        Task task1 = new Task("Task1", "Description1");
        Task task2 = new Task("Task2", "Description2");
        Epic epic1 = new Epic("Epic1", "Description1");
        manager.add(task1);
        manager.add(task2);
        manager.add(epic1);

        FileBackedTaskManager loadedManager = manager.loadFromFile(new File("taskmanager"));
        assertEquals(manager.allTasks(), loadedManager.allTasks());
        assertEquals(manager.allEpics(), loadedManager.allEpics());
        }
        */

}
