package managers;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import static java.time.Month.*;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import errors.ManagerSaveException;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public void save() throws ManagerSaveException {
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {

            writer.write("id,type,name,status,description,time,epic" + System.lineSeparator());

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
        } catch (IOException | NullPointerException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
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
                        default:
                            throw new ManagerSaveException("Информация о задачах не найдена в файле");
                    }
                } else {
                    line = br.readLine();
                    List<Integer> tempHistoryList = historyFromString(line);
                    for (Integer tempId : tempHistoryList) {
                        if (tempFileManager.storageTask.containsKey(tempId)) {
                            tempFileManager.getTaskById(tempId);
                        } else if (tempFileManager.storageEpic.containsKey(tempId)) {
                            tempFileManager.getEpicById(tempId);
                        } else if (tempFileManager.storageSubtask.containsKey(tempId)) {
                            tempFileManager.getSubtaskById(tempId);
                        }
                    }
                }
            }
            if (tempFileManager.storageTask.isEmpty() && tempFileManager.storageEpic.isEmpty() && tempFileManager.storageSubtask.isEmpty()) {
                throw new ManagerSaveException("Задачи в файле не найдены");
            }
        } catch (IOException | NullPointerException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return tempFileManager;
    }

    //for write
    private String toString(Task task) throws ManagerSaveException {
        String className = task.getClass().getSimpleName();
        switch (className) {
            case "Task":
                return String.join(",", Integer.toString(task.getId()), TypeTask.TASK.name(), task.getName(), task.getStatus().name(), task.getDescription(), task.getStartTime().format(ISO_LOCAL_DATE_TIME), "");
            case "Epic":
                return String.join(",", Integer.toString(task.getId()), TypeTask.EPIC.name(), task.getName(), task.getStatus().name(), task.getDescription(), task.getStartTime().format(ISO_LOCAL_DATE_TIME) ,"");
            case "Subtask":
                Subtask tempSubtask = (Subtask) task;
                return String.join(",", Integer.toString(tempSubtask.getId()), TypeTask.SUBTASK.name(), tempSubtask.getName(), task.getStatus().name(), tempSubtask.getDescription(), task.getStartTime().format(ISO_LOCAL_DATE_TIME), Integer.toString(tempSubtask.getEpicId()));
            default:
                throw new ManagerSaveException("Ошибка преобразование в строку");
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
        if ((tempStr.length == 6) || (tempStr.length == 7)) {
            switch (tempStr[1]) {
                case "TASK":
                    Task tempTask = new Task(tempStr[2], tempStr[4]);
                    tempTask.setId((Integer.parseInt(tempStr[0])));
                    tempTask.setStatus(StatusTask.valueOf(tempStr[3]));
                    tempTask.setStartTime(LocalDateTime.parse(tempStr[5]));
                    return tempTask;
                case "EPIC":
                    Epic tempEpic = new Epic(tempStr[2], tempStr[4]);
                    tempEpic.setId((Integer.parseInt(tempStr[0])));
                    tempEpic.setStatus(StatusTask.valueOf(tempStr[3]));
                    tempEpic.setStartTime(LocalDateTime.parse(tempStr[5]));
                    return tempEpic;
                case "SUBTASK":
                    Subtask tempSubtask = new Subtask(tempStr[2], tempStr[4]);
                    tempSubtask.setId(Integer.parseInt(tempStr[0]));
                    tempSubtask.setStatus(StatusTask.valueOf(tempStr[3]));
                    tempSubtask.setEpicId(Integer.parseInt(tempStr[6]));
                    tempSubtask.setStartTime(LocalDateTime.parse(tempStr[5]));
                    super.storageEpic.get(Integer.parseInt(tempStr[6])).setSubtasksId(Integer.parseInt(tempStr[0]));
                    return tempSubtask;
            }
        }
        return null;
    }

    private static List<Integer> historyFromString(String value) throws ManagerSaveException{
        try {
            return Arrays.stream(value.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        }catch(NullPointerException e){
            throw new ManagerSaveException("Не удаётся прочитать историю задач");
        }
    }


    //методы с наследованием
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

    public static void main(String[] args) {

        File saveFilePath = new File(".\\Resources\\saveConfig.csv");

        System.out.println("\nЗагрузка в файл saveConfig.csv");
        System.out.println("=====================================================================");

        TaskManager fileTaskManager = Managers.getDefault(saveFilePath);

        Epic epic3 = new Epic("name", "description");
        epic3.setStartTime(LocalDateTime.of(2222, FEBRUARY, 2, 10, 10));
        epic3.setDuration(30);
        fileTaskManager.createEpic(epic3);
        Task task3 = new Task("name", "description");
        task3.setStartTime(LocalDateTime.of(2222, FEBRUARY, 2, 22, 22));
        task3.setDuration(40);
        fileTaskManager.createTask(task3);
        fileTaskManager.getEpicById(1);
        fileTaskManager.getTaskById(2);
        Subtask subtask3_1 = new Subtask("name", "description");
        subtask3_1.setStartTime(LocalDateTime.of(2222, FEBRUARY, 2, 22, 22));
        subtask3_1.setEpicId(1);
        subtask3_1.setDuration(10);
        fileTaskManager.createSubtask(subtask3_1);
        fileTaskManager.getSubtaskById(3);

        System.out.println(fileTaskManager.getAllEpic());
        System.out.println(fileTaskManager.getAllSubtask());
        System.out.println(fileTaskManager.getAllTask());
        System.out.println("====================История====================");
        System.out.println(fileTaskManager.getHistory());

        fileTaskManager.getTaskById(2).setStatus(StatusTask.IN_PROGRESS);
        System.out.println("\nИзменим статус задачи");
        System.out.println(fileTaskManager.getTaskById(2));



        File loadFilePath = new File(".\\Resources\\saveConfig.csv");

        System.out.println("\nСчитывание из файла saveConfig.csv");
        System.out.println("=====================================================================");
        TaskManager fileTaskManager1 = FileBackedTasksManager.loadFromFile(loadFilePath);
        System.out.println(fileTaskManager1.getAllEpic());
        System.out.println(fileTaskManager1.getAllSubtask());
        System.out.println(fileTaskManager1.getAllTask());
        System.out.println("====================История====================");
        System.out.println(fileTaskManager1.getHistory());

    }

}
