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
}