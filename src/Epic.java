import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksId = new ArrayList<>();

    Epic(String name, String description) {

        super(name, description);

    }

    public void addId(Integer id) {
        subtasksId.add(id);
    }

    public void deleteId(Integer valueId) {
        subtasksId.remove(subtasksId.indexOf(valueId));
    }


    public void setSubtasksId(Integer id) {
        subtasksId.add(id);
    }

    public ArrayList<Integer> getSubtasksId() {

        return subtasksId;
    }

    @Override
    public String toString() {

        return "Type: Epic, " +
                        "Id = " + id  + ", " +
                        "name = " + name + ", " +
                        "description = " + description + ", " +
                        "statusTask = " + statusTask + ", " +
                        "subtaskInto = " + subtasksId;
    }




}