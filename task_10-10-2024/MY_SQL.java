import java.sql.*;

public class MY_SQL {
    private Connection connection = null;
    public boolean openConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/veterinary_clinic?useSSL=false&serverTimezone=Europe/Moscow";
            String user = "root";
            String pass = "eather1192@How91";
            this.connection = DriverManager.getConnection(url, user, pass);
            return true;
        }catch(SQLException e) {
            return false;
        }
    }

    public boolean getAllAnimals() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `animals`;");
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                result.append(resultSet.getString("animal_name")).append(" (").append(resultSet.getString("animal_breed")).append("). Хозяин: ").append(this.getOwnerByAnimalId(resultSet.getInt("id_animal"))).append("\n");
            }
            if (result.isEmpty()) {
                System.out.println("В нашей клинике ещё нет животных :(");
            }else{
                System.out.println("Список животных нашей клиники:\n" + result);
            }
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public boolean getHistoryByAnimalName(String animalName) {
        try {
            int id_an = this.getAnimalIdByName(animalName);
            if (id_an == -1) {
                System.out.println("Животное с именем «" + animalName + "» не найдено в нашей клинике!");
                return true;
            }
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM `history` WHERE `id_animal`=?;");
            preparedStatement.setInt(1, id_an);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                if (!resultSet.wasNull()) {
                    int id_animal = resultSet.getInt("id_animal");
                    result.append("Услуга «").append(this.getServiceById(resultSet.getInt("id_service"))).append("», время посещения: ").append(resultSet.getString("history_date")).append(", имя и порода животного: ").append(this.getAnimalNameById(id_animal)).append(" (").append(this.getAnimalBreedById(id_animal)).append(")").append("\n");
                }
            }
            if (result.toString().isEmpty()) {
                System.out.println("Животное по имени «" + animalName + "» ещё не посещало нашу клинику!");
            }else{
                System.out.println("История посещений нашей клиники животным по имени «" + animalName + "»:\n" + result);
            }
            return true;
        }catch (SQLException e) {
            return false;
        }
    }
    public boolean getAllRecords() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `records`;");
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                if (!resultSet.wasNull()) {
                    int id_animal = resultSet.getInt("id_animal");
                    int id_service = resultSet.getInt("id_service");
                    String record_date = resultSet.getString("record_time");

                    result.append("Запись на ").append(record_date).append(": ").append(this.getAnimalNameById(id_animal)).append(" (").append(this.getAnimalBreedById(id_animal)).append(")").append(", услуга: «").append(this.getServiceById(id_service)).append("\n");
                }
            }
            if (result.toString().isEmpty()) {
                System.out.println("На данный момент записи на приём отсутствуют!");
            }else{
                System.out.println("Список записей на приём в нашей клинике:\n" + result);
            }
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public boolean getMostPopularServices() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `service_name`,`service_sold` FROM `service` ORDER BY `service_sold` DESC LIMIT 5;");
            StringBuilder result = new StringBuilder();
            int count = 1;
            while (resultSet.next()) {
                if (!resultSet.wasNull()) {
                    result.append("№").append(count).append(" - «").append(resultSet.getString("service_name")).append("», количество продаж ").append(resultSet.getString("service_sold")).append("\n");
                    count++;
                }
            }
            if (result.toString().isEmpty()) {
                System.out.println("В нашей клинике нет услуг :(");
            }else{
                System.out.println("Список 5 самых популярных услуг нашей клиники:\n" + result);
            }
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public boolean getCostlyServices() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `service_name`,`service_cost` FROM `service` ORDER BY `service_cost` DESC LIMIT 5;");
            StringBuilder result = new StringBuilder();
            int count = 1;
            while (resultSet.next()) {
                if (!resultSet.wasNull()) {
                    result.append("№").append(count).append(" - «").append(resultSet.getString("service_name")).append("», цена ").append(resultSet.getString("service_cost")).append("\n");
                    count++;
                }
            }
            if (result.toString().isEmpty()) {
                System.out.println("В нашей клинике нет услуг :(");
            }else{
                System.out.println("Список 5 самых дорогих услуг нашей клиники:\n" + result);
            }
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public boolean getMostPopularDoctor() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `doctor_surname`,`doctor_name`,`doctor_lastname`,`doctor_demand` FROM `doctors` ORDER BY `doctor_demand` DESC LIMIT 1;");
            StringBuilder result = new StringBuilder();
            resultSet.next();
            if (!resultSet.wasNull()) {
                result.append("Самый популярный ветеринар нашей клиники - ").append(resultSet.getString("doctor_surname")).append(" ").append(resultSet.getString("doctor_name")).append(" ").append(resultSet.getString("doctor_lastname")).append(", количество посещений: ").append(resultSet.getString("doctor_demand"));
            }
            if (result.toString().isEmpty()) {
                System.out.println("В нашей клинике нет врачей :(");
            }else{
                System.out.println(result);
            }
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public boolean getAllServices() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `service`;");
            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                if (!resultSet.wasNull()) {
                    result.append("Услуга «").append(resultSet.getString("service_name")).append("», цена: ").append(resultSet.getString("service_cost")).append(", кол-во продаж: ").append(resultSet.getString("service_sold")).append("\n");
                }
            }
            if (result.toString().isEmpty()) {
                System.out.println("В нашей клинике нет услуг :(");
            }else{
                System.out.println("Список всех услуг нашей клиники:\n" + result);
            }
            return true;
        }catch(SQLException e) {
            return false;
        }
    }
    public boolean getRecordsByDoctorSurname(String surname) {
        try {
            int id_doctor = this.getDoctorIdBySurname(surname);
            if (id_doctor != -1) {
                PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM `records` WHERE `id_doctor`=?;");
                preparedStatement.setInt(1, id_doctor);
                ResultSet resultSet = preparedStatement.executeQuery();
                StringBuilder result = new StringBuilder();
                while (resultSet.next()) {
                    if (!resultSet.wasNull()) {
                        result.append("Запись на ").append(resultSet.getString("record_time")).append(", услуга: «").append(this.getServiceById(resultSet.getInt("id_service"))).append("\n");
                    }
                }
                if (result.toString().isEmpty()) {
                    System.out.println("У данного ветеринара нет записей на ближайшее время.");
                }else{
                    System.out.println("Список записей у данного ветеринара:\n" + result);
                }
            }
            return true;
        }catch(SQLException e) {
            return false;
        }
    }
    public boolean getClinicRevenue() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `id_service` FROM `history`;");
            int revenue = 0;
            while (resultSet.next()) {
                if (!resultSet.wasNull()) {
                    int cost_ = this.getCostByServiceId(resultSet.getInt("id_service"));
                    if (cost_ > 0) revenue += cost_;
                }
            }
            System.out.println("Общая выручка клиники составляет " + revenue + " руб.!");
            return true;
        }catch(SQLException e){
            return false;
        }
    }
    public boolean getDoctorsPayDay() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `doctors`;");
            while (resultSet.next()) {
                if (!resultSet.wasNull()) {
                    PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `id_service` FROM `history` WHERE `id_doctor`=?;");
                    preparedStatement.setInt(1, resultSet.getInt("id_doctor"));
                    ResultSet resultSet2 = preparedStatement.executeQuery();
                    int payday = 0;
                    while (resultSet2.next()) {
                        if (!resultSet2.wasNull()) {
                            int cost_ = this.getCostByServiceId(resultSet2.getInt("id_service"));
                            if (cost_ > 0) payday += cost_;
                        }
                    }
                    System.out.println("Зарплата ветеринара (" + resultSet.getString("doctor_surname") + " " + resultSet.getString("doctor_name") + " " + resultSet.getString("doctor_lastname") + ") - " + payday + " руб.");
                }
            }
            return true;
        }catch(SQLException e){
            return false;
        }
    }

    public int getCostByServiceId(int id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `service_cost` FROM `service` WHERE `id_service`=?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("service_cost");
            }
            return -1;
        }catch(SQLException e){
            return -1;
        }
    }
    public int getDoctorIdBySurname(String surname) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `id_doctor` FROM `doctors` WHERE `doctor_surname`=?;");
            preparedStatement.setString(1, surname);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (!resultSet.wasNull()) {
                return resultSet.getInt("id_doctor");
            }else return -1;
        }catch(SQLException e) {
            return -1;
        }
    }
    public String getServiceById(int id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM `service` WHERE `id_service`=?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder result = new StringBuilder();
            resultSet.next();
            if (!resultSet.wasNull()) {
                result.append(resultSet.getString("service_name")).append("», цена: ").append(resultSet.getInt("service_cost"));
            }
            if (result.isEmpty()) {
                return "Неизвестная услуга";
            }else{
                return result.toString();
            }
        }catch(SQLException e) {
            return "Неизвестная услуга";
        }
    }
    public String getAnimalNameById(int id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `animal_name` FROM `animals` WHERE `id_animal`=?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder result = new StringBuilder();
            resultSet.next();
            if (!resultSet.wasNull()) {
                result.append(resultSet.getString("animal_name"));
            }
            if (result.isEmpty()) {
                return "Неизвестное животное";
            }else{
                return result.toString();
            }
        }catch(SQLException e) {
            return "Неизвестное животное";
        }
    }
    public String getAnimalBreedById(int id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `animal_breed` FROM `animals` WHERE `id_animal`=?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder result = new StringBuilder();
            resultSet.next();
            if (!resultSet.wasNull()) {
                result.append(resultSet.getString("animal_breed"));
            }
            if (result.isEmpty()) {
                return "Неизвестное животное";
            }else{
                return result.toString();
            }
        }catch(SQLException e) {
            return "Неизвестное животное";
        }
    }
    public String getOwnerByAnimalId(int animal_id) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT * FROM `owners` WHERE `id_animal`=?;");
            preparedStatement.setInt(1, animal_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder result = new StringBuilder();
            resultSet.next();
            if (!resultSet.wasNull()) {
                result.append(resultSet.getString("owner_surname")).append(" ").append(resultSet.getString("owner_name")).append(" ").append(resultSet.getString("owner_lastname"));
            }
            if (result.isEmpty()) {
                return "Неизвестный владелец";
            }else{
                return result.toString();
            }
        }catch(SQLException e) {
            return "Неизвестный владелец";
        }
    }
    public String getAnimalByOwnerSurname(String surname) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `id_animal` FROM `owners` WHERE `owner_surname`=?;");
            preparedStatement.setString(1, surname);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (!resultSet.wasNull()) {
                int id_animal = resultSet.getInt("id_animal");
                return this.getAnimalNameById(id_animal) + " (" + this.getAnimalBreedById(id_animal) + ")";
            }
            return "";
        }catch(SQLException e){
            return "";
        }
    }
    public int getAnimalIdByName(String name) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT `id_animal` FROM `animals` WHERE `animal_name`=?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (!resultSet.wasNull()) {
                return resultSet.getInt("id_animal");
            }else return -1;
        }catch(SQLException e){
            return -1;
        }
    }
}