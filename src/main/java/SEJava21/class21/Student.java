package SEJava21.class21;

public class Student {
    String[] courses;
    String name;
    public Student(String[] courses, String name) {
        this.courses=courses;
        this.name=name;
    }

    public String[] getCourses() {
        return courses;
    }

    public void setCourses(String[] courses) {
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("courses", courses)
                .append("name", name)
                .toString();
    }
}
