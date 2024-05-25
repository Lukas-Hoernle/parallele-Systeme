package hoernle.lukas.task1;

import hoernle.lukas.util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CleaningStreet {
  private final int id;
  @Getter private boolean isFree = true; // gibt an, ob die Reinigungstraße frei ist

  private synchronized void use(Car car) throws InterruptedException {
    isFree = false;
    long duration = RandomUtil.fromInterval(5, 12);
    System.out.printf(
        "Car %s is using CleaningStreet %s for %s minutes%n", car.getId(), id, duration);
    Thread.sleep(duration * 1000);
    isFree = true;
    notify(); // benachrichtigt wartende Threads, dass die Reinigungstraße frei ist
  }

  public synchronized void tryUse(Car car) throws InterruptedException {
    while (!isFree) {
      try {
        wait(); // wartet, bis die Reinigungstraße frei wird
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    use(car);
    System.out.printf("Car %s is leaving CleaningStreet %s%n", car.getId(), id);
  }
}
