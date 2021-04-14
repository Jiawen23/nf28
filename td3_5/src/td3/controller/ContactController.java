package td3.controller;


import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import td3.model.Contact;
import td3.model.Country;
import td3.sauvegarde.JSONWorkspace;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContactController {
    public Contact editingContact = new Contact();
    @FXML
    public Menu file;

    @FXML
    private ComboBox country;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField city;
    @FXML
    private RadioButton genderf;
    @FXML
    private RadioButton genderm;
    @FXML
    private ToggleGroup sexeGroup;
    private MapChangeListener<String, String> validateFunction;
//    @FXML
//    public void chooseFile() throws Exception {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setInitialDirectory(new File(String.valueOf(Paths.get("test.txt"))));
//        File file = fileChooser.showOpenDialog(null);
//        if (file != null) {
//            JSONWorkspace.fromFile(file);
//        }
//    }
    @FXML
    public void validate() {
        this.editingContact.validate();
    }

    private Map<String, Control> controls = new LinkedHashMap();

    public ContactController() {
    }

    public void initialize() {
        this.makeBindings();
        this.addControls(this.editingContact);
    }
    private void makeBindings() {
        this.validateFunction = (change) -> {
            Control control = (Control) this.controls.get(change.getKey());
            if (change.wasRemoved()) {
                control.setStyle("-fx-border-color:  blue ;");
                control.setTooltip((Tooltip) null);
            } else if (change.wasAdded()) {
                control.setStyle("-fx-border-color: red ;");
                control.setTooltip(new Tooltip((String) change.getValueAdded()));
            }

        };
        this.lastName.textProperty().bindBidirectional(this.editingContact.lastNameProperty());
        this.firstName.textProperty().bindBidirectional(this.editingContact.firstNameProperty());
        this.city.textProperty().bindBidirectional(this.editingContact.cityProperty());
        this.country.getSelectionModel().selectedItemProperty().addListener((observable, oldv, newv) -> {
            this.editingContact.countryProperty().setValue((String) newv);
        });
        this.editingContact.countryProperty().addListener((obj, o, n) -> {
            this.country.getSelectionModel().select(n);
        });
        this.country.setItems(FXCollections.observableList(Country.getCountryNames()));
        this.sexeGroup.selectedToggleProperty().addListener((observable, oldv, newv) -> {
            if (newv != null) {
                RadioButton bt = (RadioButton) newv;
                this.editingContact.genderProperty().set(bt.getText());
            }

        });
        this.editingContact.genderProperty().addListener((obj, o, n) -> {
            if (n != null) {
                if (n.equals("M")) {
                    this.sexeGroup.selectToggle(this.genderm);
                } else {
                    this.sexeGroup.selectToggle(this.genderf);
                }

            }
        });
        this.editingContact.validationMessagesProperty().addListener(this.validateFunction);
    }
    private void addControls(Contact contact) {
        this.controls.put(contact.lastNameProperty().getName(), this.lastName);
        this.controls.put(contact.firstNameProperty().getName(), this.firstName);
        this.controls.put(contact.cityProperty().getName(), this.city);
        this.controls.put(contact.countryProperty().getName(), this.country);
        this.controls.put(contact.genderProperty().getName(), this.genderf);
    }



}
