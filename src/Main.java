import managers.*;
import tasks.*;

import java.io.File;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        System.out.println("\nТестирование работы программы\n");
        System.out.println("=====================================================================\n");
        System.out.println("\n-Cоздайте две задачи, эпик с тремя подзадачами и эпик без подзадач;\n");

        Task task1 = new Task("name", "description");
        Task task2 = new Task("name", "description");
        manager.createTask(task1);
        manager.createTask(task2);

        Epic newEpic1 = new Epic("name", "description");
        Subtask newSubtask1_1 = new Subtask("name", "description");
        Subtask newSubtask1_2 = new Subtask("name", "description");
        Subtask newSubtask1_3 = new Subtask("name", "description");

        manager.createEpic(newEpic1);
        newSubtask1_1.setEpicId(newEpic1.getId());
        newSubtask1_2.setEpicId(newEpic1.getId());
        newSubtask1_3.setEpicId(newEpic1.getId());

        manager.createSubtask(newSubtask1_1);
        manager.createSubtask(newSubtask1_2);
        manager.createSubtask(newSubtask1_3);

        System.out.println("-Создайте один эпик с 1 подзадачей\n");
        Epic newEpic2 = new Epic("name", "description");
        Subtask newSubtask2_1 = new Subtask("name", "description");

        manager.createEpic(newEpic2);
        newSubtask2_1.setEpicId(newEpic2.getId()); // привязываем сабтаск к epic
        manager.createSubtask(newSubtask2_1);


        System.out.println("\nПроверим, создались ли задачи");
        System.out.println("=====================================================================");
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());
        System.out.println(manager.getAllTask());

        System.out.println("\n-Зпросите созданные задачи несколько раз в разном порядке;");
        System.out.println("=====================================================================");
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(3));
        System.out.println();

        System.out.println("====================История====================");
        System.out.println(manager.getHistory());
        System.out.println("Получим получим кол - во элементов в истории: " + manager.getHistory().size());
        System.out.println("=====================================================================\n");
        System.out.println();

        System.out.println("Запросим сабтаск");
        System.out.println("=====================================================================\n");
        System.out.println(manager.getSubtaskById(4));
        System.out.println();

        System.out.println("====================История====================");
        System.out.println(manager.getHistory());
        System.out.println("Получим получим кол - во элементов в истории: " + manager.getHistory().size());
        System.out.println("=====================================================================\n");
        System.out.println();

        System.out.println("Запросим эпик и сабтаск ещё раз");
        System.out.println("=====================================================================\n");
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getSubtaskById(4));

        System.out.println("====================История====================");
        System.out.println(manager.getHistory());
        System.out.println("Получим получим кол - во элементов в истории: " + manager.getHistory().size());
        System.out.println("=====================================================================\n");

        System.out.println("\nУдалим задачу task1, которую вызвали ранее методом manager.getTaskById(1)");
        System.out.println("=====================================================================\n");
        manager.deleteTaskById(1);

        System.out.println("====================История====================");
        System.out.println(manager.getHistory());
        System.out.println("Получим получим кол - во элементов в истории: " + manager.getHistory().size());
        System.out.println("=====================================================================\n");

        System.out.println("\nУдалим эпик, который вызвали ранее методом manager.getEpicById(3)");
        System.out.println("=====================================================================\n");
        manager.deleteEpicById(3);

        System.out.println("====================История====================");
        System.out.println(manager.getHistory());
        System.out.println("Получим получим кол - во элементов в истории: " + manager.getHistory().size());
        System.out.println("=====================================================================\n");


        File saveFilePath = new File(".\\Resources\\saveConfig.csv");

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


        System.out.println("\nПроверим оставшиеся задачи");
        System.out.println("=====================================================================\n");
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());
        System.out.println(manager.getAllTask());


    }

}