import java.sql.*;
import java.util.Random;

public class MY_SQL {
    private String url = "jdbc:mysql://localhost:3306/book_shop?useSSL=false&serverTimezone=Europe/Moscow";
    private String user = "root";
    private String pass = "eather1192@How91";
    private Connection connection = null;

    public boolean openConnection() {
        try {
            this.connection = DriverManager.getConnection(url, user, pass);
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void closeConnection() {
        try {
            Statement statement = this.connection.createStatement();
            statement.close();
            this.connection.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return this.connection;
    }
    public void createDatabase() {
        try {
            Statement statement = this.connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS `book_shop`;");
            statement.execute("USE `book_shop`;");
            statement.execute("CREATE TABLE IF NOT EXISTS `books` (`id_book` INT, `title` TEXT, `id_author` INT, `id_genre` INT, PRIMARY KEY (`id_book`));");
            statement.execute("CREATE TABLE IF NOT EXISTS `authors` (`id_author` INT, `author_surname` TEXT, `author_name` TEXT, `author_lastname` TEXT, PRIMARY KEY (`id_author`));");
            statement.execute("CREATE TABLE IF NOT EXISTS `genres` (`id_genre` INT, `genre_name` TEXT, PRIMARY KEY (`id_genre`));");
            statement.execute("CREATE TABLE IF NOT EXISTS `orders` (`id_order` INT, `id_book` INT, `id_customer` INT, PRIMARY KEY (`id_order`));");
            statement.execute("CREATE TABLE IF NOT EXISTS `customers` (`id_customer` INT, `customer_name` TEXT, `customer_surname` TEXT, PRIMARY KEY (`id_customer`));");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean selectBooksBuGenre(String genre) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT `id_genre` FROM `genres` WHERE `genre_name`=?;");
            statement.setString(1, genre);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            if (!resultSet.wasNull()) {
                int id_genre = resultSet.getInt("id_genre");
                statement = this.connection.prepareStatement("SELECT `title`,`id_author` FROM `books` WHERE `id_genre`=?;");
                statement.setInt(1, id_genre);
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String author = this.getAuthorById(resultSet.getInt("id_author"));
                    System.out.println("Книга «" + title + "» от автора - " + author);
                }
            }
            return true;
        }catch(SQLException _){
            return false;
        }
    }

    public boolean selectBestAuthor() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `id_author`,`author_surname`,`author_name`,`author_lastname` FROM `authors`;");
            String best_author = "";
            int best_book_count = 0;
            while (resultSet.next()) {
                PreparedStatement stmt = this.connection.prepareStatement("SELECT `id_author`, SUM(`id_author`=?) AS total FROM `books`;");
                stmt.setInt(1, resultSet.getInt("id_author"));
                ResultSet resultSet_1 = stmt.executeQuery();
                resultSet_1.next();
                if (resultSet_1.getInt("total") > best_book_count) {
                    best_author = resultSet.getString("author_surname") + " " + resultSet.getString("author_name") + " " + resultSet.getString("author_lastname");
                    best_book_count = resultSet_1.getInt("total");
                }
            }
            System.out.println("Больше всего книг (" + best_book_count + " книг) написал автор - " + best_author);
            return true;
        }catch(SQLException _) {
            return false;
        }
    }

    public boolean selectOrdersByCustomer(String customer_name) {
        try {
            String customer_name_ = customer_name.split(" ")[0];
            String customer_surname_ = customer_name.split(" ")[1];
            PreparedStatement stmt_prepare = this.connection.prepareStatement("SELECT `id_customer` FROM `customers` WHERE `customer_name`=? AND `customer_surname`=?;");
            stmt_prepare.setString(1, customer_name_);
            stmt_prepare.setString(2, customer_surname_);
            ResultSet resultSet = stmt_prepare.executeQuery();
            resultSet.next();
            if (!resultSet.wasNull()) {
                System.out.println("Список заказов, сделанных - " + customer_name + ":");
                int id_customer = resultSet.getInt("id_customer");
                stmt_prepare = this.connection.prepareStatement("SELECT `id_order`,`id_book` FROM `orders` WHERE `id_customer`=?;");
                stmt_prepare.setInt(1, id_customer);
                ResultSet resultSet_1 = stmt_prepare.executeQuery();
                while (resultSet_1.next()) {
                    int id_order = resultSet_1.getInt("id_order");
                    int id_book = resultSet_1.getInt("id_book");
                    stmt_prepare = this.connection.prepareStatement("SELECT `title`,`id_author` FROM `books` WHERE `id_book`=?;");
                    stmt_prepare.setInt(1, id_book);
                    ResultSet resultSet_2 = stmt_prepare.executeQuery();
                    resultSet_2.next();
                    if (!resultSet_2.wasNull()) {
                        String title = resultSet_2.getString("title");
                        String author = this.getAuthorById(resultSet_2.getInt("id_author"));
                        System.out.println("Заказ №" + id_order + ": книга «" + title + "» от автора - " + author);
                    }
                }
            }
            return true;
        }catch(SQLException _) {
            return false;
        }
    }

    public boolean countInGenre() {
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT `genre_name`,`id_genre` FROM `genres`;");
            while (resultSet.next()) {
                PreparedStatement stmt_1 = this.connection.prepareStatement("SELECT `id_genre`, SUM(`id_genre`=?) AS total FROM `books`;");
                stmt_1.setInt(1, resultSet.getInt("id_genre"));
                ResultSet resultSet_1 = stmt_1.executeQuery();
                resultSet_1.next();
                if (!resultSet_1.wasNull()) {
                    System.out.println("Количество книг с жанром «" + resultSet.getString("genre_name") + "» - " + resultSet_1.getInt("total") + " книг");
                }
            }
            return true;
        }catch(SQLException e){
            return false;
        }
    }

    public boolean getBooksBefore2000() {
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT `year`,`title`,`id_author` FROM `books` WHERE `year`>2000;");
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                result.append("Книга ").append(resultSet.getInt("year")).append(" года «").append(resultSet.getString("title")).append("» автора - ").append(this.getAuthorById(resultSet.getInt("id_author")));
            }
            if (result.toString().isEmpty()) {
                System.out.println("В магазине отсутствуют книги, выпущенные после 2000 года.");
            }else{
                System.out.println("Список книг, выпущенные после 2000 года:\n" + result);
            }
            return true;
        }catch(SQLException e) {
            return false;
        }
    }

    public boolean getCustomersBiggerFive() {
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT `id_customer`,`customer_name`,`customer_surname` FROM `customers`;");
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                PreparedStatement stmt_2 = this.connection.prepareStatement("SELECT `id_order`, SUM(`id_customer`=?) AS total FROM `orders`;");
                stmt_2.setInt(1, resultSet.getInt("id_customer"));
                ResultSet resultSet_2 = stmt_2.executeQuery();
                resultSet_2.next();
                if (!resultSet_2.wasNull()) {
                    int total = resultSet_2.getInt("total");
                    if (total > 5) {
                        result.append(resultSet_2.getString("customer_name")).append(" ").append(resultSet_2.getString("customer_surname")).append(" совершил ").append(total).append(" покупок");
                    }
                }
            }
            if (result.toString().isEmpty()) {
                System.out.println("В магазине отсутствуют покупатели, сделавшие больше 5 покупок!");
            }else{
                System.out.println("Список покупателей, сделавших больше 5 покупок:\n" + result);
            }
            return true;
        }catch(SQLException e) {
            return false;
        }
    }

    public String getAuthorById(int id) {
        try {
            PreparedStatement stmt = this.connection.prepareStatement("SELECT `author_surname`,`author_name`,`author_lastname` FROM `authors` WHERE `id_author`=?;");
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            if (!resultSet.wasNull()) {
                return resultSet.getString("author_surname") + " " + resultSet.getString("author_name") + " " + resultSet.getString("author_lastname");
            }
        }catch(SQLException _) {}
        return "Неизвестный автор";
    }
}
