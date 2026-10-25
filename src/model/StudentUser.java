package model;

public class StudentUser extends User {

    public StudentUser(int userId, String username) {
        super(userId, username, "Student");
    }

    @Override
    public String getRoleDescription() {
        return "Student: View approved academic events and schedules";
    }
}

