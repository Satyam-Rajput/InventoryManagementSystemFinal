package com.satyam.inventorymanagementsystem;

import java.io.IOException;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    public Stage stage;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private void login() throws IOException {

        try {
            DAO dao = new DAO();

            User dbUser = dao.getUser(username.getText());
            if (dbUser == null) {

                ShowAlert.showAlert(Alert.AlertType.ERROR, "Invalid Username");

            } else {
                User user = new User();
                user.setPassword(user.encode(password.getText()));

                if (!(dbUser.getPassword().equals(user.getPassword()))) {
                    ShowAlert.showAlert(Alert.AlertType.ERROR, "Invalid Password");

                } else {
                    dbUser.setLastLogin(new Date());
                    dao.addNewUser(dbUser);

                    App.stage.setWidth(1280);
                    App.stage.setHeight(720);
                    App.stage.setX(30);
                    App.stage.setY(20);
                    
                    User.user.setUsername(dbUser.getUsername());
                    User.user.setAccessType(dbUser.getAccessType());
                    App.setRoot("main");
                }
            }
        } catch (Exception e) {
            ShowAlert.showAlert(Alert.AlertType.ERROR, "Unable to start application");
        }

    }
}
