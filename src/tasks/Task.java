package tasks;

public class Task {
    protected String name;
    protected String description;
    protected Integer id;
    protected StatusTask status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.NEW;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public StatusTask getStatus() {
        return status;
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

        return "Type: task.Task, " +
                "Id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "statusTask = " + status;
    }
}
