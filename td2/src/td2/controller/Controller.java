package td2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import td2.model.Contact;
import td2.model.Country;

import java.util.LinkedHashMap;
import java.util.Map;

public class Controller {
    public Contact editingContact = new Contact();

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

    @FXML
    public void validate() {
        this.editingContact.validate();
    }

    private Map<String, Control> controls = new LinkedHashMap();

    public Controller() {
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
