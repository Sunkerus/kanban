package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import managers.InMemoryTaskManager;
public class Epic extends Task {

    private final ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addId(Integer id) {
        subtasksId.add(id);
    }

    public void deleteId(Integer valueId) {
        subtasksId.remove(valueId);
    }

    public void setSubtasksId(Integer id) {
        subtasksId.add(id);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearAllId() {
        subtasksId.clear();
    }

    private LocalDateTime endTime;
    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {

        return "Type: Epic, " +
                "Id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "statusTask = " + status + ", " +
                "startTime = " + startTime + ", " +
                "duration = " + duration + ", " +
                "endTime = " + endTime + ", " +
                "subtaskInto = " + subtasksId;

    }
}