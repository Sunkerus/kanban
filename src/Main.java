import managers.*;
import tasks.*;

public class Main {

    public static void main(String[] args) {

        TaskManager manager =  Managers.getDefault();

        System.out.println("Создадим две задачи, эпик с тремя подзадачами и эпик без подзадач\n");
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


        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());

        System.out.println("\nИзменим статус newSubtask2_1 на DONE\n");
        System.out.println("________________________________________________\n");
        newSubtask2_1.setStatus(StatusTask.DONE);
        manager.updateSubtask(newSubtask2_1);
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());


        System.out.println("\nУдалим подзадачу из эпика со статусом DONE newEpic2\n");
        System.out.println("________________________________________________\n");
        manager.deleteSubtaskById(5);
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());


        System.out.println("\nВызовем эпик newEpic2\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getEpicById(4));


        System.out.println("\nПолучим историю просмотров\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getHistory());


        System.out.println("\nУдалим эпик newEpic2\n");
        System.out.println("________________________________________________\n");
        manager.deleteEpicById(4);
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());


        System.out.println("\nПолучим подзадачи из newEpic1\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getListSubtaskOfEpic(1));


        System.out.println("\nУдалим все сабтаски\n");
        System.out.println("________________________________________________\n");
        manager.removeAllSubtask();
        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());


        System.out.println("\nПолучим задачу по id\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getEpicById(1));         //сделаем запрос к эпику 6 раз, далее отобразится в истории
        System.out.println(manager.getEpicById(1));
        System.out.println(manager.getEpicById(1));
        System.out.println(manager.getEpicById(1));
        System.out.println(manager.getEpicById(1));


        System.out.println("\nПолучим историю просмотров\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getHistory());
        System.out.println(manager.getHistory().size());         //проверим кол - во элементов в массиве

        System.out.println("\nПолучим задачу по id\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getEpicById(1));         //сделаем запрос к эпику 6 раз, далее отобразится в истории
        System.out.println(manager.getEpicById(1));         //сделаем 13 запросов и проверим будет ли переполнение массива
        System.out.println(manager.getEpicById(1));         //в итоговом массиве с историем должно быть не больше 10 элементов



        System.out.println("\nПолучим историю просмотров\n");
        System.out.println("________________________________________________\n");
        System.out.println(manager.getHistory());
        System.out.println(manager.getHistory().size());         //проверим кол - во элементов в массиве
    }

}