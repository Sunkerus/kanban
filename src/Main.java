import java.util.ArrayList;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        System.out.println("Создаём один эпик с 2 подзадачами");
        Epic newEpic1 = new Epic("name", "description");
        Subtask newSubtask1_1 = new Subtask("name","descritpion");
        Subtask newSubtask1_2 = new Subtask("name","descritpion");

        manager.createEpic(newEpic1);
        newSubtask1_1.setEpicId(newEpic1.getId()); // привязываем сабтаск к epic
        newSubtask1_2.setEpicId(newEpic1.getId());

        manager.createSubtask(newSubtask1_1);
        manager.createSubtask(newSubtask1_2);


        System.out.println("Создаём один эпик с 1 подзадачей");
        Epic newEpic2 = new Epic("name", "description");
        Subtask newSubtask2_1 = new Subtask("name","descritpion");

        manager.createEpic(newEpic1);
        newSubtask2_1.setEpicId(newEpic1.getId()); // привязываем сабтаск к epic

        manager.createSubtask(newSubtask2_1);


        System.out.println(manager.getAllEpic());
        System.out.println(manager.getAllSubtask());



    }

}