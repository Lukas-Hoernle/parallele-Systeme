package hoernle.lukas.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import hoernle.lukas.util.RandomUtil;

public class Task1 {
  public static void run() {
    CarWash carWash = new CarWash();
    ArrayList<Thread> queue = new ArrayList<>();
    Counter counter = new Counter();

    // Stunde 1
    // alle 5 Minuten: 1-3 Autos, jedes 2. Auto braucht Innenreinigung
    System.out.println("Run hour 1");
    runHour(counter, 3, 2, queue, carWash);

    // Stunde 2
    // alle 5 Minuten: 3-5 Autos, jedes 3. Auto braucht Innenreinigung
    System.out.println("Run hour 2");
    runHour(counter, 5, 3, queue, carWash);

    // Stunde 3
    // alle 5 Minuten: 1-2 Autos, jedes Auto braucht Innenreinigung
    System.out.println("Run hour 3");
    runHour(counter, 2, 1, queue, carWash);

    // Stunde 4
    // alle 5 Minuten: 1-2 Autos, jedes Auto braucht Innenreinigung
    System.out.println("Run hour 4");
    runHour(counter, 2, 1, queue, carWash);

    // Warten, bis alle Threads/Autos fertig sind
    queue.forEach(
        t -> {
          try {
            t.join();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
  }

  /**
   * @param counter wird zur Generierung von Auto-IDs verwendet
   * @param max die maximale Anzahl an Autos in einem 5-Minuten-Zeitraum
   * @param clean jedes n-te Auto benötigt eine Innenreinigung
   * @param queue Warteschlange, um auf alle Threads zu warten
   * @param carWash wird verwendet, um an den Autos zu arbeiten
   */
  private static void runHour(
      Counter counter, int max, int clean, List<Thread> queue, CarWash carWash) {
    IntStream.range(0, 12)
        .sequential()
        .mapToObj(
            slot ->
                IntStream.range(1, 1 + RandomUtil.fromInterval(1, max)).map(nr -> counter.getId()))
        .map(
            batch ->
                batch
                    .parallel()
                    .mapToObj(
                        id ->
                            new Thread(
                                () -> {
                                  Car car = new Car(id);
                                  try {
                                    carWash.useWashStreet(car);
                                    if (id % clean == 0) {
                                      carWash.useInteriorCleaning(car);
                                    }
                                  } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                  }
                                })))
        .forEach(
            batch -> {
              System.out.println("Start slot");
              batch
                  .parallel()
                  .forEach(
                      t -> {
                        t.start();
                        queue.add(t);
                      });
              try {
                // Warten Sie 5 Minuten für den nächsten Slot
                Thread.sleep(5 * 1000);
              } catch (InterruptedException e) {
                throw new RuntimeException(e);
              }
            });
  }

  public static class Counter {
    private int count;

    public synchronized int getId() {
      return ++count;
    }
  }
}
