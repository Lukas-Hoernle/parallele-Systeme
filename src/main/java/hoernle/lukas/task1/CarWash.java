package hoernle.lukas.task1;

import java.util.List;
import hoernle.lukas.util.RandomUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CarWash {

  private final List<WashStreet> washStreets =
      List.of(new WashStreet(1), new WashStreet(2), new WashStreet(3));
  private final List<InteriorCleaning> interiorCleanings =
      List.of(new InteriorCleaning(1), new InteriorCleaning(2));

  public void useWashStreet(Car car) throws InterruptedException {
    System.out.printf("Car %s wants to use the wash street%n", car.getId());
    // prüft, ob ne Waschstraße frei ist
    washStreets.stream()
        .filter(WashStreet::isAvailable)
        .findAny()
        // wenn keine frei ist, warte auf eine zufällige
        .orElse(washStreets.get(RandomUtil.fromInterval(0, washStreets.size() - 1)))
        .tryUse(car);
  }

  public void useInteriorCleaning(Car car) throws InterruptedException {
    System.out.printf("Car %s wants to use interior cleaning%n", car.getId());
    // prüft, ob eine Innenreinigung frei ist
    interiorCleanings.stream()
        .filter(InteriorCleaning::isAvailable)
        .findAny()
        // wenn keine frei ist, warte auf eine zufällige
        .orElse(interiorCleanings.get(RandomUtil.fromInterval(0, interiorCleanings.size() - 1)))
        .tryUse(car);
  }
}
