package tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected String name;
    protected String description;
    protected Integer id;
    protected StatusTask status;

    protected LocalDateTime startTime;

    protected long duration;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.NEW;
        this.duration = 0;
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
                "startTime = " + startTime + ", " +
                "duration = " + duration + ", " +
                "endTime = " + getEndTime();
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


    public long getDuration() {
        return duration;
    }
    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return LocalDateTime.from(startTime).plusMinutes(duration);
        }else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task subtask = (Task) o;
        return Objects.equals(getId(), subtask.getId()) &&
                Objects.equals(subtask.getName(), getName()) &&
                Objects.equals(subtask.getDescription(), getDescription()) &&
                Objects.equals(subtask.getStatus(), getStatus());
    }
}
