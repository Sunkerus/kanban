import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public interface TaskManager {

    int generateId();

    ArrayList<Task> getAllTask();


    void removeAllTask();
    Task getTaskById(int id);

    void createTask(Task task);

    void updateTask(Task task);

    void deleteTaskById(int id);


    //методы для эпиков

    ArrayList<Epic> getAllEpic();


    void removeAllEpic();

    Epic getEpicById(int id);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void deleteEpicById(int id);

    //методя для подзадач

    ArrayList<Subtask> getAllSubtask();


    void removeAllSubtask();

    Subtask getSubtaskById(int id);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtaskById(Integer id);


//additional methods

    ArrayList<Subtask> getListSubtaskOfEpic(Integer id);


    ArrayList<Task> getHistory();
}
