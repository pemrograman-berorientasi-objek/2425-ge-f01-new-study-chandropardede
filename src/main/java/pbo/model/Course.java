package pbo.model;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {
  @Id
  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "semester", nullable = false)
  private Integer semester;

  @Column(name = "credit", nullable = false)
  private Integer credit;

  @ManyToMany(mappedBy = "courses", cascade = CascadeType.ALL)
  private Set<Student> students;

  public Course() {
    // empty
  }

  public Course(String code, String name, Integer semester, Integer credit) {
    this.code = code;
    this.name = name;
    this.semester = semester;
    this.credit = credit;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSemester() {
    return semester;
  }

  public void setSemester(Integer semester) {
    this.semester = semester;
  }

  public Integer getCredit() {
    return credit;
  }

  public void setCredit(Integer credit) {
    this.credit = credit;
  }

  public Set<Student> getStudents() {
    return students;
  }

  public void setStudents(Set<Student> students) {
    this.students = students;
  }

  @Override
  public String toString() {
    return (
      this.code + "|" + this.name + "|" + this.semester + "|" + this.credit
    );
  }
}
