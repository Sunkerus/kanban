import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

public class Manager {

    private int generateId = 0;

    HashMap<Integer, Task> storageTask = new HashMap<>();
    HashMap<Integer, Epic> storageEpic = new HashMap<>();
    HashMap<Integer, Subtask> storageSubtask = new HashMap<>();

    public int generateId() {

        generateId += 1;

        return generateId;

    }

    //методы для задач

    public HashMap<Integer,Task> getAllTask() { //получение списка задач
        return storageTask;
    }


    public void removeAllTask() { //удаление всех задач
        storageTask.clear();
    }

    public Task getTaskById(int id) { //получение по id

        return storageTask.get(id);

    }

    public void createTask(Task task) { // создание задачи(объект передаётся в качестве параметра)

        storageTask.put(generateId(), task);
        task.setId(generateId);


    }

    public void updateTask(Task task) { // обновление задачи

        storageTask.put(task.getId(), task);
    }

    public void deleteTaskById(int id) {
        storageTask.remove(id);
    }


    //методы для эпиков

    public HashMap<Integer,Epic> getAllEpic() { //получение списка эпиков
        return storageEpic;
    }


    public void removeAllEpic() { //удаление всех эпиков
        storageEpic.clear();
    }

    public Epic getEpicById(int id) { //получение по id

        return storageEpic.get(id);

    }

    public void createEpic(Epic epic) { // создание эпика(объект передаётся в качестве параметра)

        storageEpic.put(generateId(), epic);
        epic.setId(generateId);

    }

    public void updateEpic(Epic epic) { // обновление эпика
        storageTask.put(epic.getId(), epic);
    }

    public void deleteEpicById(int id) {
        ArrayList<Integer> tempSubtaskIdArr = storageEpic.get(id).getSubtasksId();

        for (Integer iterator : tempSubtaskIdArr) {
            deleteSubtaskById(iterator);
        }
        storageEpic.remove(id);
    }


    //методя для подзадач

    public HashMap<Integer,Subtask> getAllSubtask() { //получение списка подзадач
        return storageSubtask;
    }


    public void removeAllSubtask() { //удаление всех субтасков
        for (Epic iterator : storageEpic.values()) { //обновление статусов всех эпиков
            iterator.setStatus(0);
        }
        storageSubtask.clear();
    }

    public Subtask getSubtaskById(int id) { //получение по id субтасков


        return storageSubtask.get(id);

    }

    public void createSubtask(Subtask subtask) { // создание сабтаска(объект передаётся в качестве параметра)

        storageSubtask.put(generateId(), subtask);      //добавление подзадачи в MAP для подзадач  в эпике
        subtask.setId(generateId); //присвоение id
        storageEpic.get(subtask.getEpicId()).addId(subtask.getId()); //добавление идентификатора в список подзадач эпика
        updateEpicStatus(subtask.getEpicId(), storageEpic.get(subtask.getEpicId()));   //обновление статуса эпика



    }

    public void updateSubtask(Subtask subtask) { // обновление подзадачи

        storageSubtask.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpicId(), storageEpic.get(subtask.getEpicId())); //обновление статуса эпика

    }

    public void deleteSubtaskById(Integer id) {

        Integer epicId = storageSubtask.get(id).getEpicId();

        storageEpic.get(epicId).deleteId(id);
        storageSubtask.remove(id);
        updateEpicStatus(epicId, storageEpic.get(epicId));
    }


//additional methods

    public ArrayList<Subtask> getListSubtaskOfEpic(Integer id) {
        ArrayList<Subtask> tempArrSubtask = new ArrayList<>();
        for (Integer iterator : storageEpic.get(id).getSubtasksId()) {
            tempArrSubtask.add(storageSubtask.get(iterator));
        }
        return tempArrSubtask;
    }


    public void updateEpicStatus(int id, Epic epic) {
        //updateEpicStatus
        boolean checkStatusNEW = true;
        boolean checkStatusDONE = true;
        if (!epic.getSubtasksId().isEmpty()) {
            for (Integer iterator : epic.getSubtasksId()) {
                if (!Objects.equals(storageSubtask.get(iterator).getStatus(), "NEW")) {
                    checkStatusNEW = false;
                }
                if (!Objects.equals(storageSubtask.get(iterator).getStatus(), "DONE")) {
                    checkStatusDONE = false;
                }
            }
            if (checkStatusNEW) {
                epic.setStatus(0);
            } else if (checkStatusDONE) {
                epic.setStatus(2);
            } else {
                epic.setStatus(1);
            }
        }

    }


}