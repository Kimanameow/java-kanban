import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Task 1", 1);
        Task task2 = new Task("Task 2", 1);
        assertEquals(task1, task2);
    }


    @Test
    public void subclassesWithSameIdShouldBeEqual() {
        Task epic = new Epic("Epic 1", 1);
        Task epic1 = new Epic("Epic", 1);
        assertEquals(epic, epic1);

    }
}