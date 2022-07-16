import java.util.HashMap;

public class Manager {
    int generateId = 0;

    HashMap<Integer, Task> storageTasks = new HashMap<>();

    public int generateHash() {
        generateId += 1;
        return generateId;
    }

    public void addTask(String name, String description) {
       
        storageTasks.put(generateHash(), new Task(name,description));
    }

    public void addEpic(String name, String description, Subtask subtasks) {
        storageTasks.put(generateHash(), new Epic(name,description,subtasks));
    }


}
