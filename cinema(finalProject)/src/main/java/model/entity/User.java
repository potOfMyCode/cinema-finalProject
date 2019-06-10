package model.entity;

import model.util.Role;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private List<Ticket> userTickets = new LinkedList<>();

    public static User getGuestInst() {
        User user = new User();
        user.setId(0);
        user.setUsername("guest");
        user.setEmail("guest");
        user.setPassword("guest");
        user.setRole(Role.GUEST);

        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Ticket> getUserTickets() {
        return userTickets;
    }

    public void setUserTickets(List<Ticket> userTickets) {
        this.userTickets = userTickets;
    }

    public boolean notEmpty() {
        return id != 0 &&
                username != null &&
                email != null &&
                password != null &&
                role != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!username.equals(user.username)) return false;
        if (!password.equals(user.password)) return false;
        if (!email.equals(user.email)) return false;
        if (!role.equals(user.role)) return false;
        return userTickets.equals(user.userTickets);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, role, userTickets);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", userTickets=" + userTickets +
                '}';
    }
}
