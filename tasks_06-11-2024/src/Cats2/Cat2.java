package Cats2;

public  class Cat2 {
    String name;
    int age;
    static int count;
    int weight;
    public Cat2(String name, int age, int weight){
        this.name = name;
        this.age = age;
        this.weight = weight;
    }
    public void SayMeow() {
        System.out.println("Мяу");
    }
    public void Jump() {
        System.out.println("Кот прыгнул");
    }
    public void Stray(){
        count++;
        this.name = "Уличный кот номер: " + count;
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
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
