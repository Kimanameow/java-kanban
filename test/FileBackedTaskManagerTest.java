import org.junit.Test;
import taskmanager.FileBackedTaskManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file = new File("taskmanager");
    FileBackedTaskManager manager = new FileBackedTaskManager(file);
    Task task1 = new Task("name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 10);
    Epic epic1 = new Epic("Epic1", "Description1", 30, StatusOfTask.NEW, LocalDateTime.now(), 10);

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
}
