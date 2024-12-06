package Truck;

public class Truck {
    private int length;
    private int width;
    private int height;
    private int weight;

    public Truck(int length, int width, int height, int weight) {
        if (length >= 1) {
            this.length = length;
        }else {
            System.out.println("Нельзя установить отрицательную длину.");
        }

        if (width >= 1) {
            this.width = width;
        }else {
            System.out.println("Нельзя установить отрицательную ширину.");
        }

        if (height >= 1) {
            this.height = height;
        }else {
            System.out.println("Нельзя установить отрицательную ширину.");
        }

        if (weight >= 1) {
            this.weight = weight;
        }else{
            System.out.println("Нельзя установить отрицательный вес.");
        }
    }

    public Truck() {
    }

    public int getLength() {
        return this.length;
    }
    public void setLength(int length) {
        if (length >= 1) {
            this.length = length;
        }else {
            System.out.println("Нельзя установить отрицательную длину.");
        }
    }

    public int getWidth() {
        return this.width;
    }
    public void setWidth(int width) {
        if (width >= 1) {
            this.width = width;
        }else {
            System.out.println("Нельзя установить отрицательную ширину.");
        }
    }

    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        if (height >= 1) {
            this.height = height;
        }else {
            System.out.println("Нельзя установить отрицательную ширину.");
        }
    }

    public int getWeight() {
        return this.weight;
    }
    public void setWeight(int weight) {
        if (weight >= 1) {
            this.weight = weight;
        }else{
            System.out.println("Нельзя установить отрицательный вес.");
        }
    }

    public int getVolume() {
        return this.length * this.width * this.height;
    }
}
