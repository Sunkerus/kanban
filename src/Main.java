import managers.*;
import tasks.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        File saveFilePath = new File(".\\Resources\\saveConfig.csv");

        System.out.println("\nЗагрузка в файл saveConfig.csv");
        System.out.println("=====================================================================");

        TaskManager fileTaskManager = Managers.getFileBackedTaskManager(saveFilePath);

        Epic epic3 = new Epic("name", "description");
        fileTaskManager.createEpic(epic3);
        Task task3 = new Task("name", "description");
        fileTaskManager.createTask(task3);
        fileTaskManager.getEpicById(1);
        fileTaskManager.getTaskById(2);
        Subtask subtask3_1 = new Subtask("name", "description");
        subtask3_1.setEpicId(1);
        fileTaskManager.createSubtask(subtask3_1);
        fileTaskManager.getSubtaskById(3);


        System.out.println(fileTaskManager.getAllEpic());
        System.out.println(fileTaskManager.getAllSubtask());
        System.out.println(fileTaskManager.getAllTask());
        System.out.println("====================История====================");
        System.out.println(fileTaskManager.getHistory());


        File loadFilePath = new File(".\\Resources\\loadConfig.csv");

        System.out.println("\nСчитывание из файла loadConfig.csv");
        System.out.println("=====================================================================");
        TaskManager fileTaskManager1 = FileBackedTasksManager.loadFromFile(loadFilePath);
        System.out.println(fileTaskManager1.getAllEpic());
        System.out.println(fileTaskManager1.getAllSubtask());
        System.out.println(fileTaskManager1.getAllTask());
        System.out.println("====================История====================");
        System.out.println(fileTaskManager1.getHistory());

    }

}