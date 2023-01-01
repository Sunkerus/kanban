package managers;

import java.rmi.UnexpectedException;
import java.util.*;

import errors.ManagerSaveException;
import tasks.*;

public class InMemoryTaskManager implements TaskManager {

    private int generateId = 0;

    protected final HashMap<Integer, Task> storageTask = new HashMap<>();
    protected final HashMap<Integer, Epic> storageEpic = new HashMap<>();
    protected final HashMap<Integer, Subtask> storageSubtask = new HashMap<>();

    protected final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public int generateId() {

        generateId += 1;
        return generateId;
    }

    //методы для задач

    @Override
    public ArrayList<Task> getAllTask() { //получение списка задач

        Collection<Task> values = storageTask.values();
        return new ArrayList<>(values);
    }

    @Override
    public void removeAllTask() { //удаление всех задач
        storageTask.clear();

    }

    @Override
    public Task getTaskById(int id) throws NullPointerException { //получение по id
        try {
            Task storageTaskTemp = storageTask.get(id);
            historyManager.add(storageTaskTemp);
            return storageTaskTemp;
        }catch (NullPointerException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createTask(Task task) { // создание задачи(объект передаётся в качестве параметра)

        storageTask.put(generateId(), task);
        task.setId(generateId);
    }

    @Override
    public void updateTask(Task task) { // обновление задачи

        storageTask.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        storageTask.remove(id);
        historyManager.remove(id);
    }


    //методы для эпиков

    @Override
    public ArrayList<Epic> getAllEpic() { //получение списка эпиков

        Collection<Epic> values = storageEpic.values();
        return new ArrayList<>(values);
    }

    @Override
    public void removeAllEpic() { //удаление всех эпиков
        storageEpic.clear();
        storageSubtask.clear();         //удаляем все сабтаски
    }

    @Override
    public Epic getEpicById(int id) throws NullPointerException{ //получение по id
    try{
        Epic storageEpicTemp = storageEpic.get(id);
        historyManager.add(storageEpicTemp);
        return storageEpicTemp;
    }catch (NullPointerException e) {
        throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createEpic(Epic epic) { // создание эпика(объект передаётся в качестве параметра)

        storageEpic.put(generateId(), epic);
        epic.setId(generateId);
    }

    @Override
    public void updateEpic(Epic epic) { // обновление эпика
        storageTask.put(epic.getId(), epic);
    }

    @Override
    public void deleteEpicById(int id) {
        //копируем массив
        ArrayList<Integer> tempSubtaskIdArr = new ArrayList<>(storageEpic.get(id).getSubtasksId());

        for (Integer iterator : tempSubtaskIdArr) {
            deleteSubtaskById(iterator);
            historyManager.remove(iterator);
        }
        storageEpic.remove(id);
        historyManager.remove(id);
    }


    //методя для подзадач

    @Override
    public ArrayList<Subtask> getAllSubtask() { //получение списка подзадач

        Collection<Subtask> values = storageSubtask.values();
        return new ArrayList<>(values);
    }


    @Override
    public void removeAllSubtask() { //удаление всех субтасков
        for (Epic iterator : storageEpic.values()) { //обновление статусов всех эпиков
            iterator.setStatus(StatusTask.NEW);
            iterator.clearAllId();                   //очистка коллекции идентификаторов в каждом эпике
        }
        storageSubtask.clear();
    }

    @Override
    public Subtask getSubtaskById(int id) throws NullPointerException{ //получение по id субтасков
    try{
        Subtask storageSubtaskTemp = storageSubtask.get(id);
        historyManager.add(storageSubtaskTemp);
        return storageSubtaskTemp;
    }catch (NullPointerException e) {
        throw new ManagerSaveException(e.getMessage());
    }
    }

    @Override
    public void createSubtask(Subtask subtask) { // создание сабтаска(объект передаётся в качестве параметра)

        storageSubtask.put(generateId(), subtask);      //добавление подзадачи в MAP для подзадач  в эпике
        subtask.setId(generateId); //присвоение id
        storageEpic.get(subtask.getEpicId()).addId(subtask.getId()); //добавление идентификатора в список подзадач эпика
        updateEpicStatus(storageEpic.get(subtask.getEpicId()));   //обновление статуса эпика
    }

    @Override
    public void updateSubtask(Subtask subtask) { // обновление подзадачи

        storageSubtask.put(subtask.getId(), subtask);
        updateEpicStatus(storageEpic.get(subtask.getEpicId())); //обновление статуса эпика
    }

    @Override
    public void deleteSubtaskById(Integer id) {

        Integer epicId = storageSubtask.get(id).getEpicId();

        storageEpic.get(epicId).deleteId(id);
        storageSubtask.remove(id);
        updateEpicStatus(storageEpic.get(epicId));
        historyManager.remove(id);
    }


//additional methods

    @Override
    public ArrayList<Subtask> getListSubtaskOfEpic(Integer id) {
        ArrayList<Subtask> tempArrSubtask = new ArrayList<>();
        for (Integer iterator : storageEpic.get(id).getSubtasksId()) {
            tempArrSubtask.add(storageSubtask.get(iterator));
        }
        return tempArrSubtask;
    }


    protected void updateEpicStatus(Epic epic) {
        boolean checkStatusNEW = true;
        boolean checkStatusDONE = true;
        if (!epic.getSubtasksId().isEmpty()) {
            for (Integer iterator : epic.getSubtasksId()) {
                if (!Objects.equals(storageSubtask.get(iterator).getStatus(), StatusTask.NEW)) {
                    checkStatusNEW = false;
                }
                if (!Objects.equals(storageSubtask.get(iterator).getStatus(), StatusTask.DONE)) {
                    checkStatusDONE = false;
                }
            }
        }
        if (checkStatusNEW) {
            epic.setStatus(StatusTask.NEW);
        } else if (checkStatusDONE) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }
}