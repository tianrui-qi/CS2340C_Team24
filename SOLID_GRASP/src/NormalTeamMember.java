package src;

public class NormalTeamMember extends TeamMember implements ManageProject {
    private String basicResponsibility;
    public NormalTeamMember(String name, String emailAddress, String role, String basicResponsibility) {
        super(name, emailAddress, role);
        this.basicResponsibility = basicResponsibility;
    }


    public String getBasicResponsibility() {
        //return the basic responsibility of the team member
        return "basic responsibility: " + basicResponsibility;
    }
}
