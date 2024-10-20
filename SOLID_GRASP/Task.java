import java.util.Date;

public abstract class Task implements BaseTask {
    protected String title;
    protected String description;
    protected Date dueDate;
    protected String status;
    protected int priority;


    public Task (String title, String description, Date dueDate, String status, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
    }
// Getters and setters // ...
}
