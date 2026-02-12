import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

class ParkingGarage {
    private final int capacity = 3;
    private final Semaphore semaphore = new Semaphore(capacity, true); 

    public void park(String carName) {
        try {
            System.out.println(carName + " is waiting to park...");
            semaphore.acquire();
            
            int slotsUsed = capacity - semaphore.availablePermits();
            System.out.println(">> " + carName + " has entered. Slots used: " + slotsUsed);

            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3001));

            System.out.println("<< " + carName + " left. Slots freed.");
            semaphore.release();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Car extends Thread {
    private final ParkingGarage garage;
    private final String carName;

    public Car(ParkingGarage garage, String carName) {
        this.garage = garage;
        this.carName = carName;
    }

    @Override
    public void run() {
        garage.park(carName);
    }
}

public class ParkingSimulation {
    public static void main(String[] args) {
        ParkingGarage garage = new ParkingGarage();

        for (int i = 1; i <= 10; i++) {
            new Car(garage, "Car-" + i).start();
        }
    }
}
