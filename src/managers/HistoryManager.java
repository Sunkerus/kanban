package managers;

import tasks.*;

import java.util.List;

public interface HistoryManager {

    void add(Task task); //добавляет и помечает задачи, как просмотренные

    List<Task> getHistory();

    void remove(int id);
}
