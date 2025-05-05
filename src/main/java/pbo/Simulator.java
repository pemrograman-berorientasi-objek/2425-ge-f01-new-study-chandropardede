package pbo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import pbo.model.Course;
import pbo.model.Student;

public class Simulator {
  private static final String INPUT_SEPARATOR = "#";

  private final EntityManagerFactory factory;

  private final EntityManager entityManager;

  public Simulator(String persistenceUnitName) {
    this.factory = Persistence.createEntityManagerFactory(persistenceUnitName);

    this.entityManager = factory.createEntityManager();
  }

  protected EntityTransaction getTransaction() {
    return this.entityManager.getTransaction();
  }

  protected void persist(Object object) {
    this.getTransaction().begin();

    this.entityManager.persist(object);

    this.entityManager.flush();

    this.getTransaction().commit();
  }

  protected void merge(Object object) {
    this.getTransaction().begin();

    this.entityManager.merge(object);

    this.entityManager.flush();

    this.getTransaction().commit();
  }

  protected void addCourse(Course course) {
    this.persist(course);
  }

  protected void addStudent(Student student) {
    this.persist(student);
  }

  protected void showAllCourses() {
    String jpql = "SELECT c FROM Course c ORDER BY c.semester, c.code";

    TypedQuery<Course> query = this.entityManager.createQuery(jpql, Course.class);

    List<Course> courses = query.getResultList();

    for (Course c : courses) {
      System.out.println(c);
    }
  }

  protected void showAllCoursesEnrolledBy(Student student) {
    String jpql = "SELECT c FROM Course c JOIN c.students s WHERE s.id = :student_id ORDER BY c.semester, c.code";

    TypedQuery<Course> query = this.entityManager.createQuery(jpql, Course.class);

    query.setParameter("student_id", student.getId());

    List<Course> courses = query.getResultList();

    for (Course c : courses) {
      System.out.println(c);
    }
  }

  protected void showAllStudents() {
    String jpql = "SELECT s FROM Student s ORDER BY s.id";

    TypedQuery<Student> query = this.entityManager.createQuery(jpql, Student.class);

    List<Student> students = query.getResultList();

    for (Student s : students) {
      System.out.println(s);
    }
  }

  protected void showStudent(List<String> segments)
      throws EntityNotFoundException {
    String id = segments.remove(0);

    Student student = this.entityManager.find(Student.class, id);

    if (student == null) {
      throw new EntityNotFoundException();
    }

    System.out.println(student);

    this.showAllCoursesEnrolledBy(student);
  }

  private void cleanTables() {
    String deleteCoursesJpql = "DELETE FROM Course c";

    String deleteStudentsJpql = "DELETE FROM Student s";

    this.getTransaction().begin();

    this.entityManager.createQuery(deleteCoursesJpql).executeUpdate();

    this.entityManager.createQuery(deleteStudentsJpql).executeUpdate();

    this.entityManager.flush();

    this.getTransaction().commit();
  }

  public void run() {
    Scanner scanner = new Scanner(System.in);

    this.cleanTables();

    while (true) {
      String line = scanner.nextLine();

      if (line.equals("---")) {
        break;
      }

      List<String> segments = new ArrayList<String>();

      Collections.addAll(segments, line.trim().split(INPUT_SEPARATOR));

      this.followUp(segments);
    }

    scanner.close();
    this.entityManager.close();
  }

  protected void createStudent(List<String> segments)
      throws EntityAlreadyExistException {
    String id = segments.remove(0);

    Student student = this.entityManager.find(Student.class, id);

    if (student != null) {
      throw new EntityAlreadyExistException();
    }

    student = new Student(id, segments.remove(0), segments.remove(0));

    this.persist(student);
  }

  protected void createCourse(List<String> segments)
      throws EntityAlreadyExistException {
    String code = segments.remove(0);

    Course course = this.entityManager.find(Course.class, code);

    if (course != null) {
      throw new EntityAlreadyExistException();
    }

    course = new Course(
        code,
        segments.remove(0),
        Integer.parseInt(segments.remove(0)),
        Integer.parseInt(segments.remove(0)));

    this.persist(course);
  }

  protected void enroll(List<String> segments)
      throws EntityNotFoundException {
    String id = segments.remove(0);

    String code = segments.remove(0);

    Student student = this.entityManager.find(Student.class, id);

    if (student == null) {
      throw new EntityNotFoundException();
    }

    Course course = this.entityManager.find(Course.class, code);

    if (course == null) {
      throw new EntityNotFoundException();
    }

    if (student.getCourses().contains(course)) {
      return;
    }

    student.getCourses().add(course);

    this.merge(course);
  }

  protected void followUp(List<String> segments) {
    String command = segments.remove(0);

    switch (command) {
      case "student-add":
        try {
          this.createStudent(segments);
        } catch (EntityAlreadyExistException eaea) {
          // do nothing
        }
        break;
      case "student-show":
        try {
          this.showStudent(segments);
        } catch (EntityNotFoundException enfe) {
          // do nothing
        }
        break;
      case "student-show-all":
        this.showAllStudents();
        break;
      case "course-add":
        try {
          this.createCourse(segments);
        } catch (EntityAlreadyExistException eaea) {
          // do nothing
        }
        break;
      case "course-show-all":
        this.showAllCourses();
        break;
      case "enroll":
        try {
          this.enroll(segments);
        } catch (EntityNotFoundException enfe) {
          // do nothing
        }
        break;
    }
  }
}
