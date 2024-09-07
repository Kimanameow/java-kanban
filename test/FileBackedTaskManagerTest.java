import org.junit.Test;
import taskmanager.FileBackedTaskManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {

    @Test
    public void testSaveAndLoadEmptyFile() {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(new File("taskmanager"));
        assertTrue(loadedManager.allSubtasks().isEmpty());
        assertTrue(loadedManager.allEpics().isEmpty());
        assertTrue(loadedManager.allSubtasks().isEmpty());
    }
}
