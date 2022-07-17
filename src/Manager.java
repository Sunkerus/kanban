import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

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

    public void updateEpic(int id, Epic epic) {
        //updateEpicStatus
        boolean checkStatusNEW = true;
        boolean checkStatusDONE = true;
        if (!epic.getSubtasks().isEmpty()){
        for (Subtask iterator: epic.getSubtasks()){
            if (!Objects.equals(iterator.getStatus(), "NEW")){
                checkStatusNEW = false;
            }
            if (!Objects.equals(iterator.getStatus(), "DONE")){
                checkStatusDONE = false;
            }
            }
        if(checkStatusNEW){
            epic.setStatus(0);
        }else if (checkStatusDONE){
            epic.setStatus(2);
        }else {
            epic.setStatus(1);
        }
        }
        storageTasks.put(id, epic);

    }


    public void addSubtask(int id ,Subtask subtask){
        Epic someEpic = (Epic) storageTasks.get(id);
        someEpic.setSubtask(subtask);
        //updateEpicStatus
        boolean checkStatusNEW = true;
        boolean checkStatusDONE = true;
        if (!someEpic.getSubtasks().isEmpty()){
            for (Subtask iterator: someEpic.getSubtasks()){
                if (!Objects.equals(iterator.getStatus(), "NEW")){
                    checkStatusNEW = false;
                }
                if (!Objects.equals(iterator.getStatus(), "DONE")){
                    checkStatusDONE = false;
                }
            }
            if(checkStatusNEW){
                someEpic.setStatus(0);
            }else if (checkStatusDONE){
                someEpic.setStatus(2);
            }else {
                someEpic.setStatus(1);
            }
        }
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