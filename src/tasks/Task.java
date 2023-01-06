package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected Integer id;
    protected StatusTask status;

    protected LocalDateTime startTime = LocalDateTime.now();
    protected long duration;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.NEW;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public StatusTask getStatus() {
        return status;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {

        return "Type: Task, " +
                "Id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "statusTask = " + status + ", " +
                "StartTime" + startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        try {
            return LocalDateTime.from(startTime).plusMinutes(duration);
        } catch (NullPointerException ex) {
            throw new RuntimeException("Время начала выполнения задачи или время выполнеиня не указаны");
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subtask subtask = (Subtask) obj;
        return Objects.equals(getId(), subtask.getId()) &&
                Objects.equals(subtask.getName(), getName()) &&
                Objects.equals(subtask.getDescription(), getDescription()) &&
                Objects.equals(subtask.getStatus(), getStatus());
    }
}
