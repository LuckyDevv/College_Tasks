package Truck;

public class TruckMain {
    public static void main(String[] args) {
        int count = 0;

        Truck truck1 = new Truck(10, 2, 2, 2000);
        count++;

        Truck truck2 = new Truck();
        truck2.setLength(7);
        truck2.setWidth(1);
        truck2.setHeight(2);
        truck2.setWeight(1700);
        count++;

        System.out.println("Грузовик 1. Длина: " + truck1.getLength() + ", Ширина: " + truck1.getWidth() + ", Высота: " + truck1.getHeight() + ", Вес: " + truck1.getWeight());
        System.out.println("Грузовик 2. Длина: " + truck2.getLength() + ", Ширина: " + truck2.getWidth() + ", Высота: " + truck2.getHeight() + ", Вес: " + truck2.getWeight());

        System.out.println("Количество грузовиков: " + count);
    }
}
