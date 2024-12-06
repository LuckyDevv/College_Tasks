package Cats2;

public class Cats2Main {
    public static void main(String [] args) {
        Cat2 barsik = new Cat2("Барсик", 7, 100);
        barsik.SayMeow();
        barsik.setWeight(0);
        Cat2.count++;

        Cat2 Vasya = new Cat2("Вася", 8, 200 );
        Vasya.setAge(10);
        Cat2.count++;
        Vasya.Jump();



        System.out.println("Кот 1. Имя: " + barsik.name + ", Возраст: " + barsik.age + ", Вес: " + barsik.weight);
        System.out.println("Кот 2. Имя: " + Vasya.name + ", Возраст: " + Vasya.age + ", Вес: " + Vasya.weight);
        System.out.println("Колличество котов : " + Cat2.count);
    }
}
