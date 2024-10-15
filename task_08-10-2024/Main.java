import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MY_SQL mysql = new MY_SQL();
        if (mysql.openConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Задание 1. Вывести список всех книг одного жанра.");
            System.out.println("Введите название жанра:");
            String genre_name = scanner.nextLine();
            if (!mysql.selectBooksBuGenre(genre_name)) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\n\nЗадание 2. Найти автора, написавшего больше всего книг.");
            if (!mysql.selectBestAuthor()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\n\nЗадание 3. Показать все заказы одного покупателя.");
            System.out.println("Введите имя и фамилию покупателя через пробел:");
            String customer_name = scanner.nextLine();
            if (!mysql.selectOrdersByCustomer(customer_name)) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\n\nЗадание 4. Посчитать количество книг в каждом жанре.");
            if (!mysql.countInGenre()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\n\nЗадание 5. Отобразить все книги, изданные после 2000 года.");
            if (!mysql.getBooksBefore2000()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\n\nЗадание 6. Вывести список покупателей, сделавших больше 5 покупок.");
            if (!mysql.getCustomersBiggerFive()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }
        }else{
            System.out.println("Не удалось подключиться к базе данных. Произошла ошибка!");
        }
    }
}
