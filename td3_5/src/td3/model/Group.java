package td3.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.io.IOException;
import java.util.Set;

public class Group {

    private final StringProperty name;
    private final ObservableSet<Contact> contacts;
    public Group(String name) {
        this.name = new SimpleStringProperty(name);
        this.contacts = FXCollections.observableSet(new Contact[0]);
    }
    public Group() {
        this.name = new SimpleStringProperty("Nouveau Groupe");
        this.contacts = FXCollections.observableSet(new Contact[0]);
    }
    public StringProperty nameProperty() {
        return this.name;
    }
    public String getName() {
        return (String)this.name.get();
    }
    public ObservableSet<Contact> contactsProperty() {
        return this.contacts;
    }
    public void addContact(Contact c) {
        this.contacts.add(c.clone());
    }
    public void remove(Contact c) {
        this.contacts.remove(c);
    }
    public void updateContact(Contact oldC, Contact newC) {
        newC.copyTo(oldC);
    }
    public String toString() {
        return this.getName();
    }

    public Set<Contact> getContacts() {
        return this.contacts;
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

    public static Group read(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Group gr = null;
        try {
            gr = mapper.readValue(jsonString,
                    Group.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gr;
    }


    public int contactSize() {
        return this.contacts.size();
    }
}
