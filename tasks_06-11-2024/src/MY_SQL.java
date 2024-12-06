import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.security.MessageDigest;
import java.util.Arrays;

public class MY_SQL {
    private Connection connection = null;

    public boolean openConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/users_1?useSSL=false&serverTimezone=Europe/Moscow";
            String user = "root";
            String pass = "eather1192@How91";
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
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean createTables() {
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS `users_1` (`user_id` INT, `user_login` VARCHAR(255), `user_password` TEXT, `user_email` TEXT, `is_admin` BOOLEAN, PRIMARY KEY (`user_id`, `user_login`));");
            stmt.execute("CREATE TABLE IF NOT EXISTS `logs` (`log_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, `log_description` TEXT);");
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean accountExists(String user_login) {
        try {
            user_login = user_login.toLowerCase();
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `user_id` FROM `users_1` WHERE `user_login`=?;");
            preparedStatement.setString(1, user_login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public int setAdministrator(String user_login, boolean toAdmin) {
        try {
            if (isAdmin(user_login) != toAdmin) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("UPDATE `users_1` SET `is_admin`=? WHERE `user_login`=?;");
                preparedStatement.setBoolean(1, toAdmin);
                preparedStatement.setString(1, user_login);
                preparedStatement.executeUpdate();
                return 2;
            }else return 1;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
    public boolean isAdmin(String user_login) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `is_admin` FROM `users_1` WHERE `user_login`=?;");
            preparedStatement.setString(1, user_login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getBoolean("is_admin");
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean getAllAdmins() {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `user_id`,`user_login` FROM `users_1` WHERE `is_admin`=?;");
            preparedStatement.setBoolean(1, true);
            ResultSet resultSet = preparedStatement.executeQuery();
            String result = "";
            while (resultSet.next()) {
                result += "Администратор "+resultSet.getString("user_login")+", ID: "+resultSet.getString("user_id")+"\n";
            }
            if (result.isEmpty()) {
                System.out.println("Ещё нет администраторов!");
            }else{
                System.out.println("Список администраторов в нашей системе:\n" + result);
            }
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getAllUsers() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `user_id`,`user_login`,`user_email`,`is_admin` FROM `users_1`;");
            String result = "";
            while (resultSet.next()) {
                result += "Пользователь "+resultSet.getString("user_login")+", E-Mail: "+resultSet.getString("user_email")+", ID: "+resultSet.getString("user_id")+", "+ (resultSet.getBoolean("is_admin") ? "Является администратором" : "Не является администратором") +"\n";
            }
            if (result.isEmpty()) {
                System.out.println("Список пользователей пуст!");
            }else{
                System.out.println("Список пользователей в нашей системе:\n" + result);
            }
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public int accountAuth(String user_login, String user_password) {
        try {
            user_login = user_login.toLowerCase();
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `user_password`,`is_admin` FROM `users_1` WHERE `user_login`=?;");
            preparedStatement.setString(1, user_login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString(1).equals(this.hashSHA_256(user_password))) {
                    if (resultSet.getBoolean(2)) {
                        return 1;
                    }else{
                        return 2;
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean accountRegistration(String user_login, String user_password, String user_email) {
        try {
            user_login = user_login.toLowerCase();
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT IGNORE INTO `users_1` (`user_id`, `user_password`, `user_login`, `user_email`, `is_admin`) VALUES (?,?,?,?,?);");
            int id = this.getMaxUserId()+1;
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, this.hashSHA_256(user_password));
            preparedStatement.setString(3, user_login);
            preparedStatement.setString(4, user_email);
            preparedStatement.setBoolean(5, false);
            preparedStatement.execute();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getMaxUserId() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(`user_id`) FROM `users_1`;");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String hashSHA_256(String input) {
        Base64.Encoder base = Base64.getEncoder();
        return base.encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }
}