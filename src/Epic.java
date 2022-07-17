import java.util.ArrayList;

public class Epic extends Task{

    ArrayList<Subtask> subtasks = new ArrayList<>();

    Epic(String name, String description, Subtask subtasks) {

        super(name, description);

        this.subtasks.add(subtasks);

    }

    public void setSubtask(String name, String description) {

        subtasks.add(new Subtask(name,description));

    }

    public ArrayList<Subtask> getSubtasks() {

        return subtasks;

    }

    @Override
    public String toString () {

        return "qwe";

    }

}