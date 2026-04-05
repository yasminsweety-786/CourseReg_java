package model;

import java.io.Serializable;

public class Course implements Serializable {
    public int id;
    public String name;
    public int semester;
    public int credits;

    public Course(int id, String name, int semester, int credits) {
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.credits = credits;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getSemester() { return semester; }
    public int getCredits() { return credits; }

    @Override
    public String toString() {
        return id + " - " + name + " (Sem: " + semester + ", " + credits + " cr)";
    }
}