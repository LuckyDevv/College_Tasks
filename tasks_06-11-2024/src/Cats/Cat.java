package Cats;

public class Cat {

    private String name;
    private int age;
    private int weight;

    public Cat(String name, int age, int weight) {
        this.name = name;
        if (age >= 0) {
            this.age = age;
        }else{
            System.out.println("Нельзя установить отрицательный возраст.");
            this.age = 0;
        }
        if (weight >= 0) {
            this.weight = weight;
        }else{
            System.out.println("Нельзя установить отрицательный вес.");
            this.weight = 0;
        }
    }

    public Cat() {
    }

    public void sayMeow() {
        System.out.println("Мяу!");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        }else{
            System.out.println("Нельзя установить отрицательный возраст кота!");
        }
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        if (weight >= 0) {
            this.weight = weight;
        }else{
            System.out.println("Нельзя установить отрицательный вес кота!");
        }
    }
}
