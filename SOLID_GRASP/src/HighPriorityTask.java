package solid_grasp;

import java.util.Date;

public class HighPriorityTask extends Task{
    private boolean ifHighP;
    public HighPriorityTask(String title, String description, Date dueDate, String status, int priority, boolean ifHighP) {
        super(title, description, dueDate, status, priority);
        this.ifHighP = ifHighP;
    }
    @Override
    public void execute() {
        //execute the execute function if ifHighP boolean is true else Don’t execute.

    }

    public boolean getifHighP() {
        return ifHighP;
    }
}
