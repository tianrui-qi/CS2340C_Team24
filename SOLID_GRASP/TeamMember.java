import java.util.ArrayList;
import java.util.List;

public abstract class TeamMember implements ManageProject, ManageTask {
    private String name;
    private String emailAddress;
    private String role;
    private List<Task> taskList;


    public TeamMember(String name, String emailAddress, String role) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.role = role;
        this.taskList = new ArrayList<Task>();
    }


    @Override
    public void joinProject(Project project) {
        // add the team member onto the project
    }


    @Override
    public void leaveProject(Project project) {
        // remove the team member from the project
    }


    public String getName() {
        //return the name of the team member
        return null;
    }


    String getEmail() {
        //return the email of the team member
        return null;
    }


    public String getRole() {
        //return the role of the team member
        return null;
    }


    public void addTask(Task task) {
        //add task to task list
    }


    public void removeTask(Task task) {
        // remove given task from the task list
    }
}
