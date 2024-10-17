import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.EpicNotFoundException;
import taskmanager.Managers;
import taskmanager.TaskManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EpicTest {

    TaskManager manager;

    @BeforeEach
    public void createManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void checkingTaskStatus() {
        Epic e1 = new Epic("Name", "descr", 10, StatusOfTask.NEW, LocalDateTime.now(), 100);
        Epic e2 = new Epic("Name", "descr", 20, StatusOfTask.NEW, LocalDateTime.now().plusDays(1), 100);
        Epic e3 = new Epic("Name", "descr", 30, StatusOfTask.NEW, LocalDateTime.now().minusMonths(1), 100);
        Epic e4 = new Epic("Name", "descr", 40, StatusOfTask.NEW, LocalDateTime.now().plusWeeks(4), 100);
        manager.add(e1);
        manager.add(e2);
        manager.add(e3);
        manager.add(e4);

        Subtask s1 = new Subtask("Name", "Descr", 25, StatusOfTask.NEW, e1.getId(), 10, LocalDateTime.now().plusDays(1));
        Subtask s2 = new Subtask("Name", "Descr", 26, StatusOfTask.NEW, e1.getId(), 10, LocalDateTime.now());

        Subtask s3 = new Subtask("Name", "Descr", 125, StatusOfTask.DONE, e2.getId(), 10, LocalDateTime.now().plusDays(3));
        Subtask s4 = new Subtask("Name", "Descr", 126, StatusOfTask.DONE, e2.getId(), 10, LocalDateTime.now().plusDays(4));

        Subtask s5 = new Subtask("Name", "Descr", 225, StatusOfTask.NEW, e3.getId(), 10, LocalDateTime.now().minusDays(1));
        Subtask s6 = new Subtask("Name", "Descr", 425, StatusOfTask.DONE, e3.getId(), 10, LocalDateTime.now().minusDays(2));

        Subtask s7 = new Subtask("Name", "Descr", 525, StatusOfTask.IN_PROGRESS, e4.getId(), 10, LocalDateTime.now().minusHours(2));
        Subtask s8 = new Subtask("Name", "Descr", 625, StatusOfTask.IN_PROGRESS, e4.getId(), 10, LocalDateTime.now().minusDays(6));

        manager.add(s1);
        manager.add(s2);
        manager.add(s3);
        manager.add(s4);
        manager.add(s5);
        manager.add(s6);
        manager.add(s7);
        manager.add(s8);

        assertEquals(StatusOfTask.NEW, e1.getStatus());
        assertEquals(StatusOfTask.DONE, e2.getStatus());
        assertEquals(StatusOfTask.IN_PROGRESS, e3.getStatus());
        assertEquals(StatusOfTask.IN_PROGRESS, e4.getStatus());
    }

    @Test
    public void catchEpicNotFound() {
        Subtask subtask1 = new Subtask("name", 10);
        assertThrows(EpicNotFoundException.class, () -> manager.add(subtask1));
    }
}
