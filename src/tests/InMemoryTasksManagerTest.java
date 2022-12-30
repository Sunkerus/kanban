package tests;

import managers.*;
import org.junit.jupiter.api.Test;
import managers.InMemoryTaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    public InMemoryTasksManagerTest() {
        super(new InMemoryTaskManager());
    }
    private final TaskManager tasksManager = super.getTasksManager();

}