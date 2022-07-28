import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        historyList.add(0,task);
        if (historyList.size() > 10) {
            historyList.remove(10);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

}
