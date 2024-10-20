package src;

import java.util.Date;

public class SpecializedTask extends Task {
    private String specializationType;


    public SpecializedTask(String title, String description, Date dueDate, String status, int priority, String specializationType) {


        super(title, description, dueDate, status, priority);
        this.specializationType = specializationType;
    }


    @Override
    public void execute() {
// Add logic for specialized task
    }

    public String getSpecializationType() {
        return "specialization type:" + specializationType;
    }
}
