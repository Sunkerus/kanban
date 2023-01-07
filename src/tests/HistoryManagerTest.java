package tests;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

class HistoryManagerTest {

    private TaskManager taskManager;
    private HistoryManager historyManager;
    private Epic epic1;
    private Task task2;
    private Task task3;
    private Subtask subtask4;


    @BeforeEach
    public void managerCreation() {
        taskManager = Managers.getInMemoryTaskManager();
        historyManager = Managers.getDefaultHistory();

        epic1 = new Epic("name", "description");
        task2 = new Task("name", "description");
        task3 = new Task("name", "description");
        subtask4 = new Subtask("name", "description");
        subtask4.setEpicId(1);
        taskManager.createEpic(epic1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createSubtask(subtask4);
    }

    //Пустая история задач.
    //add
    @Test
    public void addTasksWhenHistoryIsEmpty() {

        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(subtask4);

        List<Task> historyList = historyManager.getHistory();
        final int historySize = 4;

        assertEquals(historySize, historyList.size(), "Задачи не добавляются в историю.");

    }

    //getHistory
    @Test
    public void getHistoryWhenHistoryIsEmpty() {
        List<Task> historyList = historyManager.getHistory();
        final int historySize = 0;

        assertEquals(historySize, historyList.size(), "В historyManager находятся задачи не вызывавшиеся ранее.");
    }

    @Test
    public void removeHistoryWhenHistoryIsEmpty() {
        historyManager.remove(1);
        final int historySize = 0;
        List<Task> historyList = historyManager.getHistory();

        assertEquals(historySize, historyList.size(), "В historyManager удаляются несуществующие задачи");


    }

    //Дублирование
    //add
    @Test
    public void shouldHistoryDuplicateWhenAddNewIdenticalTasks() {
        historyManager.add(task2);
        historyManager.add(task2);
        final int historySize = 1;
        List<Task> historyList = historyManager.getHistory();

        assertEquals(historySize, historyList.size(), "При добавлении идентичной задачи происходит дублирование");
    }

    //Удаление из истории: начало, середина, конец.
    @Test
    public void shouldHistoryDeleteCorrectlyFromStart() {
        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(subtask4);

        historyManager.remove(epic1.getId());
        List<Task> historyList = historyManager.getHistory();

        assertFalse(historyList.contains(epic1), "Из начала истории не удаляется задача");
    }

    @Test
    public void shouldHistoryDeleteCorrectlyFromMiddle() {
        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(subtask4);

        historyManager.remove(task2.getId());
        List<Task> historyList = historyManager.getHistory();

        assertFalse(historyList.contains(task2), "Из начала истории не удаляется задача");
    }

    @Test
    public void shouldHistoryDeleteCorrectlyFromTail() {
        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(subtask4);

        historyManager.remove(subtask4.getId());
        List<Task> historyList = historyManager.getHistory();

        assertFalse(historyList.contains(subtask4), "Из начала истории не удаляется задача");
    }


}
