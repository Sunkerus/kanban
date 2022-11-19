package managers;

import tasks.*;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import errors.ManagerSaveException;

public class FileBackedTasksManager extends InMemoryTaskManager {

    File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save(){
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {

            writer.write("id,type,name,status,description,epic" + System.lineSeparator());

            for (Map.Entry<Integer, Task> task : super.storageTask.entrySet()) {
                writer.write(toString(task.getValue()) + System.lineSeparator());
            }

            for (Map.Entry<Integer, Epic> task : super.storageEpic.entrySet()) {
                writer.write(toString(task.getValue()) + System.lineSeparator());
            }

            for (Map.Entry<Integer, Subtask> task : super.storageSubtask.entrySet()) {
                writer.write(toString(task.getValue()) + System.lineSeparator());
            }

            writer.write(System.lineSeparator());

            writer.write(historyToString(super.historyManager));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws FileNotFoundException, NullPointerException {
        FileBackedTasksManager tempFileManager = new FileBackedTasksManager(file);
        try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader);) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                if (!Objects.equals(line, "")) {
                    Task tempTask = tempFileManager.fromString(line);
                    String className = tempTask.getClass().getSimpleName();
                    switch (className) {
                        case "Task":
                            tempFileManager.storageTask.put(tempTask.getId(), tempTask);
                            break;
                        case "Epic":
                            tempFileManager.storageEpic.put(tempTask.getId(), (Epic) tempTask);
                            break;
                        case "Subtask":
                            tempFileManager.storageSubtask.put(tempTask.getId(), (Subtask) tempTask);
                            break;
                    }
                } else {
                    line = br.readLine();
                   List<Integer> tempHistoryList =  historyFromString(line);
                    for (Integer tempId: tempHistoryList) {
                        if (tempFileManager.storageTask.containsKey(tempId)) {
                           tempFileManager.getTaskById(tempId);
                        }else if (tempFileManager.storageEpic.containsKey(tempId)) {
                            tempFileManager.getEpicById(tempId);
                        } else if (tempFileManager.storageSubtask.containsKey(tempId)) {
                            tempFileManager.getSubtaskById(tempId);
                        }
                    }

                }
            }
        } catch(IOException  e){
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        return tempFileManager;
    }

        //for write
    private String toString(Task task) {
        String className = task.getClass().getSimpleName();
        switch (className) {
            case "Task":
                return String.join(",", Integer.toString(task.getId()), TypeTask.TASK.name(), task.getName(), task.getStatus().name(), task.getDescription(), "");
            case "Epic":
                return String.join(",", Integer.toString(task.getId()), TypeTask.EPIC.name(), task.getName(), task.getStatus().name(), task.getDescription(), "");
            case "Subtask":
                Subtask tempSubtask = (Subtask) task;
                return String.join(",", Integer.toString(tempSubtask.getId()), TypeTask.SUBTASK.name(), tempSubtask.getName(), task.getStatus().name(), tempSubtask.getDescription(), Integer.toString(tempSubtask.getEpicId()));

            default:
                return null;
        }
    }

    private static String historyToString(HistoryManager manager) {
        ArrayList<String> returnStr = new ArrayList<>();
        for (Task historyId : manager.getHistory()) {
            returnStr.add(Integer.toString(historyId.getId()));
        }
        return String.join(",", returnStr);
    }

    //for read
    private Task fromString(String value) {
        String[] tempStr = value.split(",");

        switch (tempStr[1]) {
            case "TASK":
                Task tempTask = new Task(tempStr[2], tempStr[4]);
                tempTask.setId((Integer.parseInt(tempStr[0])));
                tempTask.setStatus(StatusTask.valueOf(tempStr[3]));
                return tempTask;
            case "EPIC":
                Epic tempEpic = new Epic(tempStr[2], tempStr[4]);
                tempEpic.setId((Integer.parseInt(tempStr[0])));
                tempEpic.setStatus(StatusTask.valueOf(tempStr[3]));
                return tempEpic;
            case "SUBTASK":
                Subtask tempSubtask = new Subtask(tempStr[2], tempStr[4]);
                tempSubtask.setId(Integer.parseInt(tempStr[0]));
                tempSubtask.setStatus(StatusTask.valueOf(tempStr[3]));
                tempSubtask.setEpicId(Integer.parseInt(tempStr[5]));
                super.storageEpic.get(Integer.parseInt(tempStr[5])).setSubtasksId(Integer.parseInt(tempStr[0]));
                return tempSubtask;
        }
        return null;
    }

    private static List<Integer> historyFromString(String value) {
        return Arrays.stream(value.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }


    //методы с наследованием
    @Override
    public int generateId() {
        return super.generateId();
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

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask tempSubtask = super.getSubtaskById(id);
        save();
        return tempSubtask;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    protected void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }


}
