package com.college.college_javafx;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class MY_SQL {
    private Connection connection = null;

    public boolean openConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/vet_clinic?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Europe/Moscow";
            String user = "phpmyadmin";
            String pass = "5328alexRU";
            this.connection = DriverManager.getConnection(url, user, pass);
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void closeConnection() {
        try {
            this.connection.close();
        }catch(SQLException _) {}
    }

    public boolean createTables() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS `users` (`user_id` INT, `user_login` VARCHAR(255), `user_password` TEXT, `user_surname` TEXT, `user_name` TEXT, `user_lastname` TEXT, `is_admin` BOOLEAN, PRIMARY KEY (`user_id`, `user_login`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `doctors` (`doctor_id` INT, `doctor_surname` TEXT, `doctor_name` TEXT, `doctor_lastname` TEXT, `doctor_demand` INT, PRIMARY KEY (`doctor_id`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `animals` (`animal_id` INT, `animal_name` TEXT, `animal_breed` TEXT, `owner_id` INT, PRIMARY KEY (`animal_id`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `records` (`record_id` INT, `animal_id` INT, `doctor_id` INT, `user_id` INT, `service_id` INT, `record_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`record_id`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `services` (`service_id` INT, `service_name` TEXT, `service_cost` INT, `service_sold` INT, PRIMARY KEY (`service_id`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `logs` (`log_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, `log_description` TEXT);");
            return true;
        }catch(SQLException e) {
            return false;
        }
    }


    public boolean accountExists(String user_login) {
        try {
            user_login = user_login.toLowerCase();
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `user_id` FROM `users` WHERE `user_login`=?;");
            preparedStatement.setString(1, user_login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }catch(SQLException _) {}
        return false;
    }
    public boolean animalExists(int animal_id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `owner_id` FROM `animals` WHERE `animal_id`=?;");
            preparedStatement.setInt(1, animal_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }catch(SQLException _) {}
        return false;
    }
    public int setAdministrator(String user_login, boolean toAdmin) {
        try {
            if (this.isAdmin(user_login) != toAdmin) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("UPDATE `users` SET `is_admin`=? WHERE `user_login`=?;");
                preparedStatement.setBoolean(1, toAdmin);
                preparedStatement.setString(2, user_login);
                preparedStatement.executeUpdate();
                return 2;
            }else return 1;
        }catch(SQLException _){}
        return 0;
    }
    public boolean isAdmin(String user_login) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `is_admin` FROM `users` WHERE `user_login`=?;");
            preparedStatement.setString(1, user_login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getBoolean("is_admin");
            }
        }catch(SQLException _) {}
        return false;
    }
    public String getAllAdmins() {
        String result = "";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `user_id`,`user_login` FROM `users` WHERE `is_admin`=?;");
            preparedStatement.setBoolean(1, true);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result += "Администратор "+resultSet.getString("user_login")+", ID: "+resultSet.getString("user_id")+"\n";
            }
        }catch(SQLException _){}
        return result;
    }
    public String getAllUsers() {
        String result = "";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `users`;");
            while (resultSet.next()) {
                result += "Пользователь "+resultSet.getString("user_login")+
                        ", ФИО: "+resultSet.getString("user_surname")
                        + " " + resultSet.getString("user_name")
                        + " " + resultSet.getString("user_lastname")
                        +", ID: "+resultSet.getString("user_id")+", "
                        + (resultSet.getBoolean("is_admin") ? "Является администратором" : "Не является администратором") +"\n";
            }
        }catch(SQLException _){}
        return result;
    }
    public String getAllServices(boolean w_ids) {
        String result = "";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `services`;");
            NumberFormat nf = NumberFormat.getInstance();
            while (resultSet.next()) {
                String id = "";
                if (w_ids) {
                    id = "ID: " + resultSet.getString("service_id") + ", ";
                }
                result += id + "«" + resultSet.getString("service_name") +  "», цена: " + nf.format(resultSet.getInt("service_cost")) + " ₽\n";
            }
        }catch(SQLException _) {}
        return result;
    }
    public String getAllDoctors(boolean w_ids) {
        String result = "";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `doctors`;");
            while (resultSet.next()) {
                String id = "";
                if (w_ids) {
                    id = "ID: " + resultSet.getString("doctor_id") + ", ";
                }
                result += id + resultSet.getString("doctor_surname")
                        + " " + resultSet.getString("doctor_name")
                        + " " + resultSet.getString("doctor_lastname")
                        + ", репутация: " + resultSet.getString("doctor_demand") + "\n";
            }
        }catch(SQLException _) {}
        return result;
    }
    public String getAllAnimals() {
        String result = "";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `animals`;");
            while (resultSet.next()) {
                result += "ID: " + resultSet.getInt("animal_id") +
                        ", имя: " + resultSet.getString("animal_name") +
                        ", порода: «" + resultSet.getString("animal_breed") +
                        "», хозяин: " + this.getUserFullNameById(resultSet.getInt("owner_id")) + "\n";
            }
        }catch(SQLException _) {}
        return result;
    }

    public String getAllRecords() {
        String result = "";
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `records`;");
            while (resultSet.next()) {
                String animal = this.getAnimalById(resultSet.getInt("animal_id"));
                String doctor = this.getDoctorFullNameById(resultSet.getInt("doctor_id"));
                String service = this.getServiceById(resultSet.getInt("service_id"));
                String owner = this.getUserFullNameById(resultSet.getInt("user_id"));

                if (animal.isEmpty()) animal = "Неизвестно";
                if (doctor.isEmpty()) doctor = "Неизвестно";
                if (service.isEmpty()) service = "Неизвестно";
                if (owner.isEmpty()) owner = "Неизвестно";

                Timestamp date = resultSet.getTimestamp("record_date");
                String dateString = new SimpleDateFormat("dd.MM.yyyy").format(date);

                result += "ID: " + resultSet.getInt("record_id")
                        + ", животное: " + animal
                        + ", хозяин: " + owner
                        + ",\nветеринар: " + doctor
                        + ", услуга: " + service
                        + ", дата приёма: " + dateString + "\n\n";
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    public String getAnimalsByUser(String user_login) {
        String result = "";
        try {
            int id = this.getIdByLogin(user_login);
            if (id != 0) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM `animals` WHERE `owner_id`=?;");
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    result += resultSet.getString("animal_name") + ", порода: «" + resultSet.getString("animal_breed") + "»\n";
                }
            }
        }catch(SQLException _) {}
        return result;
    }
    public String getRecordsByUserId(String user_login) {
        String result = "";
        try {
            int user_id = this.getIdByLogin(user_login);
            if (user_id != 0) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM `records` WHERE `user_id`=?;");
                preparedStatement.setInt(1, user_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String animal = this.getAnimalById(resultSet.getInt("animal_id"));
                    String doctor = this.getDoctorFullNameById(resultSet.getInt("doctor_id"));
                    String service = this.getServiceById(resultSet.getInt("service_id"));

                    if (animal.isEmpty()) animal = "Неизвестно";
                    if (doctor.isEmpty()) doctor = "Неизвестно";
                    if (service.isEmpty()) service = "Неизвестно";

                    Timestamp date = resultSet.getTimestamp("record_date");
                    String dateString = new SimpleDateFormat("dd.MM.yyyy").format(date);

                    result += "ID: " + resultSet.getInt("record_id")
                            + ", животное: " + animal
                            + ",\nветеринар: " + doctor
                            + ", услуга: " + service
                            + ", дата приёма: " + dateString + "\n\n";
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean addAnimal(String animal_name, String animal_breed, int owner_id) {
        try {
            int id = this.getMaxAnimalId() + 1;
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT IGNORE INTO `animals` VALUES (?,?,?,?);");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, animal_name);
            preparedStatement.setString(3, animal_breed);
            preparedStatement.setInt(4, owner_id);
            preparedStatement.execute();
            return true;
        }catch(SQLException e) {
            return false;
        }
    }
    public boolean removeAnimal(int animal_id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("DELETE FROM `animals` WHERE `animal_id`=?;");
            preparedStatement.setInt(1, animal_id);
            preparedStatement.execute();
            return true;
        }catch(SQLException e) {
            return false;
        }
    }
    public boolean addRecord(int animal_id, int doctor_id, int owner_id, int service_id, Timestamp record_time) {
        try {
            int id = this.getMaxRecordId() + 1;
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT IGNORE INTO `records` VALUES (?,?,?,?,?,?);");
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, animal_id);
            preparedStatement.setInt(3, doctor_id);
            preparedStatement.setInt(4, owner_id);
            preparedStatement.setInt(5, service_id);
            preparedStatement.setTimestamp(6, record_time);
            preparedStatement.execute();
            return true;
        }catch(SQLException e) {
            return false;
        }
    }
    public boolean removeRecord(int record_id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("DELETE FROM `records` WHERE `record_id`=?;");
            preparedStatement.setInt(1, record_id);
            preparedStatement.execute();
            return true;
        }catch(SQLException e) {
            return false;
        }
    }
    public boolean addService(String service_name, int service_cost) {
        try {
            int id = this.getMaxServiceId() + 1;
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT IGNORE INTO `services` VALUES (?,?,?,?);");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, service_name);
            preparedStatement.setInt(3, service_cost);
            preparedStatement.setInt(4, 0);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public boolean removeService(int service_id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("DELETE FROM `services` WHERE `service_id`=?;");
            preparedStatement.setInt(1, service_id);
            preparedStatement.execute();
            return true;
        }catch(SQLException e) {
            return false;
        }
    }
    public boolean addDoctor(String surname, String name, String lastname) {
        try {
            int id = this.getMaxDoctorId() + 1;
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT IGNORE INTO `doctors` VALUES (?,?,?,?,?);");
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, lastname);
            preparedStatement.setInt(5, 0);
            preparedStatement.execute();
            return true;
        }catch(SQLException e) {
            return false;
        }
    }
    public boolean removeDoctors(int doctor_id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("DELETE FROM `doctors` WHERE `doctor_id`=?;");
            preparedStatement.setInt(1, doctor_id);
            preparedStatement.execute();
            return true;
        }catch(SQLException e) {
            return false;
        }
    }
    public boolean removeUser(String user_login) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("DELETE FROM `users` WHERE `user_login`=?;");
            preparedStatement.setString(1, user_login);
            preparedStatement.execute();
            return true;
        }catch(SQLException e) {
            return false;
        }
    }

    public int accountAuth(String user_login, String user_password) {
        try {
            user_login = user_login.toLowerCase();
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `user_password`,`is_admin` FROM `users` WHERE `user_login`=?;");
            preparedStatement.setString(1, user_login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString(1).equals(this.hash(user_password))) {
                    if (resultSet.getBoolean(2)) {
                        return 1;
                    }else{
                        return 2;
                    }
                }else return 3;
            }
        }catch (SQLException _) {}
        return 0;
    }
    public boolean accountRegistration(String user_login, String user_password, String user_surname, String user_name, String user_lastname) {
        try {
            user_login = user_login.toLowerCase();
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT IGNORE INTO `users` (`user_id`, `user_login`, `user_password`, `user_surname`, `user_name`, `user_lastname`, `is_admin`) VALUES (?,?,?,?,?,?,?);");
            int id = this.getMaxUserId()+1;
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, user_login);
            preparedStatement.setString(3, this.hash(user_password));
            preparedStatement.setString(4, user_surname);
            preparedStatement.setString(5, user_name);
            preparedStatement.setString(6, user_lastname);
            preparedStatement.setBoolean(7, false);
            preparedStatement.execute();
            return true;
        }catch (SQLException e) {
            return false;
        }
    }

    public String getUserNameByLogin(String login) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `user_name` FROM `users` WHERE `user_login`=?;");
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("user_name");
            }
        }catch(SQLException _) {}
        return "Неизвестно";
    }
    public int getMaxUserId() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(`user_id`) FROM `users`;");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException _) {}
        return 0;
    }
    public int getMaxAnimalId() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(`animal_id`) FROM `animals`;");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException _) {}
        return 0;
    }
    public int getMaxRecordId() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(`record_id`) FROM `records`;");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException _) {}
        return 0;
    }
    public int getMaxDoctorId() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(`doctor_id`) FROM `doctors`;");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException _) {}
        return 0;
    }
    public int getMaxServiceId() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(`service_id`) FROM `services`;");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException _) {}
        return 0;
    }
    public int getIdByLogin(String login) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `user_id` FROM `users` WHERE `user_login`=?;");
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        }catch(SQLException _) {}
        return 0;
    }
    private String getUserFullNameById(int user_id) {
        String result = "";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `user_surname`,`user_name`,`user_lastname` FROM `users` WHERE `user_id`=?;");
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("user_surname") + " " + resultSet.getString("user_name") + " " + resultSet.getString("user_lastname");
            }
        }catch(SQLException _) {}
        return result;
    }
    private String getDoctorFullNameById(int doctor_id) {
        String result = "";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `doctor_surname`,`doctor_name`,`doctor_lastname` FROM `doctors` WHERE `doctor_id`=?;");
            preparedStatement.setInt(1, doctor_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("doctor_surname") + " " + resultSet.getString("doctor_name") + " " + resultSet.getString("doctor_lastname");
            }
        }catch(SQLException _) {}
        return result;
    }
    private String getAnimalById(int animal_id) {
        String result = "";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `animal_name`,`animal_breed` FROM `animals` WHERE `animal_id`=?;");
            preparedStatement.setInt(1, animal_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getString("animal_name") + ", порода: «" + resultSet.getString("animal_breed") + "»";
            }
        }catch(SQLException _) {}
        return result;
    }
    private String getServiceById(int service_id) {
        String result = "";
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `service_name`,`service_cost` FROM `services` WHERE `service_id`=?;");
            preparedStatement.setInt(1, service_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                NumberFormat nf = NumberFormat.getInstance();
                result = "«" + resultSet.getString("service_name") + "», цена: " + nf.format(resultSet.getInt("service_cost")) + " ₽";
            }
        }catch(SQLException _) {}
        return result;
    }
    public int getServiceIdByName(String service_name) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `service_id` FROM `services` WHERE `service_name`=?;");
            preparedStatement.setString(1, service_name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("service_id");
            }
        }catch(SQLException _){}
        return 0;
    }
    public int getDoctorIdByFullName(String surname, String name, String lastname) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `doctor_id` FROM `doctors` WHERE `doctor_surname`=? AND `doctor_name`=? AND `doctor_lastname`=?;");
            preparedStatement.setString(1, surname);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, lastname);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("doctor_id");
            }
        }catch(SQLException _) {}
        return 0;
    }
    public int getAnimalIdByName(String animal_name, String user_login) {
        try {
            int owner_id = this.getIdByLogin(user_login);
            if (owner_id != 0) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `animal_id` FROM `animals` WHERE `animal_name`=? AND `owner_id`=?;");
                preparedStatement.setString(1, animal_name);
                preparedStatement.setInt(2, owner_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("animal_id");
                }
            }
        }catch(SQLException _) {}
        return 0;
    }
    public String hash(String input) {
        Base64.Encoder base = Base64.getEncoder();
        return base.encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }
}