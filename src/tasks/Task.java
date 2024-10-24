package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private int id;
    private StatusOfTask status = StatusOfTask.NEW;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(String name, String description, int id, StatusOfTask status, LocalDateTime startTime, int minutesOfDuration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        setStartTime(startTime);
        this.duration = Duration.ofMinutes(minutesOfDuration);
    }

    public Task(String name, String description, int id, StatusOfTask status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String name, int id, StatusOfTask status) {
        this.name = name;
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

    public void setStartTime(LocalDateTime startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss.SSSSSS");
        startTime.format(formatter);
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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

    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s", id, getType(), name, status, description, duration.toMinutes(), formatLocalDateTime(startTime));
    }

    public String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }
}