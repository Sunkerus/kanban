package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksId = new ArrayList<>();

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

    @Override
    public String toString() {

        return "Type: Epic, " +
                "Id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "statusTask = " + status + ", " +
                "startTime = " + startTime + ", " +
                "subtaskInto = " + subtasksId;

    }
}