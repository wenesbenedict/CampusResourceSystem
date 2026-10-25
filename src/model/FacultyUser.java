package model;

public class FacultyUser extends User {

    public FacultyUser(int userId, String username) {
        super(userId, username, "Faculty");
    }

    @Override
    public String getRoleDescription() {
        return "Faculty: Create events and manage resource booking requests";
    }
}

