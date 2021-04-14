package td3.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import td3.sauvegarde.JSONWorkspace;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationModel {
    private File fileToSave;
    private final ObservableList<Group> groups;
    private StringProperty error = new SimpleStringProperty();
    private BooleanProperty clearGroups = new SimpleBooleanProperty();
    ObservableList<PieChart.Data> groupPieChartData = FXCollections.observableArrayList();
    ObjectProperty<XYChart.Series<String, Number>> contactBarChartData = new SimpleObjectProperty();
    Map<Contact, Group> groupForContact = new HashMap();
    GraphicsTool graphicsTool;

    public ApplicationModel() {
        this.groups = FXCollections.observableArrayList();
        this.graphicsTool = new GraphicsTool(this.groups);
    }
    public ObservableList<Group> groupsProperty() {
        return this.groups;
    }

    public StringProperty errorProperty() {
        return this.error;
    }

    public BooleanProperty clearGroupsProperty() {
        return this.clearGroups;
    }

    public ObservableList<PieChart.Data> groupPieChartDataProperty() {
        return this.groupPieChartData;
    }

    public ObjectProperty<XYChart.Series<String, Number>> contactBarChartDataProperty() {
        return this.contactBarChartData;
    }

    public Map<Contact, Group> getGroupForContact() {
        return this.groupForContact;
    }
    public Group addGroup(){
        Group group = new Group("Nouveau Groupe");
        this.groups.add(group);
        return group;

    }
    public Group removeGroup(Group g){
        this.groups.remove(g);
        return g;
    }
    public void addContact(Contact c, Group g) {
        g.addContact(c);
    }
    public void removeContact(Contact c, Group g) {
        g.remove(c);
    }
    private void setFile(File file) {
        this.fileToSave = file;
    }
    public void updateExistingContact(Contact original, Contact edited, Group group) {
        group.updateContact(original, edited);
    }
    public void saveAs(File file) {
        try {
            JSONWorkspace jw = new JSONWorkspace();
            jw.setGroups(this.groups);
            jw.save(this.fileToSave);
            this.setFile(file);
        } catch (IOException var3) {
            this.error.setValue("Erreur d'Ã©criture du fichier");
            var3.printStackTrace();
        }

    }
    public void loadFile(File file) {
        try {
            JSONWorkspace jw = new JSONWorkspace();
            List<Group> grs = jw.fromFile(file);
            this.clearGroups.setValue(true);
            this.groups.clear();
            grs.forEach((gr) -> {
                Group addedGroup = new Group(gr.getName());
                this.groups.add(addedGroup);
                gr.getContacts().forEach((c) -> {
                    addedGroup.addContact(c);
                });
            });
            this.setFile(file);
        } catch (Exception var4) {
            this.error.setValue("Erreur de lecture du fichier");
            var4.printStackTrace();
        }

    }
    public void save() {
        if (this.fileToSave == null) {
            this.error.setValue("Pas de fichier sÃ©lectionnÃ©");
        } else {
            this.saveAs(this.fileToSave);
        }

    }
    public void designCharts() {
        List<PieChart.Data> ldata = this.graphicsTool.contactPieChartData();
        this.groupPieChartData.clear();
        this.groupPieChartData.addAll(ldata);
        XYChart.Series<String, Number> citySerie = this.graphicsTool.citiesSeriesData();
        this.contactBarChartData.setValue(citySerie);
    }

    public void reloadGraphics() {
        this.designCharts();
    }
}
