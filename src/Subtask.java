import java.util.Arrays;
import java.util.Objects;

public class Subtask extends Task{

    Subtask(String name, String description) {

        super(name, description);

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