import java.util.ArrayList;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        System.out.println("Создаём один эпик с подзадачей");
        Epic newEpic1 = new Epic("name", "description", new Subtask ("подзадача", "description" ));

    }

}