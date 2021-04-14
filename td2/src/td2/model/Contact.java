package td2.model;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.function.Predicate;

public class Contact {

    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty city;
    private final StringProperty country;
    private final StringProperty gender;
    private final ObservableMap<String, String> validationMessages;
    Predicate<StringProperty> testProperty = (prop) -> {
        return prop.get() == null || ((String)prop.get()).trim().equals("");
    };

    public Contact() {
        this.lastName = new SimpleStringProperty(this, Property.NAME.name(), "");
        this.firstName = new SimpleStringProperty(this, Property.GIVEN_NAME.name(), "");
        this.gender = new SimpleStringProperty(this, Constants.SEX, "M");
        this.city = new SimpleStringProperty(this, Property.CITY.name(), "");
        this.country = new SimpleStringProperty(this, Property.COUNTRY.name(), "");
        this.validationMessages = FXCollections.observableHashMap();
    }

    public StringProperty lastNameProperty() {
        return this.lastName;
    }

    public StringProperty firstNameProperty() {
        return this.firstName;
    }

    public StringProperty genderProperty() {
        return this.gender;
    }

    public StringProperty countryProperty() {
        return this.country;
    }

    public StringProperty cityProperty() {
        return this.city;
    }

    private void validate(StringProperty sprop) {
        if (this.testProperty.test(sprop)) {
            this.validationMessages.put(sprop.getName(), Property.valueOf(sprop.getName()).tooltip);
        }

    }

    public boolean validate() {
        this.validationMessages.clear();
        this.validate(this.lastName);
        this.validate(this.firstName);
        this.validate(this.city);
        this.validate(this.country);
        return this.validationMessages.isEmpty();
    }

    public ObservableMap<String, String> validationMessagesProperty() {
        return this.validationMessages;
    }

    public String toString() {
        return (String)this.lastNameProperty().get() + " " + (String)this.firstNameProperty().get();
    }
}
