public class Task {
    private String name;
    private String description;
    private int taskId;
    private String statusTask;

    private String[] statusTaskStorage = new String[] {"NEW", "IN_PROGRESS", "DONE"};

    Task(String name, String description, int id){
        this.name = name;
        this.description = description;
        this.id = id;
        this.statusTask = statusTaskStorage[0]; // statusTask = NEW;

    }

    public void setId(int taskId){
        this.taskId = taskId;
    }

    public int getId() {
        return taskId;
    }

    public void setStatus (int taskStatusId){
        statusTask = statusTaskStorage[taskStatusId];
    }

    public String getStatus() { return statusTask; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }




}
