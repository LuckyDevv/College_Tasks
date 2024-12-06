package Cars;

public class CarsMain {
    public static void main(String[] args) {
        Car bugatti = new Car("Bugatti Veyron", 407);
        CarFactory ford = new CarFactory("Ford", 115 , 50000000);

        bugatti.color = "blue";
        bugatti.accelerationTo100km = 3;
        bugatti.engineVolume = 6.3;
        bugatti.manufacturerCountry = "Italy";
        bugatti.ownerFirstName = "Amigo";
        bugatti.yearOfIssue = 2016;
        bugatti.insurance = true;
        bugatti.price = 2000000;
        bugatti.isNew = false;
        bugatti.placesInTheSalon = 2;
        bugatti.maxSpeed = 407;
        bugatti.model = "Bugatti Veyron";

        System.out.println("Модель Bugatti Veyron. Объем двигателя - " + bugatti.engineVolume + ", багажника - " + bugatti.trunkVolume + ", салон сделан из " + bugatti.salonMaterial +
                ", ширина дисков - " + bugatti.wheels + ". Была приоберетена в 2018 году господином " + bugatti.ownerLastName);
        bugatti = new Car("Bugatti Veyron", 407);
        System.out.println(bugatti.model);
        System.out.println(bugatti.maxSpeed);

    }
}
/*
1. Ошибка в названии переменной. Переменные не совпадали. Поставил одинаковые названия.
2. Ошибка в выводе в консоль. Некорректный вывод. Исправил на корректные названия данных.
3. Пропущена точка с запятой. Поставил точку с запятой.
4. Прощуена точка в this. Поставил точку.
5. Объявлена переменная String вместо int. Изменил тип переменной.
 */