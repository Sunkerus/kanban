package tests;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class EpicTest {

    private static TaskManager manager;

    @BeforeAll
    public static void beforeAll() {

        manager = Managers.getInMemoryTaskManager();
    }

    @BeforeEach
    public void removeAllEpic() {
        manager.removeAllEpic();
    }


    @Test
    public void shouldEpicStatusChangeWhenSubtaskListIsEmpty() {


    }

    @Test
    public void shouldEpicStatusChangeWhenAllSubtaskIsNew() {

    }

    @Test
    public void shouldEpicStatusBeDoneWhenAllSubtaskIsDone() {

    }

    @Test
    public void shouldEpicStatusBeInProgressShenSubtaskBeNewAndDone() {

    }

    @Test
    public void shouldEpicStatusBeInProgressWhenAllSubtaskBeInProgress() {

    }

}