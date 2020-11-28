package tu.cit.examples.producers;

import com.opencsv.bean.CsvBindByName;

public class StudentModel {

    @CsvBindByName
    private int studentid;

    @CsvBindByName
    private String name;

    @CsvBindByName
    private String dept;

    @CsvBindByName
    private String subject;

    @CsvBindByName
    private String marks;

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
