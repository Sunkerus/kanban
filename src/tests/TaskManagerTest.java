package tests;

import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.StatusTask;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {

    private final T manager;

    public TaskManagerTest(T manager) {
        this.manager = manager;
    }

    public T getTasksManager() {
        return manager;
    }


    //for task
    @Test
    public void shouldCreateTaskBeIdenticalWithGetByIdTask() { //create and getTaskById
        Task task1 = new Task("task1","description1");
        manager.createTask(task1);
        final int idTask1 = task1.getId();
        Task createdTask = manager.getTaskById(idTask1);
        assertNotNull(task1,"Созданная задача не была получена");
        assertEquals(task1,createdTask,"Созданная задача не идентична полученной");
    }

    @Test
    public void shouldAllTasksGetIdenticalCreate(){         //getAllTask
        Task task1 = new Task("task1","description1");
        Task task2 = new Task ("task2","description2");
        manager.createTask(task1);
        manager.createTask(task2);
        List<Task> getTasks =  manager.getAllTask();
        final boolean isContains = getTasks.contains(task1) && getTasks.contains(task2);
        assertTrue(isContains,"Задачи не возвращаются");
    }

    @Test
    public void shouldAllTasksRemove() {        //removeAllTask
        Task task1 = new Task("task1","description1");
        Task task2 = new Task ("task2","description2");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.removeAllTask();
        List<Task> getTasks =  manager.getAllTask();
        final boolean isContains = getTasks.contains(task1) && getTasks.contains(task2);
        assertFalse(isContains,"Задачи не удаляются");
    }

    @Test
    public void shouldTaskBeUpdate() {          //updateTask
        Task task1 = new Task("task1","description1");
        manager.createTask(task1);
        final int idTask1 = task1.getId();
        task1.setDescription("other_description");
        task1.setStatus(StatusTask.IN_PROGRESS);
        manager.updateTask(task1);

        final boolean isUpdate = manager.getTaskById(idTask1).getDescription() == "other_description" &&
                                    manager.getTaskById(idTask1).getStatus() == StatusTask.IN_PROGRESS;
        assertTrue(isUpdate,"Задача не обновляется");
    }

    @Test
    public void shouldTaskBeRemoveById() {      //removeTask
        Task task1 = new Task("task1","description1");
        manager.createTask(task1);
        final int idTask1 = task1.getId();
        manager.deleteTaskById(idTask1);

        final boolean isHere = manager.getAllTask().contains(task1);

        assertFalse(isHere,"Задача не удаляется");
    }



}
