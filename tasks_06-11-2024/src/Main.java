import Cars.CarsMain;
import Cats.CatsMain;
import Cats2.Cats2Main;
import Truck.TruckMain;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(getMenuText());
            int choice = sc.nextInt();
            menuSwitcher(choice);
        }
    }

    private static String getMenuText() {
        return """
                \nВыберите действие:
                1. Запустить котов
                2. Запустить котов 2.0
                3. Запустить Bugatti
                4. Запустить грузовики\n""";
    }

    private static void menuSwitcher(int choice) {
        switch (choice) {
            case 1:
                CatsMain.main(null);
                break;
            case 2:
                Cats2Main.main(null);
                break;
            case 3:
                CarsMain.main(null);
                break;
            case 4:
                TruckMain.main(null);
                break;
        }
    }
}
