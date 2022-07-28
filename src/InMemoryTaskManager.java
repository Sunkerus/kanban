import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int generateId = 0;

    private HashMap<Integer, Task> storageTask = new HashMap<>();
    private HashMap<Integer, Epic> storageEpic = new HashMap<>();
    private HashMap<Integer, Subtask> storageSubtask = new HashMap<>();
    private ArrayList<Task> historyList = new ArrayList<>();

    @Override
    public int generateId() {

        generateId += 1;
        return generateId;
    }

    //методы для задач

    @Override
    public ArrayList<Task> getAllTask() { //получение списка задач

        Collection<Task> values = storageTask.values();
        ArrayList<Task> listOfValues = new ArrayList<>(values);
        return listOfValues;
    }

    @Override
    public void removeAllTask() { //удаление всех задач
        storageTask.clear();
    }

    @Override
    public Task getTaskById(int id) { //получение по id

        historyList.add(storageTask.get(id));
        return storageTask.get(id);
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
    }


    //методы для эпиков

    @Override
    public ArrayList<Epic> getAllEpic() { //получение списка эпиков

        Collection<Epic> values = storageEpic.values();
        ArrayList<Epic> listOfValues = new ArrayList<>(values);
        return listOfValues;
    }

    @Override
    public void removeAllEpic() { //удаление всех эпиков
        storageEpic.clear();
        storageSubtask.clear();         //удаляем все сабтаски
    }

    @Override
    public Epic getEpicById(int id) { //получение по id

        historyList.add(storageEpic.get(id));
        return storageEpic.get(id);
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
        }
        storageEpic.remove(id);
    }


    //методя для подзадач

    @Override
    public ArrayList<Subtask> getAllSubtask() { //получение списка подзадач

        Collection<Subtask> values = storageSubtask.values();
        ArrayList<Subtask> listOfValues = new ArrayList<>(values);
        return listOfValues;
    }


    @Override
    public void removeAllSubtask() { //удаление всех субтасков
        for (Epic iterator : storageEpic.values()) { //обновление статусов всех эпиков
            iterator.setStatus(0);
            iterator.clearAllId();                   //очистка коллекции идентификаторов в каждом эпике
        }
        storageSubtask.clear();
    }

    @Override
    public Subtask getSubtaskById(int id) { //получение по id субтасков

        historyList.add(storageSubtask.get(id));
        return storageSubtask.get(id);
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

    @Override
    public ArrayList<Task> getHistory(){
        return historyList;
    }


    protected void updateEpicStatus(Epic epic) {
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