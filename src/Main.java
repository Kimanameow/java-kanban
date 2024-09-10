import taskmanager.Managers;
import taskmanager.TaskManager;
import tasks.*;

import java.io.File;


public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("Поехали!");
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        manager.add(task1);
        manager.add(task2);
        Epic epic1 = new Epic("Переезд", "Готовимся");
        manager.add(epic1);
        Subtask subtask1 = new Subtask("Собрать коробки", "Описание", epic1.getId());
        Subtask subtask2 = new Subtask("Упаковать кошку", "Описание", epic1.getId());
        manager.add(subtask1);
        manager.add(subtask2);

        Epic epic2 = new Epic("Важный эпик 2", "Описание");
        manager.add(epic2);
        Subtask subtask3 = new Subtask("Подзадача важного эпика 2", "Описание", epic2.getId());
        manager.add(subtask3);

        System.out.println(manager.allTasks().toString());
        System.out.println(manager.allEpics().toString());
        System.out.println(manager.allSubtasks().toString());

        task1.setStatus(StatusOfTask.IN_PROGRESS);
        subtask1.setStatus(StatusOfTask.DONE);
        manager.updateTask(task1.getId(), task1);
        manager.updateSubtask(subtask1.getId(), subtask1);
        System.out.println(task1);
        System.out.println(subtask1);
        System.out.println(epic1);

        manager.removeTaskPerId(task2.getId());
        manager.removeEpicPerId(epic2.getId());
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.allTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.allEpics()) {
            System.out.println(epic);

        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.allSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
