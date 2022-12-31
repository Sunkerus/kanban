package tests;

import managers.*;
import org.junit.jupiter.api.Test;
import managers.InMemoryTaskManager;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    public InMemoryTasksManagerTest() {
        super((InMemoryTaskManager) Managers.getInMemoryTaskManager());
    }

    //все тесты, для методов, использованных в InMemoryTaskManager, написаны в TaskManager Test
}