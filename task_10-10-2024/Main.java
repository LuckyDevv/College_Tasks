import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MY_SQL mysql = new MY_SQL();
        if (mysql.openConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nВас приветствует программа ветеринарной клиники EATHER!\n");
            if (!mysql.getAllAnimals()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\nЗапрос 1. Вывести историю приёмов животного.\nВведите имя животного:");
            if (!mysql.getHistoryByAnimalName(scanner.nextLine())) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\nЗапрос 2. Вывести все записи на осмотр в клинику.");
            if (!mysql.getAllRecords()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\nЗапрос 3. Вывести самые популярные услуги (ТОП-5).");
            if (!mysql.getMostPopularServices()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\nЗапрос 4. Вывести самые дорогие услуги (ТОП-5):");
            if (!mysql.getCostlyServices()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\nЗапрос 5. Найти животного по фамилии владельца:\nВведите фамилию владельца:");
            String result_animal = mysql.getAnimalByOwnerSurname(scanner.nextLine());
            if (result_animal.isEmpty()) {
                System.out.println("Не удалось найти животного!");
            }else{
                System.out.println("Итог поиска: " + result_animal);
            }

            System.out.println("\n\nЗапрос 6. Найти самого популярного ветеринара.");
            if (!mysql.getMostPopularDoctor()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\n\nЗапрос 7. Вывести весь список услуг клиники.");
            if (!mysql.getAllServices()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\nЗапрос 8. Вывести все записи доктора.\nВведите фамилию доктора:");
            if (!mysql.getRecordsByDoctorSurname(scanner.nextLine())) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\nЗапрос 9. Вывести общую выручку клиники.");
            if (!mysql.getClinicRevenue()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }

            System.out.println("\n\nЗапрос 10. Вывести зарплату ветеринаров, основываясь на их посещениях:");
            if (!mysql.getDoctorsPayDay()) {
                System.out.println("Не удалось выполнить запрос. Произошла ошибка!");
            }
        }else{
            System.out.println("Не удалось подключиться к базе данных. Произошла ошибка!");
        }
    }
}
