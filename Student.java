package model;

public class Student {
    private String rollNo;
    private String name;
    private String password;
    private String department;
    private int semester;
    private String minorStream;

    public Student(String rollNo, String name, String password, String department, int semester, String minorStream) {
        this.rollNo = rollNo;
        this.name = name;
        this.password = password;
        this.department = department;
        this.semester = semester;
        this.minorStream = minorStream;
    }

    public String getRollNo() { return rollNo; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getDepartment() { return department; }
    public int getSemester() { return semester; }
    public String getMinorStream() { return minorStream; }
    
    public void setMinorStream(String minorStream) { this.minorStream = minorStream; }
}
