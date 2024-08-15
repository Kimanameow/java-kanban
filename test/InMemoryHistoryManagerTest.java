import history.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.Managers;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    public void createManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void addHistory() {
        Task task1 = new Task("Name", 204);
        historyManager.add(task1);
        assert (historyManager.getHistory().contains(task1));
    }

    @Test
    void removeTaskFromHistory() {
        Task task1 = new Task("Name", 304);
        historyManager.add(task1);
        historyManager.remove(304);
        assertFalse(historyManager.getHistory().contains(task1));
    }

    @Test
    void getEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void addTaskWithEqualId() {
        Task task1 = new Task("Name1", 204);
        Task task2 = new Task("Name2", 204);
        historyManager.add(task1);
        historyManager.add(task2);
        assertEquals(1, historyManager.getHistory().size());
        assertTrue(historyManager.getHistory().contains(task2));
    }

}
