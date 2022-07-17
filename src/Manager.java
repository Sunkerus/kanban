import java.util.HashMap;

public class Manager {

    int generateId = 0;

    HashMap<Integer, Task> storageTasks = new HashMap<>();

    public int generateHash() {

        generateId += 1;

        return generateId;

    }

    //методы для задач

    public HashMap<Integer, Task> getAllTask(){
        HashMap<Integer, Task> tempHash = new HashMap<>();
        for (Integer iterator: storageTasks.keySet()) {
            if (storageTasks.get(iterator).getClass().getSimpleName() == "Task"){
               tempHash.put(iterator, storageTasks.get(iterator));
            }
            return tempHash;
        }

    }


    public void createTask(String name, String description) { // fin

        storageTasks.put(generateHash(), new Task(name, description));

    }

    public void


    public void addEpic(String name, String description, Subtask subtasks) { //fin

        storageTasks.put(generateHash(), new Epic(name, description, subtasks));

    }

    /*public Task addSubtask(int id, String name, String description) { //fin

        if (storageTasks.containsKey(id)) { //выполнить проверку на epic

            storageTasks.get(id).setSubtask(name, description);

        } else {

            System.out.println("no epic task with this id");

        } */

        public void changeStatus(int id, int statusId){

            if (storageTasks.containsKey(id)) { //выполнить проверку на epic
                storageTasks.get(id).setStatus(statusId);
            } else {
                System.out.println("task with this id not found");

            }
    }

    public void deleteAllTask() {

        storageTasks.clear();

    }

    public Task getTaskById(int id) {

        return storageTasks.get(id);

    }

    public Task getEpicSubtask(int id) {

        return storageTasks.get(id).getSubtasks();

    }

//updateTask

    public void updateTask(int id, Task newTask) {

        if (storageTasks.containsKey(id))

            storageTasks.put(id, newTask);

    }

    public void deleteTask(int id) {

        storageTasks.remove(id);

        System.out.println("Task delete succsessful");

//for epic need add status and update ststus block upper in codw

    }

    public void setSubtask() {

    }
}