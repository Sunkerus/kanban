import managers.*;
import tasks.*;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        System.out.println("\nТестирование работы программы\n");
        System.out.println("________________________________________________\n");
        System.out.println("\nCоздайте две задачи, эпик с тремя подзадачами и эпик без подзадач;\n");
        Task task1 = new Task("name", "description");
        Task task2 = new Task("name", "description");
        manager.createTask(task1);
        manager.createTask(task2);

        Epic newEpic1 = new Epic("name", "description");
        Subtask newSubtask1_1 = new Subtask("name", "description");
        Subtask newSubtask1_2 = new Subtask("name", "description");

        manager.createEpic(newEpic1);
        newSubtask1_1.setEpicId(newEpic1.getId()); // привязываем сабтаск к epic
        newSubtask1_2.setEpicId(newEpic1.getId());

        manager.createSubtask(newSubtask1_1);
        manager.createSubtask(newSubtask1_2);

        System.out.println("Создаём один эпик с 1 подзадачей\n");
        Epic newEpic2 = new Epic("name", "description");
        Subtask newSubtask2_1 = new Subtask("name", "description");

        manager.createEpic(newEpic2);
        newSubtask2_1.setEpicId(newEpic2.getId()); // привязываем сабтаск к epic

        manager.createSubtask(newSubtask2_1);

        System.out.println("\nПроверим, создались ли задачи\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());
        System.out.println(manager.getAllTask());

        System.out.println("\nзапросите созданные задачи несколько раз в разном порядке;\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getSubtaskById(4));
        System.out.println(manager.getEpicById(3));


        System.out.println("\nПолучим историю просмотров в порядке обращанеия к записям: старые --> новые \n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getHistory());
        System.out.println("Получим получим кол - во элементов в истории: " + manager.getHistory().size());

        System.out.println("\nУдалим задачу task1, которую вызвали ранее методом manager.getTaskById(1) \n");
        System.out.println("________________________________________________\n");
        manager.deleteTaskById(1);
        System.out.println(manager.getHistory());
        System.out.println("Получим получим кол - во элементов в истории: " + manager.getHistory().size());

        System.out.println("\nУдалим эпик, который вызвали ранее методом manager.getTaskById(1) \n");
        System.out.println("________________________________________________\n");
        manager.deleteEpicById(3);
        System.out.println(manager.getHistory());
        System.out.println("Получим получим кол - во элементов в истории без повторов: " + manager.getHistory().size());

        System.out.println("\nПроверим, осталились удалённые задачи\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());
        System.out.println(manager.getAllTask());


    }

}