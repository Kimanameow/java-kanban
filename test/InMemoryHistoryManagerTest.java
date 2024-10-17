import history.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.Managers;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    public void createManager() {
        historyManager = Managers.getDefaultHistory();
    }

    Task task1 = new Task("Name1", 104);
    Task task2 = new Task("Name2", 204);
    Task task3 = new Task("Name3", 304);


    @Test
    void addHistory() {
        historyManager.add(task1);
        assertTrue(historyManager.getHistory().contains(task1));
    }

    @Test
    void removeTaskFromHistory() {
        historyManager.add(task1);
        historyManager.remove(104);
        assertFalse(historyManager.getHistory().contains(task1));
    }

    @Test
    void getEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void addTaskWithEqualId() {
        Task task4 = new Task("Name1", 404);
        Task task5 = new Task("Name2", 404);
        historyManager.add(task4);
        historyManager.add(task5);
        assertEquals(1, historyManager.getHistory().size());
        assertTrue(historyManager.getHistory().contains(task5));
    }

    @Test
    void removeFirstTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task1.getId());
        ArrayList<Task> thisHistory = new ArrayList<>();
        thisHistory.add(task2);
        thisHistory.add(task3);
        assertEquals(historyManager.getHistory(), thisHistory);
    }

    @Test
    void removeMiddleTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        ArrayList<Task> thisHistory = new ArrayList<>();
        thisHistory.add(task1);
        thisHistory.add(task3);
        assertEquals(historyManager.getHistory(), thisHistory);
    }

    @Test
    void removeLastTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());
        ArrayList<Task> thisHistory = new ArrayList<>();
        thisHistory.add(task1);
        thisHistory.add(task2);
        assertEquals(historyManager.getHistory(), thisHistory);
    }

    @Test
    void removeTaskFromEmptyHistory() {
        historyManager.remove(1);
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void removeOneTask() {
        historyManager.add(task1);
        historyManager.remove(task1.getId());
        assertTrue(historyManager.getHistory().isEmpty());
    }
}

