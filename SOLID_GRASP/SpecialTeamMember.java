public class SpecialTeamMember extends TeamMember implements ManageProject{
    private String specialResponsibility;
    public SpecialTeamMember(String name, String emailAddress, String role, String specialResponsibility) {
        super(name, emailAddress, role);
        this.specialResponsibility = specialResponsibility;
    }
    public String getSpecialResponsibility() {
        //return the special responsibility of the special team member
        return null;
    }
}
