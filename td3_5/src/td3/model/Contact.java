package td3.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.IOException;
import java.util.function.Predicate;

public class Contact {

    private final StringProperty lastName;
    private final StringProperty firstName;
    private final StringProperty city;
    private final StringProperty country;
    private final StringProperty gender;
    private final BooleanProperty validate;
    private final ObservableMap<String, String> validationMessages;
    Predicate<StringProperty> testProperty = (prop) -> {
        return prop.get() == null || ((String)prop.get()).trim().equals("");
    };

    public Contact() {
        this.validate = new SimpleBooleanProperty();;
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
    public BooleanProperty validateProperty() {
        return this.validate;
    }
    private void validate(StringProperty sprop) {
        if (this.testProperty.test(sprop)) {
            this.validationMessages.put(sprop.getName(), Property.valueOf(sprop.getName()).tooltip);
        }

    }

    public void validate() {
        this.validationMessages.clear();
        this.validate(this.lastName);
        this.validate(this.firstName);
        this.validate(this.city);
        this.validate(this.country);
        this.validate.setValue(false);
        this.validate.setValue(this.validationMessages.isEmpty());
    }

    public ObservableMap<String, String> validationMessagesProperty() {
        return this.validationMessages;
    }

    public String toString() {
        return (String)this.lastNameProperty().get() + " " + (String)this.firstNameProperty().get();
    }
    public Contact clone() {
        Contact target = new Contact();
        this.copyTo(target);
        return target;
    }

    public void init() {
        (new Contact()).copyTo(this);
    }

    public void copyTo(Contact target) {
        target.lastNameProperty().set((String)this.lastNameProperty().get());
        target.firstNameProperty().set((String)this.firstNameProperty().get());
        target.cityProperty().set((String)this.cityProperty().get());
        target.countryProperty().set((String)this.countryProperty().get());
        target.genderProperty().set((String)this.genderProperty().get());
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        String s = "";
        try {
            s = mapper.writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static Contact read(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Contact con = null;
        try {
            con = mapper.readValue(jsonString,
                    Contact.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
    }

    public String getCity() {
        return this.city.getValue();
    }

    public void setCity(String city) {
        this.city.setValue(city);
    }
}

