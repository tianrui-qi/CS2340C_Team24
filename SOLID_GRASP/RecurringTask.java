import java.util.Date;

public class RecurringTask extends Task {
    private int recurringTime;
    private boolean ifRecurring;


    public RecurringTask(String title, String description, Date dueDate, String status, int priority, int recurringTime, boolean ifRecurring) {


        super(title, description, dueDate, status, priority);
        this.recurringTime = recurringTime;
        this.ifRecurring = ifRecurring;
    }


    @Override
    public void execute() {
        System.out.println("Executing recurring task: " + title);
// while recurringTime does not equal to 0, if ifRecurring variable is
        //True, then keep looping until recurringTime goes back to 0.
    }
}
