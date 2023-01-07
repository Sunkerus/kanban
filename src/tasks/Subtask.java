package tasks;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description) {

        super(name, description);
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {

        return "Type: Subtask, " +
                "Id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "statusTask = " + status + ", " +
                "startTime = " + startTime;
    }

}