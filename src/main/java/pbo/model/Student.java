package pbo.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class Student {
  @Id
  @Column(name = "id", nullable = false, length = 255)
  private String id;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "program", nullable = false, length = 255)
  private String program;

  @ManyToMany(targetEntity = Course.class, cascade = CascadeType.ALL)
  @JoinTable(
    name = "course_student",
    joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(
      name = "course_code",
      referencedColumnName = "code"
    )
  )
  private Set<Course> courses;

  public Student() {
    // empty
  }

  public Student(String id, String name, String program) {
    this.id = id;
    this.name = name;
    this.program = program;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProgram() {
    return program;
  }

  public void setProgram(String program) {
    this.program = program;
  }

  public Set<Course> getCourses() {
    return courses;
  }

  public void setCourses(Set<Course> courses) {
    this.courses = courses;
  }

  @Override
  public String toString() {
    return this.id + "|" + this.name + "|" + this.program;
  }
}
