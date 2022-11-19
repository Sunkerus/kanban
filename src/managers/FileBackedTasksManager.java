package managers;

import tasks.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.nio.file.Path;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import errors.ManagerSaveException;

public class FileBackedTasksManager extends InMemoryTaskManager{

    String filePath;


    public FileBackedTasksManager(String path) {
        filePath = path;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(filePath, StandardCharsets.UTF_8)) {

            for(Map.Entry<Integer, Task> task :super.storageTask.entrySet()) {
                writer.write(toString(task.getValue()) + System.lineSeparator());
            }

            for(Map.Entry<Integer, Epic> task :super.storageEpic.entrySet()) {
                writer.write(toString(task.getValue()) + System.lineSeparator());
            }

            for(Map.Entry<Integer, Subtask> task :super.storageSubtask.entrySet()) {
                writer.write(toString(task.getValue()) + System.lineSeparator());
            }

            writer.write(System.lineSeparator());

            writer.write(historyToString(super.historyManager));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    //метод для сохранения задач в строчку

    public String toString(Task task) {
      String className = task.getClass().getSimpleName();
        switch (className) {
            case "Task":
                return String.join(",", Integer.toString(task.getId()),"TypeTask.TASK",task.getName(),task.getDescription(), ",");
            case "Epic":
                return String.join(",",Integer.toString(task.getId()),"TypeTask.EPIC",task.getName(),task.getDescription(), ",");
            case "Subtask":
                Subtask tempSubtask = (Subtask) task;
                return String.join(",",Integer.toString(tempSubtask.getId()),"TypeTask.SUBTASK",tempSubtask.getName(),tempSubtask.getDescription(), Integer.toString(tempSubtask.getEpicId()));

            default:
                return "qwert";
        }
    }

    //метод создания задачи из строки
    private Task fromString(String value) {
        String[] tempStr = value.split(",");

        switch (tempStr[1]) {
            case "TASK":
                Task tempTask = new Task(tempStr[2], tempStr[4]);
                tempTask.setId((Integer.parseInt(tempStr[0])));
                tempTask.setStatus(StatusTask.valueOf(tempStr[3])); //?
                return tempTask;
            case "EPIC":
                Task tempEpic = new Epic(tempStr[2], tempStr[4]);
                tempEpic.setId((Integer.parseInt(tempStr[0])));
                tempEpic.setStatus(StatusTask.valueOf(tempStr[3])); //?
                return tempEpic;
            case "SUBTASK":
                Subtask tempSubtask = new Subtask(tempStr[2], tempStr[4]);
                tempSubtask.setId((Integer.parseInt(tempStr[0])));
                tempSubtask.setStatus(StatusTask.valueOf(tempStr[3])); //?
                tempSubtask.setEpicId(Integer.parseInt(tempStr[5]));
                return tempSubtask;
        }
        return null;
    }

    //сохранение истории
    private static String historyToString(HistoryManager manager) {
        ArrayList<String> returnStr = new ArrayList<>();
        for (Task historyId: manager.getHistory()) {
            returnStr.add(Integer.toString(historyId.getId()));
        }
        return  String.join(",",returnStr);

    }

    private static List<Integer> historyFromString(String value) {
        return Arrays.stream( value.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }




    @Override
    public int generateId() {
        return super.generateId();
    }

    //методы для задач

    @Override
    public ArrayList<Task> getAllTask() { //получение списка задач
        return super.getAllTask();
    }

    @Override
    public void removeAllTask() { //удаление всех задач
        super.removeAllTask();
        save();

    }

    @Override
    public Task getTaskById(int id) { //получение по id
        Task tempTask = super.getTaskById(id);
        save();
        return tempTask;
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
        super.deleteTaskById(id);
        save();
    }


    //методы для эпиков

    @Override
    public ArrayList<Epic> getAllEpic() { //получение списка эпиков
        return super.getAllEpic();
    }

    @Override
    public void removeAllEpic() { //удаление всех эпиков
        super.removeAllEpic();
        save();                        //удаляем все сабтаски
    }

    @Override
    public Epic getEpicById(int id) { //получение по id
        Epic tempEpic = super.getEpicById(id);
        save();
        return tempEpic;
    }

    @Override
    public void createEpic(Epic epic) { // создание эпика(объект передаётся в качестве параметра)
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) { // обновление эпика
        super.updateEpic(epic);
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
        return super.getAllSubtask();
    }


    @Override
    public void removeAllSubtask() { //удаление всех субтасков
        super.removeAllSubtask();
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) { //получение по id субтасков
        Subtask tempSubtask = super.getSubtaskById(id);
        save();
        return tempSubtask;
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
       return super.getListSubtaskOfEpic(id);
    }


    protected void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }







}
