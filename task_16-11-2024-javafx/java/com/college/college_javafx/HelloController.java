package com.college.college_javafx;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.sql.Timestamp;
import java.time.LocalDate;

public class HelloController {
    public PasswordField login_password;
    public javafx.scene.control.TextField login_login;
    public AnchorPane main_pane;
    public TextField reg_login;
    public TextField reg_surname;
    public TextField reg_name;
    public TextField reg_lastname;
    public PasswordField reg_password;
    public PasswordField reg_password_2;
    public Label label_notify;
    public TextField admin_wr_login;
    public TextArea text_list;
    public Pane pane_auth_switcher;
    public Pane pane_auth;
    public Pane pane_reg;
    public Pane pane_admin;
    public Pane pane_admin_2_admin;
    public Pane pane_list;
    public Pane pane_user;
    public TextField user_animal_name;
    public TextField user_animal_breed;
    public Pane pane_add_animal;
    public TextField admin_doctor_surname;
    public TextField admin_doctor_name;
    public TextField admin_doctor_lastname;
    public Pane pane_add_doctor;
    public Pane pane_add_service;
    public TextField admin_service_name;
    public TextField admin_service_cost;
    public Pane pane_add_record;
    public TextField user_record_doctor_fullname;
    public TextField user_record_animal_name;
    public TextField user_record_service_name;
    public DatePicker user_datepicker;

    private final MY_SQL my_sql = new MY_SQL();
    private boolean isAuth = false;
    private boolean isAdmin = false;
    private String authLogin = "";
    private boolean isAddAdmin = false;
    private boolean isRemoveUser = false;
    private boolean isRemoveAnimal = false;
    private String authName = "";

    private void sendNotify(String text) {
        if (text.isEmpty()) {
            label_notify.setVisible(false);
            return;
        }
        label_notify.setText(text);
        label_notify.autosize();
        label_notify.setLayoutX((main_pane.getWidth() - label_notify.getWidth()) / 2);
        label_notify.setVisible(true);
    }

    public void stopFocus(MouseEvent mouseEvent) {
        main_pane.requestFocus();
    }

    public void setAuthForm() {
        pane_auth_switcher.setVisible(false);
        pane_auth.setVisible(true);

        this.sendNotify("");
    }

    public void setStartForm() {
        pane_auth_switcher.setVisible(true);
        pane_reg.setVisible(false);
        pane_admin.setVisible(false);
        pane_admin_2_admin.setVisible(false);
        pane_list.setVisible(false);
        pane_auth.setVisible(false);
        pane_user.setVisible(false);
        pane_add_animal.setVisible(false);
        pane_add_service.setVisible(false);
        pane_add_doctor.setVisible(false);
        pane_add_record.setVisible(false);

        login_login.setText("");
        login_password.setText("");
        reg_login.setText("");
        reg_surname.setText("");
        reg_name.setText("");
        reg_lastname.setText("");
        reg_password.setText("");
        reg_password_2.setText("");

        this.sendNotify("");
    }

    public void setRegForm() {
        pane_auth_switcher.setVisible(false);
        pane_reg.setVisible(true);

        this.sendNotify("");
    }

    public void authUser(MouseEvent mouseEvent) {
        if (this.isAuth) {
            this.sendNotify("Вы уже вошли в аккаунт!");
            return;
        }
        this.sendNotify("");

        String login = login_login.getText();
        String password = login_password.getText();

        if (login.isEmpty()) {
            this.sendNotify("Не введён логин!");
            return;
        }
        if (password.isEmpty()) {
            this.sendNotify("Не введен пароль!");
            return;
        }
        if (this.my_sql.openConnection()) {
            if (this.my_sql.accountExists(login)) {
                switch (this.my_sql.accountAuth(login, password)) {
                    case 1:
                        this.sendNotify("Вы успешно авторизовались как администратор!");
                        this.isAuth = true;
                        this.isAdmin = true;
                        this.authLogin = login;
                        this.authName = my_sql.getUserNameByLogin(login);
                        setAdmin();
                        break;
                    case 2:
                        this.sendNotify("Вы успешно авторизовались!");
                        this.isAuth = true;
                        this.isAdmin = false;
                        this.authLogin = login;
                        this.authName = my_sql.getUserNameByLogin(login);
                        setUser();
                        break;
                    case 3:
                        this.sendNotify("Неверный пароль!");
                        break;
                    case 0:
                        this.sendNotify("Не удалось войти");
                        break;
                }
            }else this.sendNotify("Аккаунт не найден!");
            my_sql.closeConnection();
        }else this.sendNotify("Не удалось установить соединение с БД!");
    }

    public void regUser(MouseEvent mouseEvent) {
        if (this.isAuth) {
            this.sendNotify("Вы уже вошли в аккаунт!");
            return;
        }
        this.sendNotify("");

        String login = reg_login.getText();
        String password = reg_password.getText();
        String password2 = reg_password_2.getText();
        String surname = reg_surname.getText();
        String name = reg_name.getText();
        String lastname = reg_lastname.getText();

        if (login.isEmpty()) {
            this.sendNotify("Не введён логин!");
            return;
        }
        if (surname.isEmpty()) {
            this.sendNotify("Не введена фамилия!");
            return;
        }
        if (name.isEmpty()) {
            this.sendNotify("Не введено имя!");
            return;
        }
        if (lastname.isEmpty()) {
            this.sendNotify("Не введено отчество!");
            return;
        }
        if (password.isEmpty()) {
            this.sendNotify("Не введен пароль!");
            return;
        }
        if (password2.isEmpty()) {
            this.sendNotify("Не введено подтверждение пароля!");
            return;
        }
        if (!password.equals(password2)) {
            this.sendNotify("Подтверждение пароля не совпадает с паролем!");
            return;
        }
        String check_pwd = this.checkPassword(password);
        System.out.println(check_pwd);
        if (!check_pwd.isEmpty()) {
            this.sendNotify(check_pwd);
            return;
        }
        if (my_sql.openConnection()) {
            if (!my_sql.accountExists(login)) {
                if (my_sql.accountRegistration(login, password, surname, name, lastname)) {
                    this.isAuth = true;
                    this.authLogin = login;
                    this.authName = name;
                    sendNotify("Вы успешно зарегистрировались!");
                    setUser();
                }else sendNotify("Не удалось зарегистрироваться!");
            }else sendNotify("Вы уже зарегистрированы!");
            my_sql.closeConnection();
        }else sendNotify("Не удалось установить соединение с БД!");
    }

    private String checkPassword(String password) {
        if (password.length() < 8) {
            return "Длина пароля должна быть не менее 8 символов!";
        }
        if (password.toLowerCase().equals(password)) {
            return "В пароле должна быть минимум 1 заглавная буква!";
        }
        if (password.split(" ").length > 1) {
            return "В пароле не должно быть пробелов!";
        }
        return "";
    }

    private void setUser(){
        pane_auth_switcher.setVisible(false);
        pane_reg.setVisible(false);
        pane_admin.setVisible(false);
        pane_admin_2_admin.setVisible(false);
        pane_list.setVisible(false);
        pane_auth.setVisible(false);
        pane_user.setVisible(true);
        pane_add_animal.setVisible(false);
        pane_add_service.setVisible(false);
        pane_add_doctor.setVisible(false);
        pane_add_record.setVisible(false);
        this.sendNotify("Добро пожаловать, " + this.authName);
    }
    private void setAdmin(){
        pane_auth_switcher.setVisible(false);
        pane_reg.setVisible(false);
        pane_admin.setVisible(true);
        pane_admin_2_admin.setVisible(false);
        pane_list.setVisible(false);
        pane_auth.setVisible(false);
        pane_user.setVisible(false);
        pane_add_service.setVisible(false);
        pane_add_doctor.setVisible(false);
        this.sendNotify("Добро пожаловать, " + this.authName + " (админ)");
    }

    public void addAdmin(MouseEvent mouseEvent) {
        this.isAddAdmin = true;
        pane_admin.setVisible(false);
        pane_admin_2_admin.setVisible(true);
    }

    public void removeAdmin(MouseEvent mouseEvent) {
        this.isAddAdmin = false;
        pane_admin.setVisible(false);
        pane_admin_2_admin.setVisible(true);
    }


    public void removeUser(MouseEvent mouseEvent) {
        this.isAddAdmin = false;
        this.isRemoveUser = true;
        pane_admin.setVisible(false);
        pane_admin_2_admin.setVisible(true);
    }

    public void removeAnimal(MouseEvent mouseEvent) {
        this.isAddAdmin = false;
        this.isRemoveUser = false;
        this.isRemoveAnimal = true;
        pane_admin.setVisible(false);
        pane_admin_2_admin.setVisible(true);
        admin_wr_login.setPromptText("Введите ID животного...");
    }

    public void listAdmins(MouseEvent mouseEvent) {
        pane_admin.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getAllAdmins();
            if (!result.isEmpty()) {
                sendNotify("Список всех администраторов:");
                text_list.setText(result);
            }else{
                sendNotify("Список администраторов пуст.");
                pane_list.setVisible(false);
                pane_admin.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_admin.setVisible(true);
        }
    }

    public void listUsers(MouseEvent mouseEvent) {
        pane_admin.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getAllUsers();
            if (!result.isEmpty()) {
                sendNotify("Список всех пользователей:");
                text_list.setText(result);
            }else{
                sendNotify("Список пользователей пуст.");
                pane_list.setVisible(false);
                pane_admin.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_admin.setVisible(true);
        }
    }

    public void listServices(MouseEvent mouseEvent) {
        pane_admin.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getAllServices(true);
            if (!result.isEmpty()) {
                sendNotify("Список всех услуг:");
                text_list.setText(result);
            }else{
                sendNotify("Список услуг пуст.");
                pane_list.setVisible(false);
                pane_admin.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_admin.setVisible(true);
        }
    }

    public void listRecords(MouseEvent mouseEvent) {
        pane_admin.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getAllRecords();
            if (!result.isEmpty()) {
                sendNotify("Список всех записей на приём:");
                text_list.setText(result);
            }else{
                sendNotify("Список записей пуст.");
                pane_list.setVisible(false);
                pane_admin.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_admin.setVisible(true);
        }
    }

    public void listAnimals(MouseEvent mouseEvent) {
        pane_admin.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getAllAnimals();
            if (!result.isEmpty()) {
                sendNotify("Список всех животных:");
                text_list.setText(result);
            }else{
                sendNotify("Список животных пуст.");
                pane_list.setVisible(false);
                pane_admin.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_admin.setVisible(true);
        }
    }

    public void listDoctors(MouseEvent mouseEvent) {
        pane_admin.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getAllDoctors(true);
            if (!result.isEmpty()) {
                sendNotify("Список всех ветеринаров:");
                text_list.setText(result);
            }else{
                sendNotify("Список ветеринаров пуст.");
                pane_list.setVisible(false);
                pane_admin.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_admin.setVisible(true);
        }
    }

    public void leaveAccount(MouseEvent mouseEvent) {
        this.isAuth = false;
        this.isAdmin = false;
        this.isAddAdmin = false;
        this.isRemoveAnimal = false;
        this.isRemoveUser = false;
        this.authLogin = "";
        setStartForm();
    }

    public void admin_doo(MouseEvent mouseEvent) {
        String login_wr = admin_wr_login.getText();
        if (login_wr.isEmpty()) {
            sendNotify("Вы не ввели логин пользователя!");
            return;
        }
        if (my_sql.openConnection()) {
            if (this.isRemoveAnimal) {
                try {
                    int id_animal = Integer.parseInt(login_wr);
                    if (my_sql.animalExists(id_animal)) {
                        if (my_sql.removeAnimal(id_animal)) {
                            sendNotify("Животное успешно удалено!");
                            admin_wr_login.setText("");
                        }else{
                            sendNotify("Не удалось удалить животное!");
                        }
                    }else{
                        sendNotify("Не найдено животное с таким ID!");
                    }
                }catch (NumberFormatException e) {
                    sendNotify("ID должен быть числом!");
                }
            }else if (my_sql.accountExists(login_wr)) {
                if (this.isRemoveUser) {
                    if (my_sql.removeUser(login_wr)) {
                        sendNotify("Пользователь успешно удалён!");
                        admin_wr_login.setText("");
                    }else{
                        sendNotify("Не удалось выполнить запрос!");
                    }
                }else{
                    switch (my_sql.setAdministrator(login_wr, this.isAddAdmin)) {
                        case 0:
                            sendNotify("Не удалось выполнить запрос!");
                            break;
                        case 1:
                            if (this.isAddAdmin) {
                                sendNotify("Пользователь уже администратор!");
                            }else{
                                sendNotify("Пользователь не администратор!");
                            }
                            break;
                        case 2:
                            if (this.isAddAdmin) {
                                sendNotify("Администратор успешно назначен!");
                            }else{
                                sendNotify("Администратор успешно удалён!");
                            }
                            break;
                    }
                }
            }else sendNotify("Пользователь не найден!");
            my_sql.closeConnection();
            admin_wr_login.setText("");
        }else sendNotify("Не удалось установить соединение с БД!");
    }

    public void admin_cancel(MouseEvent mouseEvent) {
        if (!this.isAuth) {
            setStartForm();
            return;
        }

        if (this.isAdmin) {
            if (this.isRemoveAnimal) {
                admin_wr_login.setPromptText("Введите логин...");
            }
            this.isRemoveAnimal = false;
            this.isRemoveUser = false;
            this.isAddAdmin = false;
            this.setAdmin();
        }else{
            this.setUser();
        }
        admin_wr_login.setText("");
    }

    public void list_service(MouseEvent mouseEvent) {
        pane_user.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getAllServices(false);
            if (!result.isEmpty()) {
                sendNotify("Список услуг:");
                text_list.setText(result);
            }else{
                sendNotify("В нашей клинике пока нет услуг!");
                pane_list.setVisible(false);
                pane_user.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_user.setVisible(true);
        }
    }

    public void list_doctors(MouseEvent mouseEvent) {
        pane_user.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getAllDoctors(false);
            if (!result.isEmpty()) {
                sendNotify("Список ветеринаров:");
                text_list.setText(result);
            }else{
                sendNotify("В нашей клинике пока нет ветеринаров!");
                pane_list.setVisible(false);
                pane_user.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_user.setVisible(true);
        }
    }

    public void list_records_user(MouseEvent mouseEvent) {
        pane_user.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getRecordsByUserId(this.authLogin);
            if (!result.isEmpty()) {
                sendNotify("Список ваших записей на приём:");
                text_list.setText(result);
            }else{
                sendNotify("Вы не записывались на приём!");
                pane_list.setVisible(false);
                pane_user.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_user.setVisible(true);
        }
    }

    public void list_animals_user(MouseEvent mouseEvent) {
        pane_user.setVisible(false);
        pane_list.setVisible(true);

        if (my_sql.openConnection()) {
            String result = my_sql.getAnimalsByUser(this.authLogin);
            if (!result.isEmpty()) {
                sendNotify("Список ваших животных:");
                text_list.setText(result);
            }else{
                sendNotify("У вас пока нет животных!");
                pane_list.setVisible(false);
                pane_user.setVisible(true);
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
            pane_list.setVisible(false);
            pane_user.setVisible(true);
        }
    }

    public void user_animal_add(MouseEvent mouseEvent) {
        String animal_name = user_animal_name.getText();
        String animal_breed = user_animal_breed.getText();

        if (animal_name.isEmpty()) {
            sendNotify("Вы не ввели имя животного!");
            return;
        }

        if (animal_breed.isEmpty()) {
            sendNotify("Вы не ввели породу животного!");
            return;
        }

        if (my_sql.openConnection()) {
            if (my_sql.addAnimal(animal_name, animal_breed, my_sql.getIdByLogin(this.authLogin))) {
                sendNotify("Животное успешно добавлено!");
                user_animal_name.setText("");
                user_animal_breed.setText("");
            }else{
                sendNotify("Не удалось добавить животного!");
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
        }
    }

    public void addAnimal(MouseEvent mouseEvent) {
        pane_user.setVisible(false);
        pane_add_animal.setVisible(true);
    }

    public void admin_doctor_add(MouseEvent mouseEvent) {
        String doctor_surname = admin_doctor_surname.getText();
        String doctor_name = admin_doctor_name.getText();
        String doctor_lastname = admin_doctor_lastname.getText();

        if (doctor_surname.isEmpty()) {
            sendNotify("Не введена фамилия ветеринара!");
            return;
        }

        if (doctor_name.isEmpty()) {
            sendNotify("Не введено имя ветеринара!");
            return;
        }

        if (doctor_lastname.isEmpty()) {
            sendNotify("Не введено отчество ветеринара!");
            return;
        }

        if (my_sql.openConnection()) {
            if (my_sql.addDoctor(doctor_surname, doctor_name, doctor_lastname)) {
                sendNotify("Ветеринар успешно добавлен!");
                admin_doctor_surname.setText("");
                admin_doctor_name.setText("");
                admin_doctor_lastname.setText("");
            }else{
                sendNotify("Не удалось добавить ветеринара!");
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
        }
    }

    public void admin_service_add(MouseEvent mouseEvent) {
        String service_name = admin_service_name.getText();
        String service_cost = admin_service_cost.getText();

        if (service_name.isEmpty()) {
            sendNotify("Не введено название услуги!");
            return;
        }

        if (service_cost.isEmpty()) {
            sendNotify("Не введена цена услуги!");
            return;
        }

        try {
            int cost = Integer.parseInt(service_cost);
            if (my_sql.openConnection()) {
                if (my_sql.addService(service_name, cost)) {
                    sendNotify("Услуга успешно добавлена!");
                    admin_service_name.setText("");
                    admin_service_cost.setText("");
                }else{
                    sendNotify("Не удалось добавить услугу!");
                }
                my_sql.closeConnection();
            }else{
                sendNotify("Не удалось установить соединение с БД!");
            }
        }catch(NumberFormatException e) {
            sendNotify("Цена должна быть числом!");
        }
    }

    public void addDoctor(MouseEvent mouseEvent) {
        pane_admin.setVisible(false);
        pane_add_doctor.setVisible(true);
    }

    public void addService(MouseEvent mouseEvent) {
        pane_admin.setVisible(false);
        pane_add_service.setVisible(true);
    }

    public void user_record_add(MouseEvent mouseEvent) {
        String doctor_fullname = user_record_doctor_fullname.getText();
        String service_name = user_record_service_name.getText();
        String animal_name = user_record_animal_name.getText();
        LocalDate date = user_datepicker.getValue();
        Timestamp timestamp = Timestamp.valueOf(date.atStartOfDay());

        if (doctor_fullname.isEmpty()) {
            sendNotify("Не введено ФИО врача!");
            return;
        }

        String[] doctor_fullname_array = doctor_fullname.split(" ");
        if (doctor_fullname_array.length < 3) {
            sendNotify("ФИО должно быть введено через пробел!");
            return;
        }

        if (service_name.isEmpty()) {
            sendNotify("Вы не ввели название услуги!");
            return;
        }

        if (animal_name.isEmpty()) {
            sendNotify("Вы не ввели имя животного!");
            return;
        }

        if (timestamp.getTime() < System.currentTimeMillis()+86400000) {
            sendNotify("Нельзя создать запись ранее 1 дня!");
            return;
        }

        if (my_sql.openConnection()) {
            int doctor_id = my_sql.getDoctorIdByFullName(doctor_fullname_array[0], doctor_fullname_array[1], doctor_fullname_array[2]);
            if (doctor_id == 0) {
                sendNotify("Ветеринар не найден!");
                return;
            }

            int service_id = my_sql.getServiceIdByName(service_name);
            if (service_id == 0) {
                sendNotify("Услуга не найдена!");
                return;
            }

            int animal_id = my_sql.getAnimalIdByName(animal_name, this.authLogin);
            if (animal_id == 0) {
                sendNotify("Животное не найдено!");
                return;
            }

            if (my_sql.addRecord(animal_id, doctor_id, my_sql.getIdByLogin(this.authLogin), service_id, timestamp)) {
                sendNotify("Вы успешно записались на приём!");
                user_record_doctor_fullname.setText("");
                user_record_service_name.setText("");
                user_record_animal_name.setText("");
                user_datepicker.getEditor().clear();
            }else{
                sendNotify("Не удалось записаться на приём!");
            }
            my_sql.closeConnection();
        }else{
            sendNotify("Не удалось установить соединение с БД!");
        }
    }

    public void addRecord(MouseEvent mouseEvent) {
        pane_user.setVisible(false);
        pane_add_record.setVisible(true);
    }
}