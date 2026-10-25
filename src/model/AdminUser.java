package model;

public class AdminUser extends User {

    public AdminUser(int userId, String username) {
        super(userId, username, "Admin");
    }

    @Override
    public String getRoleDescription() {
        return "Administrator: Approve bookings, allocate resources, and manage accounts";
    }
}

