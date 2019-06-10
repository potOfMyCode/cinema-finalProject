package model.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Day {
    private int id;
    private String name;
    private String shortName;
    private List<Session> sessions = new LinkedList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName.toUpperCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public boolean notEmpty() {
        return id != 0 &&
                name != null &&
                shortName != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day = (Day) o;

        if (id != day.id) return false;
        if (!name.equals(day.name)) return false;
        if (!shortName.equals(day.shortName)) return false;
        return sessions.equals(day.sessions);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shortName, sessions);
    }

    @Override
    public String toString() {
        return "Day{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
               // ", sessions=" + sessions +
                '}';
    }
}
