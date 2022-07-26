import java.util.Arrays;
import java.util.Objects;

public class Subtask extends Task{

    private int epicId;
    Subtask(String name, String description) {

        super(name, description);

    }


    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // проверяем адреса объектов
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Subtask otherSubtask = (Subtask) obj;
        return Objects.equals(name, otherSubtask.name) && //
                Objects.equals(description, otherSubtask.description);//
    }
}