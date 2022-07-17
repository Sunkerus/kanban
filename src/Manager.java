import java.util.HashMap;
import java.util.ArrayList;

public class Manager {

    int generateId = 0;

    HashMap<Integer, Task> storageTasks = new HashMap<>();

    public int generateHash() {

        generateId += 1;

        return generateId;

    }

    //методы для задач

    public HashMap<Integer, Task> getAllTask(){ //получение задач

        return storageTasks;

    }


    public void removeAllTask(){ //удаление задач
          storageTasks.clear();
    }

    public Task getTaskById(int id) {

        return storageTasks.get(id);

    }

    public void createTask(Task task) { // создание задачи

        storageTasks.put(generateHash(), task);

    }
    public void updateTask(int id, Task task){
        storageTasks.put(id,task);
    }

    public void deleteTaskById(int id){
        storageTasks.remove(id);
    }

    //additional methods

    public ArrayList<Subtask> getListSubtaskOfEpic(Epic epic){
        ArrayList<Subtask> tempArrSubtask = new ArrayList<>();
        for (Integer iterator: storageTasks.keySet()) {
            if (storageTasks.get(iterator).equals(epic)) {
                Epic someEpic = (Epic) storageTasks.get(iterator);
                tempArrSubtask = someEpic.getSubtasks();
            }
        }
        return tempArrSubtask;
    }

}