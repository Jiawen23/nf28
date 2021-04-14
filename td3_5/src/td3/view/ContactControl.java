package td3.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import td3.controller.ContactController;

import java.io.IOException;

public class ContactControl {

    public Parent root = null;
    public ContactController contactController;
    public ContactControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("contact_control.fxml"));
        try {
            root = fxmlLoader.load();
            contactController = fxmlLoader.getController();
        } catch (IOException exception) {

        throw new RuntimeException(exception); }

    } }