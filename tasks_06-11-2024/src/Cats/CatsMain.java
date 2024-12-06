package Cats;

public class CatsMain {
    public static void main(String[] args) {
        int count = 0;

        Cat cat1 = new Cat("Барсик", 1, 10);
        count++;
        System.out.println("Кот "+count+". Имя: " + cat1.getName() + ", Возраст: " + cat1.getAge() + ", Вес: " + cat1.getWeight());

        Cat cat2 = new Cat();
        cat2.setName("Коржик");
        cat2.setAge(2);
        cat2.setWeight(7);
        count++;

        System.out.println("Кот "+count+". Имя: " + cat2.getName() + ", Возраст: " + cat2.getAge() + ", Вес: " + cat2.getWeight());
        System.out.println("Общее кол-во котов: " + count);
    }
}