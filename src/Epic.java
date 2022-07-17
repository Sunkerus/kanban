import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{

    ArrayList<Subtask> subtasks = new ArrayList<>();

    Epic(String name, String description, Subtask subtasks) {

        super(name, description);

        this.subtasks.add(subtasks);

    }

    public void setSubtask(String name, String description) {

        subtasks.add(new Subtask(name,description));

    }

    public void setSubtask(Subtask subtask) {

        subtasks.add(subtask);

    }

    public ArrayList<Subtask> getSubtasks() {

        return subtasks;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // проверяем адреса объектов
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic otherEpic = (Epic) obj;
        return Objects.equals(name, otherEpic.name) && // проверяем все поля
                Objects.equals(description, otherEpic.description) && // нужно логическое «и»
                Objects.equals(subtasks, otherEpic.subtasks);
    }
}