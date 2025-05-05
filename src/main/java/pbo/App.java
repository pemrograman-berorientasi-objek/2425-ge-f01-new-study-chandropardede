package pbo;

/**
 * Main class
 *
 */
public class App {

  public static void main(String[] args) {
    String persistenceUnitName = "study_plan_pu";

    (new Simulator(persistenceUnitName)).run();
  }
}
