package src;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Project implements ManageTask, ManageTeamMember {


    private BaseTask baseTasks;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private List<Task> tasks;
    private List<TeamMember> teamMembers;
    public Project(String name, String description, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
    }


    public Project(BaseTask baseTasks) {
        this.baseTasks = baseTasks;
    }
    public void executeAllTasks() {
        //execute all the tasks in the task list
    }




    public void setTask(List<BaseTask> newTask) {
        //replacing the task list with the new task list
    }


    public void addTask(Task task) {
        //add the given task onto the task list
    }


    public void removeTask(Task task) {
        // remove the given task from the task list
    }
    public void addTeamMember(TeamMember teamMember) {
        // add the given team member onto the team member list
    }
    public void removeTeamMember(TeamMember teamMember) {
        // remove the given team member from the team member list
    }
    public List<Task> getTasks() { return tasks; }
    public List<TeamMember> getTeamMembers() { return teamMembers; }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BaseTask getBaseTasks() {
        return baseTasks;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
