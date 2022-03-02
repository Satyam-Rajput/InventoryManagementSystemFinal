/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.satyam.inventorymanagementsystem;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Satyam
 */
public class ShowAlert {

    private static Alert alert = null;

    public static void showAlert(Alert.AlertType type, String msg) {

        alert = new Alert(type);
        String title = "";
        if (type == Alert.AlertType.ERROR) {
            title = "Erorr";
        } else {
            title = "Success";
        }
        alert.setTitle(title);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    public static boolean showConfirmationAlert(String msg) {

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(msg);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

}
