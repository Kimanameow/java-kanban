import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.EpicNotFoundException;
import taskmanager.Managers;
import taskmanager.TaskManager;
import tasks.Epic;
import tasks.StatusOfTask;
import tasks.Subtask;

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
        Epic e1 = new Epic("Name","descr");
        Epic e2 = new Epic("Name", "descr");
        Epic e3 = new Epic("Name", "descr");
        Epic e4 = new Epic("Name", "descr");
        manager.add(e1);
        manager.add(e2);
        manager.add(e3);
        manager.add(e4);

        Subtask s1 = new Subtask("Name", 25, StatusOfTask.NEW, e1.getId());
        Subtask s2 = new Subtask("Name", 26, StatusOfTask.NEW, e1.getId());

        Subtask s3 = new Subtask("Name", 125, StatusOfTask.DONE, e2.getId());
        Subtask s4 = new Subtask("Name", 126, StatusOfTask.DONE, e2.getId());

        Subtask s5 = new Subtask("Name", 225, StatusOfTask.NEW, e3.getId());
        Subtask s6 = new Subtask("Name", 226, StatusOfTask.DONE, e3.getId());

        Subtask s7 = new Subtask("Name", 325, StatusOfTask.IN_PROGRESS, e4.getId());
        Subtask s8 = new Subtask("Name", 326, StatusOfTask.IN_PROGRESS, e4.getId());

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
    public void catchEpicNotFound(){
        Subtask subtask1 = new Subtask("name", 10);
        assertThrows(EpicNotFoundException.class,()-> manager.add(subtask1));
    }
}
