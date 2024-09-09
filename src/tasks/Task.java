package tasks;

import java.lang.reflect.Type;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private int id;
    private StatusOfTask status = StatusOfTask.NEW;

    public Task(String name, String description, int id, StatusOfTask status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(StatusOfTask status) {
        this.status = status;
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeOfTask getType() {
        return TypeOfTask.TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%d;%s;%s;%s;%s", id, getType(), name, status, description);
    }
}






