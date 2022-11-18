package managers;

import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import errors.ManagerSaveException

public class FileBackedTasksManager extends InMemoryTaskManager{

    //change bug


    public FileBackedTasksManager() {

    }


    public void save() {
        try () {

        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }


    //rewriting methods

    static String historyToString(HistoryManager) {
        return null;
    }

    static void loadFromFile(File file) {

    }


    //methods wich i must change
    private String toString(Task task) {
        return null;
    }

    private Task fromString (String value) {
        return null;
    }

    static String historyToString(HistoryManager manager){
        return null;
    }


    static List<Integer> historyFromString(String value) {
        return null;

        try(Files.readString(Path.of("/resources/saveConfig.csv")));
    }


    private final HashMap<Integer, Task> storageTask = new HashMap<>();
    private final HashMap<Integer, Epic> storageEpic = new HashMap<>();
    private final HashMap<Integer, Subtask> storageSubtask = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getAllTask() { //получение списка задач
        save();
        return super.getAllTask();

    }

    @Override
    public void removeAllTask() { //удаление всех задач
        super.removeAllTask();
        save();


    }

    @Override
    public Task getTaskById(int id) { //получение по id
        save();
        return super.getTaskById(id);
    }

    @Override
    public void createTask(Task task) { // создание задачи(объект передаётся в качестве параметра)
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) { // обновление задачи
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) { // проверить на возможные исключения throwable
        deleteTaskById(id);
        save();
    }


    //методы для эпиков

    @Override
    public ArrayList<Epic> getAllEpic() { //получение списка эпиков

        Collection<Epic> values = storageEpic.values();
        return new ArrayList<>(values);
    }

    @Override
    public void removeAllEpic() { //удаление всех эпиков
        super.removeAllEpic();
        save();                        //удаляем все сабтаски
    }

    @Override
    public Epic getEpicById(int id) { //получение по id

        Epic storageEpicTemp = storageEpic.get(id);
        historyManager.add(storageEpicTemp);
        return storageEpicTemp;
    }

    @Override
    public void createEpic(Epic epic) { // создание эпика(объект передаётся в качестве параметра)
            super.createEpic(epic);
            save();
    }

    @Override
    public void updateEpic(Epic epic) { // обновление эпика
        super.updateEpic();
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }


    //методя для подзадач

    @Override
    public ArrayList<Subtask> getAllSubtask() { //получение списка подзадач
        Collection<Subtask> values = storageSubtask.values();
        return new ArrayList<>(values);
    }


    @Override
    public void removeAllSubtask() { //удаление всех субтасков
        super.removeAllSubtask();
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) { //получение по id субтасков

        Subtask storageSubtaskTemp = storageSubtask.get(id);
        historyManager.add(storageSubtaskTemp);
        return storageSubtaskTemp;
    }

    @Override
    public void createSubtask(Subtask subtask) { // создание сабтаска(объект передаётся в качестве параметра)
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) { // обновление подзадачи
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
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
        super.updateEpicStatus(epic);
        save();
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }






}
