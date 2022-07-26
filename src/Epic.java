import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{

    private ArrayList<Integer> subtasksId = new ArrayList<>();

    Epic(String name, String description) {

        super(name, description);

    }

   public void addId(Integer id) {
        subtasksId.add(id);
   }

    public void deleteId(Integer id) {
        subtasksId.remove(id); //реализовать функцию поиска id
    }


    public void setSubtasksId(Integer id){
        subtasksId.add(id);
    }


    public ArrayList<Integer> getSubtasksId(){
        return subtasksId;
    }




    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // проверяем адреса объектов
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Epic otherEpic = (Epic) obj;
        return Objects.equals(name, otherEpic.name) && // проверяем все поля
                Objects.equals(description, otherEpic.description) && // нужно логическое «и»
                Objects.equals(subtasksId, otherEpic.subtasksId);
    }
}