package managers;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static java.time.Month.*;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import java.time.LocalDateTime;
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

            writer.write("id,type,name,status,description,startTime,duration,endTime,epic" + System.lineSeparator());

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
                            tempFileManager.checkTheTaskCompletionTime(tempTask);
                            break;
                        case "Epic":
                            tempFileManager.storageEpic.put(tempTask.getId(), (Epic) tempTask);
                            break;
                        case "Subtask":
                            Subtask tempSubtask = (Subtask) tempTask;
                            tempFileManager.storageSubtask.put(tempSubtask.getId(), tempSubtask);
                            if (tempFileManager.storageEpic.containsKey(tempSubtask.getEpicId())) {
                                tempFileManager.updateEpicTime(tempFileManager.storageEpic.get(tempSubtask.getEpicId()));
                            }
                            tempFileManager.checkTheTaskCompletionTime(tempTask);
                            break;
                        default:
                            throw new ManagerSaveException("???????????????????? ?? ?????????????? ???? ?????????????? ?? ??????????");
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
                throw new ManagerSaveException("???????????? ?? ?????????? ???? ??????????????");
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
                return String.join(",",
                        Integer.toString(task.getId()),
                        TypeTask.TASK.name(),
                        task.getName(),
                        task.getStatus().name(),
                        task.getDescription(),
                        (task.getStartTime() != null) ? task.getStartTime().format(ISO_LOCAL_DATE_TIME) : "TimeNotSelected",
                        Long.toString(task.getDuration()),
                        (task.getEndTime() != null) ? task.getEndTime().format(ISO_LOCAL_DATE_TIME) : "TimeNotSelected",
                        "");
            case "Epic":
                Epic tempEpic = (Epic) task;
                return String.join(",",
                        Integer.toString(tempEpic.getId()),
                        TypeTask.EPIC.name(),
                        tempEpic.getName(),
                        tempEpic.getStatus().name(),
                        tempEpic.getDescription(),
                        (tempEpic.getStartTime() != null && tempEpic.getSubtasksId().isEmpty()) ? tempEpic.getStartTime().format(ISO_LOCAL_DATE_TIME) : "TimeNotSelected",
                        Long.toString(tempEpic.getDuration()),
                        (tempEpic.getEndTime() != null && tempEpic.getSubtasksId().isEmpty()) ? tempEpic.getEndTime().format(ISO_LOCAL_DATE_TIME) : "TimeNotSelected",
                        "");
            case "Subtask":
                Subtask tempSubtask = (Subtask) task;
                return String.join(",",
                        Integer.toString(tempSubtask.getId()),
                        TypeTask.SUBTASK.name(),
                        tempSubtask.getName(),
                        tempSubtask.getStatus().name(),
                        tempSubtask.getDescription(),
                        (tempSubtask.getStartTime() != null) ? tempSubtask.getStartTime().format(ISO_LOCAL_DATE_TIME) : "TimeNotSelected",
                        Long.toString(task.getDuration()),
                        (tempSubtask.getEndTime() != null) ? tempSubtask.getEndTime().format(ISO_LOCAL_DATE_TIME) : "TimeNotSelected",
                        Integer.toString(tempSubtask.getEpicId()));
            default:
                throw new ManagerSaveException("???????????? ???????????????????????????? ?? ????????????");
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
        if ((tempStr.length == 8) || (tempStr.length == 9)) {
            switch (tempStr[1]) {
                case "TASK":
                    Task tempTask = new Task(tempStr[2], tempStr[4]);
                    tempTask.setId((Integer.parseInt(tempStr[0])));
                    tempTask.setStatus(StatusTask.valueOf(tempStr[3]));
                    tempTask.setStartTime((tempStr[5].equals("TimeNotSelected")) ? null : LocalDateTime.parse(tempStr[5]));
                    tempTask.setDuration(Long.parseLong(tempStr[6]));
                    return tempTask;
                case "EPIC":
                    Epic tempEpic = new Epic(tempStr[2], tempStr[4]);
                    tempEpic.setId((Integer.parseInt(tempStr[0])));
                    tempEpic.setStatus(StatusTask.valueOf(tempStr[3]));
                    tempEpic.setStartTime((tempStr[5].equals("TimeNotSelected")) ? null : LocalDateTime.parse(tempStr[5]));
                    tempEpic.setDuration(Long.parseLong(tempStr[6]));
                    tempEpic.setEndTime((tempStr[5].equals("TimeNotSelected")) ? null : LocalDateTime.parse(tempStr[5]));
                    return tempEpic;
                case "SUBTASK":
                    Subtask tempSubtask = new Subtask(tempStr[2], tempStr[4]);
                    tempSubtask.setId(Integer.parseInt(tempStr[0]));
                    tempSubtask.setStatus(StatusTask.valueOf(tempStr[3]));
                    tempSubtask.setEpicId(Integer.parseInt(tempStr[8]));
                    tempSubtask.setStartTime((tempStr[5].equals("TimeNotSelected")) ? null : LocalDateTime.parse(tempStr[5]));
                    tempSubtask.setDuration(Long.parseLong(tempStr[6]));
                    super.storageEpic.get(Integer.parseInt(tempStr[8])).setSubtasksId(Integer.parseInt(tempStr[0]));
                    return tempSubtask;
            }
        }
        return null;
    }

    private static List<Integer> historyFromString(String value) throws ManagerSaveException {
        try {
            return Arrays.stream(value.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        } catch (NullPointerException e) {
            throw new ManagerSaveException("???? ?????????????? ?????????????????? ?????????????? ??????????");
        }
    }


    //???????????? ?? ??????????????????????????
    @Override
    public void removeAllTask() { //???????????????? ???????? ??????????
        super.removeAllTask();
        save();
    }

    @Override
    public Task getTaskById(int id) { //?????????????????? ???? id
        Task tempTask = super.getTaskById(id);
        save();
        return tempTask;
    }

    @Override
    public void createTask(Task task) { // ???????????????? ????????????(???????????? ???????????????????? ?? ???????????????? ??????????????????)
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) { // ???????????????????? ????????????
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) { // ?????????????????? ???? ?????????????????? ???????????????????? throwable
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void removeAllEpic() { //???????????????? ???????? ????????????
        super.removeAllEpic();
        save();                        //?????????????? ?????? ????????????????
    }

    @Override
    public Epic getEpicById(int id) { //?????????????????? ???? id
        Epic tempEpic = super.getEpicById(id);
        save();
        return tempEpic;
    }

    @Override
    public void createEpic(Epic epic) { // ???????????????? ??????????(???????????? ???????????????????? ?? ???????????????? ??????????????????)
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) { // ???????????????????? ??????????
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

        System.out.println("\n???????????????? ?? ???????? saveConfig.csv");
        System.out.println("=====================================================================");

        TaskManager fileTaskManager = Managers.getDefault(saveFilePath);

        Epic epic3 = new Epic("name", "description");

        fileTaskManager.createEpic(epic3);
        Task task3 = new Task("name", "description");
        task3.setStartTime(LocalDateTime.of(2020, FEBRUARY, 15, 10, 0));
        task3.setDuration(50);
        fileTaskManager.createTask(task3);
        fileTaskManager.getEpicById(1);
        fileTaskManager.getTaskById(2);
        Subtask subtask3_1 = new Subtask("name", "description");
        subtask3_1.setStartTime(LocalDateTime.of(1970, FEBRUARY, 20, 10, 10));
        subtask3_1.setEpicId(1);
        subtask3_1.setDuration(10);
        fileTaskManager.createSubtask(subtask3_1);
        fileTaskManager.getSubtaskById(3);

        Subtask subtask3_2 = new Subtask("name", "description");
        subtask3_2.setStartTime(LocalDateTime.of(1970, FEBRUARY, 21, 10, 10));
        subtask3_2.setEpicId(1);
        subtask3_2.setDuration(20);
        fileTaskManager.createSubtask(subtask3_2);

        task3.setStartTime(LocalDateTime.of(2022, FEBRUARY, 21, 10, 10));
        fileTaskManager.getAllTask();
        fileTaskManager.updateTask(task3);

        System.out.println(fileTaskManager.getAllEpic());
        System.out.println(fileTaskManager.getAllSubtask());
        System.out.println(fileTaskManager.getAllTask());
        System.out.println("====================??????????????====================");
        System.out.println(fileTaskManager.getHistory());

        fileTaskManager.getTaskById(2).setStatus(StatusTask.IN_PROGRESS);
        System.out.println("\n?????????????? ???????????? ????????????");
        System.out.println(fileTaskManager.getTaskById(2));

        System.out.println("\n?????????????????? GetPrTask:");
        System.out.println(fileTaskManager.getPrioritizedTasks());

        Epic epic4 = new Epic("name", "description");

        fileTaskManager.createEpic(epic4);
        Subtask subtask4_1 = new Subtask("name", "description");
        subtask4_1.setEpicId(5);
        subtask4_1.setStartTime(LocalDateTime.of(2022, FEBRUARY, 1, 20, 0));
        subtask4_1.setDuration(30);
        fileTaskManager.createSubtask(subtask4_1);
        System.out.println("\n???????????????? ?????????? ?? ?????????????????????? id = 5");
        System.out.println(fileTaskManager.getEpicById(5));

        System.out.println("\n?????????????????? GetPrTask:");
        System.out.println(fileTaskManager.getPrioritizedTasks());

        fileTaskManager.getEpicById(5);
        System.out.println("\n???????????? ???????? id 1");
        fileTaskManager.deleteEpicById(1);

        System.out.println("\n?????????????????? GetPrTask:");
        System.out.println(fileTaskManager.getPrioritizedTasks());

        File loadFilePath = new File(".\\Resources\\saveConfig.csv");

        System.out.println("\n???????????????????? ???? ?????????? saveConfig.csv");
        System.out.println("=====================================================================");
        TaskManager fileTaskManager1 = FileBackedTasksManager.loadFromFile(loadFilePath);
        System.out.println(fileTaskManager1.getAllEpic());
        System.out.println(fileTaskManager1.getAllSubtask());
        System.out.println(fileTaskManager1.getAllTask());
        System.out.println("====================??????????????====================");
        System.out.println(fileTaskManager1.getHistory());

        System.out.println("\n?????????????? Epic id = 5:");
        System.out.println(fileTaskManager1.getEpicById(5));

        System.out.println("\n?????????????????? GetPrTask:");
        System.out.println(fileTaskManager1.getPrioritizedTasks());
    }

}
