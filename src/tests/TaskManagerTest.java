package tests;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    private final T manager;

    public TaskManagerTest(T manager) {
        this.manager = manager;
    }

    public T getTasksManager() {
        return manager;
    }


    @AfterEach
    public void clearData() {
        manager.removeAllTask();
        manager.removeAllEpic();
        manager.removeAllSubtask();
    }

    //for task
    @Test
    public void shouldCreateTaskBeIdenticalWithGetById() { //create and getTaskById
        Task task1 = new Task("task1", "description1");
        manager.createTask(task1);
        final int idTask1 = task1.getId();
        Task createdTask = manager.getTaskById(idTask1);
        assertNotNull(task1, "Созданная задача не была получена");
        assertEquals(createdTask, task1, "Созданная задача не идентична полученной");
    }

    @Test
    public void shouldAllTasksGetIdenticalCreate() {         //getAllTask
        Task task1 = new Task("task1", "description1");
        Task task2 = new Task("task2", "description2");
        manager.createTask(task1);
        manager.createTask(task2);
        List<Task> getTasks = manager.getAllTask();
        final boolean isContains = getTasks.contains(task1) && getTasks.contains(task2);
        assertTrue(isContains, "Задачи не возвращаются");
    }

    @Test
    public void shouldAllTasksRemove() {        //removeAllTask
        Task task1 = new Task("task1", "description1");
        Task task2 = new Task("task2", "description2");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.removeAllTask();
        List<Task> getTasks = manager.getAllTask();
        final boolean isContains = getTasks.contains(task1) && getTasks.contains(task2);
        assertFalse(isContains, "Задачи не удаляются");
    }

    @Test
    public void shouldTaskBeUpdate() {          //updateTask
        Task task1 = new Task("task1", "description1");
        manager.createTask(task1);
        final int idTask1 = task1.getId();
        task1.setDescription("other_description");
        task1.setStatus(StatusTask.IN_PROGRESS);
        manager.updateTask(task1);

        final boolean isUpdate = manager.getTaskById(idTask1).getDescription().equals("other_description") &&
                manager.getTaskById(idTask1).getStatus() == StatusTask.IN_PROGRESS;
        assertTrue(isUpdate, "Задача не обновляется");
    }

    @Test
    public void shouldTaskBeRemoveById() {      //removeTask
        Task task1 = new Task("task1", "description1");
        manager.createTask(task1);
        final int idTask1 = task1.getId();
        manager.deleteTaskById(idTask1);

        final boolean isHere = manager.getAllTask().contains(task1);

        assertFalse(isHere, "Задача не удаляется");
    }

    //forEpic

    @Test
    public void shouldEpicReturnIdenticalWhenGetById() {
        Epic epic1 = new Epic("Epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();
        Task createdEpic = manager.getEpicById(idEpic1);
        assertNotNull(epic1, "Созданная задача не была получена");
        assertEquals(createdEpic, epic1, "Созданная задача не идентична полученной");
    }

    @Test
    public void shouldAllEpicsGetIdenticalCreate() {         //getAllEpic
        Epic epic1 = new Epic("epic1", "description1");
        Epic epic2 = new Epic("epic2", "description2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        List<Epic> getEpic = manager.getAllEpic();
        final boolean isContains = getEpic.contains(epic1) && getEpic.contains(epic2);
        assertTrue(isContains, "Задачи не возвращаются");
    }

    @Test
    public void shouldAllEpicsRemove() {        //removeAllEpic
        Epic epic1 = new Epic("Epic1", "description1");
        Epic epic2 = new Epic("Epic2", "description2");
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.removeAllEpic();
        List<Epic> getEpics = manager.getAllEpic();
        final boolean isContains = getEpics.contains(epic1) && getEpics.contains(epic2);
        assertFalse(isContains, "Задачи не удаляются");
    }

    @Test
    public void shouldEpicBeUpdate() {          //updateTask
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();
        epic1.setDescription("other_description");
        epic1.setStatus(StatusTask.IN_PROGRESS);
        manager.updateTask(epic1);

        final boolean isUpdate = manager.getEpicById(idEpic1).getDescription().equals("other_description") &&
                manager.getEpicById(idEpic1).getStatus() == StatusTask.IN_PROGRESS;
        assertTrue(isUpdate, "Задача не обновляется");
    }

    @Test
    public void shouldEpicBeRemoveById() {      //removeEpic
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();
        manager.deleteEpicById(idEpic1);

        final boolean isHere = manager.getAllEpic().contains(epic1);

        assertFalse(isHere, "Задача не удаляется");
    }

    @Test
    public void shouldEpicChangeStatusWhenSubtaskIs() {
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();
        final StatusTask savedEpicStatus = manager.getEpicById(idEpic1).getStatus();

        assertEquals(savedEpicStatus, StatusTask.NEW, "Статус эпика при создании задаётся не верно");

        Subtask subtask1 = new Subtask("Subtask1", "description1");
        subtask1.setEpicId(idEpic1);
        subtask1.setStatus(StatusTask.DONE);
        manager.createSubtask(subtask1);
        final StatusTask savedEpicStatus1 = manager.getEpicById(idEpic1).getStatus();

        assertEquals(savedEpicStatus1, StatusTask.DONE, "Статус эпика при всех выполненных задачах расчитывается не верно.");
    }

    //for subtask

    @Test
    public void shouldSubtaskHasIdOfEpic() {
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("subtask1", "description1");
        subtask1.setEpicId(idEpic1);
        manager.createSubtask(subtask1);
        final int idSubtask = subtask1.getId();

        final int idEpicFromSubtask = manager.getSubtaskById(idSubtask).getEpicId();
        assertEquals(idEpicFromSubtask, idEpic1, "Установленное id эпика в сабтаске не соотвествует полученному из сабтаска");
    }


    @Test
    public void shouldCreateSubtaskBeIdenticalWithGetById() { //create and getSubtaskById
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("subtask1", "description1");
        subtask1.setEpicId(idEpic1);
        manager.createSubtask(subtask1);

        final int idSubtask1 = subtask1.getId();
        Subtask createdSubtask = manager.getSubtaskById(idSubtask1);
        assertNotNull(subtask1, "Созданная подзадача не была получена");
        assertEquals(createdSubtask, subtask1, "Созданная подзадача не идентична полученной");
    }

    @Test
    public void shouldAllSubtasksGetIdenticalCreate() {         //getAllSubtasks
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("subtask1", "description1");
        Subtask subtask2 = new Subtask("subtask2", "description2");
        subtask1.setEpicId(idEpic1);
        subtask2.setEpicId(idEpic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        List<Subtask> getSubtasks = manager.getAllSubtask();
        final boolean isContains = getSubtasks.contains(subtask1) && getSubtasks.contains(subtask2);
        assertTrue(isContains, "Подзадачи не возвращаются");
    }

    @Test
    public void shouldAllSubtasksRemove() {        //removeAllSubtask
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("subtask1", "description1");
        Subtask subtask2 = new Subtask("subtask2", "description2");
        subtask1.setEpicId(idEpic1);
        subtask2.setEpicId(idEpic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.removeAllSubtask();

        List<Subtask> getSubtasks = manager.getAllSubtask();
        final boolean isContains = getSubtasks.contains(subtask1) && getSubtasks.contains(subtask2);
        assertFalse(isContains, "Подзадачи не удаляются");
    }


    @Test
    public void shouldSubtaskBeUpdate() {          //updateTask
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("subtask1", "description1");
        subtask1.setEpicId(idEpic1);
        manager.createSubtask(subtask1);
        final int idSubtask1 = subtask1.getId();

        subtask1.setStatus(StatusTask.IN_PROGRESS);
        subtask1.setDescription("other_description");
        manager.updateSubtask(subtask1);

        final boolean isUpdate = manager.getSubtaskById(idSubtask1).getDescription().equals("other_description") &&
                manager.getSubtaskById(idSubtask1).getStatus() == StatusTask.IN_PROGRESS;
        assertTrue(isUpdate, "Подзадача не обновляется");
    }


    @Test
    public void shouldSubtaskBeRemoveById() {      //removeTask
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("subtask1", "description1");
        subtask1.setEpicId(idEpic1);
        manager.createSubtask(subtask1);
        final int idSubtask1 = subtask1.getId();
        manager.deleteSubtaskById(idSubtask1);

        final boolean isHere = manager.getAllSubtask().contains(subtask1);

        assertFalse(isHere, "Подзадача не удаляется");
    }

    @Test
    public void shouldGetListSubtaskOfEpicReturnArrayOfSubtask() {
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("subtask1", "description1");
        Subtask subtask2 = new Subtask("subtask2", "description2");
        subtask1.setEpicId(idEpic1);
        subtask2.setEpicId(idEpic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        List<Subtask> listOfSubtasks = manager.getListSubtaskOfEpic(idEpic1);

        final boolean isHere = listOfSubtasks.contains(subtask1) && listOfSubtasks.contains(subtask2);

        assertTrue(isHere, "Не возвращается список задач из эпика");
    }

    @Test
    public void shouldManagerReturnHistoryList() {
        Epic epic1 = new Epic("epic1", "description1");
        manager.createEpic(epic1);
        final int idEpic1 = epic1.getId();

        Subtask subtask1 = new Subtask("subtask1", "description1");
        Subtask subtask2 = new Subtask("subtask2", "description2");
        subtask1.setEpicId(idEpic1);
        subtask2.setEpicId(idEpic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);

        final int subtaskId = subtask1.getId();

        manager.getEpicById(idEpic1);
        manager.getSubtaskById(subtaskId);

       List<Task> historyFromManager = manager.getHistory();
       final boolean isHere = historyFromManager.contains(epic1) && historyFromManager.contains(subtask1);

       assertTrue(isHere,"История не возвращается");

    }
}