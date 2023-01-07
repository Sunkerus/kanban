package managers;

import tasks.*;

import java.util.List;
import java.util.Set;


public interface TaskManager {

    int generateId();

    List<Task> getAllTask();


    void removeAllTask();

    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);


    //методы для эпиков

    List<Epic> getAllEpic();


    void removeAllEpic();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    //методя для подзадач

    List<Subtask> getAllSubtask();


    void removeAllSubtask();

    Subtask getSubtaskById(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(Integer id);


//additional methods

    List<Subtask> getListSubtaskOfEpic(Integer id);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}