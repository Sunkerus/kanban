package tests;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    private static TaskManager manager;

    @BeforeAll
    public static void beforeAll() {    //запуск перед всеми тестами

        manager = Managers.getInMemoryTaskManager();
    }

    @BeforeEach
    public void removeAllEpic() {   //запуск перед каждым тестом.
        manager.removeAllEpic();
    }


    @Test
    public void shouldEpicStatusNewWhenSubtaskListIsEmpty() { //Пустой список подзадач
        Epic epicTest = new Epic("TestEpic", "desctiptionEpic");
        manager.createEpic(epicTest);
        final int epicTestId = epicTest.getId();

        StatusTask stausEpic = manager.getEpicById(epicTestId).getStatus();

        assertEquals(StatusTask.NEW, stausEpic,"Не верно задан статус эпика при создании");
    }

    @Test
    public void shouldEpicStatusChangeWhenAllSubtaskIsNew() {
        Epic epicTest = new Epic("TestEpic", "desctiptionEpic");
        manager.createEpic(epicTest);
        final int epicTestId = epicTest.getId();

        Subtask subtaskTest1 = new Subtask("TestSubtask1","TestSubtask1");
        Subtask subtaskTest2 = new Subtask("TestSubtask2","TestSubtask2");

        subtaskTest1.setStatus(StatusTask.NEW);
        subtaskTest1.setStatus(StatusTask.NEW);

        subtaskTest1.setEpicId(epicTestId);
        subtaskTest2.setEpicId(epicTestId);

        manager.createSubtask(subtaskTest1);
        manager.createSubtask(subtaskTest2);

        StatusTask statusEpic = manager.getEpicById(epicTestId).getStatus();


        assertEquals(StatusTask.NEW,statusEpic,"Не верно изменяется статус эпика, когда статус всех подзадач NEW");

    }

    @Test
    public void shouldEpicStatusBeDoneWhenAllSubtaskIsDone() {

        Epic epicTest = new Epic("TestEpic", "desctiptionEpic");
        manager.createEpic(epicTest);
        final int epicTestId = epicTest.getId();

        Subtask subtaskTest1 = new Subtask("TestSubtask1","TestSubtask1");
        Subtask subtaskTest2 = new Subtask("TestSubtask2","TestSubtask2");

        subtaskTest1.setStatus(StatusTask.DONE);
        subtaskTest2.setStatus(StatusTask.DONE);

        subtaskTest1.setEpicId(epicTestId);
        subtaskTest2.setEpicId(epicTestId);

        manager.createSubtask(subtaskTest1);
        manager.createSubtask(subtaskTest2);

        StatusTask statusEpic = manager.getEpicById(epicTestId).getStatus();

        assertEquals(StatusTask.DONE,statusEpic,"Не верно изменяется статус эпика, когда статус всех подзадач DONE");
    }

    @Test
    public void shouldEpicStatusBeInProgressWhenSubtaskBeNewAndDone() {
        Epic epicTest = new Epic("TestEpic", "desctiptionEpic");
        manager.createEpic(epicTest);
        final int epicTestId = epicTest.getId();

        Subtask subtaskTest1 = new Subtask("TestSubtask1","TestSubtask1");
        Subtask subtaskTest2 = new Subtask("TestSubtask2","TestSubtask2");

        subtaskTest1.setStatus(StatusTask.DONE);
        subtaskTest2.setStatus(StatusTask.NEW);

        subtaskTest1.setEpicId(epicTestId);
        subtaskTest2.setEpicId(epicTestId);

        manager.createSubtask(subtaskTest1);
        manager.createSubtask(subtaskTest2);

        StatusTask statusEpic = manager.getEpicById(epicTestId).getStatus();

        assertEquals(StatusTask.IN_PROGRESS,statusEpic,"Не верно изменяется статус эпика на IN_PROGRESS, когда статус подзадач DONE и NEW");
    }

    @Test
    public void shouldEpicStatusBeInProgressWhenAllSubtaskBeInProgress() {
        Epic epicTest = new Epic("TestEpic", "desctiptionEpic");
        manager.createEpic(epicTest);
        final int epicTestId = epicTest.getId();

        Subtask subtaskTest1 = new Subtask("TestSubtask1","TestSubtask1");
        Subtask subtaskTest2 = new Subtask("TestSubtask2","TestSubtask2");

        subtaskTest1.setStatus(StatusTask.IN_PROGRESS);
        subtaskTest2.setStatus(StatusTask.IN_PROGRESS);

        subtaskTest1.setEpicId(epicTestId);
        subtaskTest2.setEpicId(epicTestId);

        manager.createSubtask(subtaskTest1);
        manager.createSubtask(subtaskTest2);

        StatusTask statusEpic = manager.getEpicById(epicTestId).getStatus();

        assertEquals(StatusTask.IN_PROGRESS,statusEpic,"Не верно изменяется статус эпика на IN_PROGRESS, когда статус подзадач DONE и NEW");
    }

}