import java.time.LocalDate;
import java.util.Date;

public class HighPriorityTask extends Task{
    private Boolean ifHighP;
    public HighPriorityTask(String title, String description, Date dueDate, int recurrenceInterval, String status, int priority) {
        super(title, description, dueDate, status, priority);
        this.ifHighP = ifHighP;
    }
    @Override
    public void execute() {
        //execute the execute function if ifHighP boolean is true else
        //Don’t execute.
    }
}
