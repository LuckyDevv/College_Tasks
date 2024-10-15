import java.sql.*;

public class MY_SQL {
    private Connection connection = null;
    /*
    10. Система учета домашних животных
    - Таблицы: Животные, Владельцы, Ветеринары, Записи о здоровье, Услуги.
     */
    public boolean openConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=Europe/Moscow";
            String user = "root";
            String pass = "eather1192@How91";
            this.connection = DriverManager.getConnection(url, user, pass);
            Statement statement = this.connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS `vet_clinic`;");
            statement.execute("USE `vet_clinic`");
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void closeConnection() {
        try {
            this.connection.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createTables() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS `animals` (`animal_id` INT, `animal_name` TEXT, `animal_breed` TEXT, PRIMARY KEY (`animal_id`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `owners` (`owner_id` INT, `owner_surname` TEXT, `owner_name` TEXT, `owner_lastname` TEXT, `animal_id` INT, PRIMARY KEY (`owner_id`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `veterinarians` (`veterinarian_id` INT, `veterinarian_surname` TEXT, `veterinarian_name` TEXT, `veterinarian_lastname` TEXT, PRIMARY KEY (`veterinarian_id`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `records` (`record_id` INT, `animal_id` INT, `veterinarian_id` INT, `record_description` TEXT, PRIMARY KEY (`record_id`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `services` (`service_id` INT, `service_title` TEXT, `service_cost` INT, PRIMARY KEY (`service_id`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `logs` (`log_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, `log_description` TEXT);");
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addAnimal(String name, String breed) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO `animals` (`animal_id`, `animal_name`, `animal_breed`) VALUES (?, ?, ?);");
            int id = this.getMaxAnimalId()+1;
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, breed);
            preparedStatement.execute();
            String log_text = "Добавлено животное. ID: "+id+", Имя: "+name+", Порода: "+breed;
            System.out.println(log_text);
            if (!this.addLog(log_text)) {
                System.out.println("Не удалось создать лог!");
            }
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean addOwner(String surname, String name, String lastname, int animal_id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO `owners` (`owner_id`, `owner_surname`, `owner_name`, `owner_lastname`, `animal_id`) VALUES (?, ?, ?, ?, ?);");
            int id = this.getMaxOwnerId()+1;
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, lastname);
            preparedStatement.setInt(5, animal_id);
            preparedStatement.execute();
            String log_text = "Добавлен хозяин. ID: "+id+", ФИО: "+surname+" "+name+" "+lastname;
            System.out.println(log_text);
            if (!this.addLog(log_text)) {
                System.out.println("Не удалось создать лог!");
            }
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addVeterinarian(String surname, String name, String lastname) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO `veterinarians` (`veterinarian_id`, `veterinarian_surname`, `veterinarian_name`, `veterinarian_lastname`) VALUES (?, ?, ?, ?);");
            int id = this.getMaxVeterinarianId()+1;
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, lastname);
            preparedStatement.execute();
            String log_text = "Добавлен ветеринар. ID: "+id+", ФИО: "+surname+" "+name+" "+lastname;
            System.out.println(log_text);
            if (!this.addLog(log_text)) {
                System.out.println("Не удалось создать лог!");
            }
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addService(String title, int cost) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO `services` (`service_id`, `service_title`, `service_cost`) VALUES (?,?,?);");
            int id = this.getMaxServiceId()+1;
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setInt(3, cost);
            preparedStatement.execute();
            String log_text = "Добавлена услуга. ID: "+id+", Название: "+title+", Цена: "+cost;
            System.out.println(log_text);
            if (!this.addLog(log_text)) {
                System.out.println("Не удалось создать лог!");
            }
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addRecord(int animal_id, int veterinarian_id, String record_description) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO `records` (`record_id`, `animal_id`, `veterinarian_id`, `record_description`) VALUES (?, ?, ?, ?);");
            int id = this.getMaxRecordId()+1;
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, animal_id);
            preparedStatement.setInt(3, veterinarian_id);
            preparedStatement.setString(4, record_description);
            preparedStatement.execute();
            String log_text = "Добавлена запись о здоровье. ID: "+id+", Результат осмотра: "+record_description+", ID животного: "+animal_id+", ID ветеринара: "+veterinarian_id;
            System.out.println(log_text);
            if (!this.addLog(log_text)) {
                System.out.println("Не удалось создать лог!");
            }
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getVeterinarianIdByFIO(String surname, String name, String lastname) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `veterinarian_id` FROM `veterinarians` WHERE `veterinarian_surname`=? AND `veterinarian_name`=? AND `veterinarian_lastname`=?;");
            preparedStatement.setString(1, surname);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, lastname);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("veterinarian_id");
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getAnimalIdByName(String animal_name) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `animal_id` FROM `animals` WHERE `animal_name` = ?;");
            preparedStatement.setString(1, animal_name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("animal_id");
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private int getMaxAnimalId() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(`animal_id`) FROM `animals`;");
            if (rs.next()) {
                return rs.getInt(1);
            }else return 0;
        }catch(SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private int getMaxOwnerId() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(`owner_id`) FROM `owners`;");
            if (rs.next()) {
                return rs.getInt(1);
            }else return 0;
        }catch(SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private int getMaxVeterinarianId() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(`veterinarian_id`) FROM `veterinarians`;");
            if (rs.next()) {
                return rs.getInt(1);
            }else return 0;
        }catch(SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private int getMaxServiceId() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(`service_id`) FROM `services`;");
            if (rs.next()) {
                return rs.getInt(1);
            }else return 0;
        }catch(SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private int getMaxRecordId() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(`record_id`) FROM `records`;");
            if (rs.next()) {
                return rs.getInt(1);
            }else return 0;
        }catch(SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    private boolean addLog(String text) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO `logs` (`log_date`, `log_description`) VALUES (DEFAULT, ?);");
            preparedStatement.setString(1, text);
            preparedStatement.execute();
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}