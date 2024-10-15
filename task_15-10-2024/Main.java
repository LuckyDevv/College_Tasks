import java.util.Scanner;

public class Main {
    private static final MY_SQL mysql = new MY_SQL();
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        if (mysql.openConnection()) {
            if (mysql.createTables()) {
                while (true) {
                    System.out.println(getMenuText());
                    int choice = scanner.nextInt();
                    menuSwitcher(choice);
                }
            }else{
                System.out.println("Невозможно продолжить работу приложения из-за невозможности создать таблицы.");
            }
        }
    }

    private static String getMenuText() {
        return """
                Выберите действие:
                1. Добавить животное
                2. Добавить хозяина
                3. Добавить ветеринара
                4. Добавить услугу
                5. Добавить запись о здоровье""";
    }

    private static void menuSwitcher(int choice) {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        switch (choice) {
            case 1:
                System.out.println("Введите имя животного:");
                String animal_name = scanner.nextLine();
                System.out.println("Введите породу животного:");
                String animal_breed = scanner.nextLine();
                if (!mysql.addAnimal(animal_name, animal_breed)) {
                    System.out.println("Не удалось добавить животного!");
                }
                break;
            case 2:
                System.out.println("Введите ФИО хозяина через пробел:");
                String[] owner_fio = scanner.nextLine().split(" ");
                String owner_surname = "";
                String owner_name = "";
                String owner_lastname = "";
                if (owner_fio.length >= 3) {
                    owner_surname = owner_fio[0];
                    owner_name = owner_fio[1];
                    owner_lastname = owner_fio[2];
                } else {
                    System.out.println("Вы не ввели ФИО хозяина!");
                    break;
                }

                System.out.println("Введите имя животного, которое пренадлежит этому хозяину:");
                int animal_id = mysql.getAnimalIdByName(scanner.nextLine());
                if (animal_id == 0) {
                    System.out.println("Не удалось найти животного с таким именем!");
                    break;
                }

                if (!mysql.addOwner(owner_surname, owner_name, owner_lastname, animal_id)) {
                    System.out.println("Не удалось добавить хозяина!");
                }
                break;
            case 3:
                System.out.println("Введите ФИО ветеринара через пробел:");
                String[] vet_fio = scanner.nextLine().split(" ");
                String vet_surname = "";
                String vet_name = "";
                String vet_lastname = "";
                if (vet_fio.length >= 3) {
                    vet_surname = vet_fio[0];
                    vet_name = vet_fio[1];
                    vet_lastname = vet_fio[2];
                } else {
                    System.out.println("Вы не ввели ФИО ветеринара!");
                    break;
                }
                if (!mysql.addVeterinarian(vet_surname, vet_name, vet_lastname)) {
                    System.out.println("Не удалось добавить ветеринара!");
                }
                break;
            case 4:
                System.out.println("Введите название услуги:");
                String service_title = scanner.nextLine();
                System.out.println("Введите цену услуги:");
                int service_cost = scanner.nextInt();
                if (!mysql.addService(service_title, service_cost)) {
                    System.out.println("Не удалось добавить услугу!");
                }
                break;
            case 5:
                System.out.println("Введите имя животного:");
                String animal_name_ = scanner.nextLine();
                int animal_id_ = mysql.getAnimalIdByName(animal_name_);
                if (animal_id_ == 0) {
                    System.out.println("Не удалось найти животного с таким именем!");
                    break;
                }

                System.out.println("Введите ФИО ветеринара через пробел:");
                String[] vet_fio_ = scanner.nextLine().split(" ");
                String vet_surname_ = "";
                String vet_name_ = "";
                String vet_lastname_ = "";
                if (vet_fio_.length >= 3) {
                    vet_surname_ = vet_fio_[0];
                    vet_name_ = vet_fio_[1];
                    vet_lastname_ = vet_fio_[2];
                } else {
                    System.out.println("Вы не ввели ФИО ветеринара!");
                    break;
                }
                int vet_id_ = mysql.getVeterinarianIdByFIO(vet_surname_, vet_name_, vet_lastname_);
                if (vet_id_ == 0) {
                    System.out.println("Не удалось найти ветеринара с таким ФИО!");
                    break;
                }

                System.out.println("Введите результат осмотра:");
                String record_description = scanner.nextLine();
                if (!mysql.addRecord(animal_id_, vet_id_, record_description)) {
                    System.out.println("Не удалось создать запись о здоровье!");
                }
            default:
                System.out.println("Я не знаю такого пункта, попробуйте ещё раз.");
                break;
        }
        System.out.println();
    }
}
