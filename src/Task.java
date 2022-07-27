public class Task {
    protected String name;
    protected String description;
    protected String statusTask;
    protected Integer id;

    private String[] statusTaskStorage = new String[]{"NEW", "IN_PROGRESS", "DONE"};

    Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.statusTask = statusTaskStorage[0]; // statusTask = NEW;
    }

    public void setStatus(int taskStatusId) {
        statusTask = statusTaskStorage[taskStatusId];
    }

    public String getStatus() {
        return statusTask;
    }


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


    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {

        return "Type: Task, " +
                "Id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "statusTask = " + statusTask;
    }


}
