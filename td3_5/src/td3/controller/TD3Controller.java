package td3.controller;

import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import td3.model.ApplicationModel;
import td3.model.Constants;
import td3.model.Contact;
import td3.model.Group;
import td3.sauvegarde.JSONWorkspace;
import td3.view.ContactControl;

import java.awt.event.ActionEvent;
import java.io.File;

public class TD3Controller {
    @FXML
    BarChart<String, Number> contactBarChart;
    @FXML
    PieChart groupPieChart;
    @FXML
    private BorderPane rootPane;
    @FXML
    private TreeView<Object> tree;
    ContactController contactController;
    Parent contactPane;
    private Contact editingContact;
    private Contact originalContact;
    private final ApplicationModel model = new ApplicationModel();
    private final Image contactIcon = new Image(String.valueOf(this.getClass().getResource("contact.png")));
    private final Image groupIcon = new Image(String.valueOf(this.getClass().getResource("group.png")));
    private TreeItem<Object> selectedItem;
    ListChangeListener<Group> groupListener;
    SetChangeListener<Contact> contactListener;

    public TD3Controller(){}


    @FXML
    private void initialize() {

        ContactControl cc = new ContactControl();
        contactController = cc.contactController;
        editingContact = contactController.editingContact;
        contactPane = cc.root;
        rootPane.setCenter(contactPane);
        this.groupListener = (change) -> {
            change.next();
            if (change.wasAdded()) {
                change.getAddedSubList().forEach((group) -> {
                    TreeItem<Object> groupItem = new TreeItem(group, new ImageView(this.groupIcon));
                    this.tree.getRoot().getChildren().add(groupItem);
                    this.fixSelectedElements(groupItem, (Contact)null);
                    group.contactsProperty().addListener(this.contactListener);
                    group.contactsProperty().forEach((contact) -> {
                        TreeItem<Object> contactItem = new TreeItem(contact, new ImageView(this.contactIcon));
                        groupItem.getChildren().add(contactItem);
                    });
                });
            } else if (change.wasRemoved()) {
                TreeItem<Object> parentItem = this.selectedItem.getParent();
                parentItem.getChildren().remove(this.selectedItem);
                this.fixSelectedElements(parentItem, (Contact)null);
            }

        };
        this.contactListener = (change) -> {
            TreeItem parent;
            if (this.selectedItem.getValue() instanceof Group) {
                parent = this.selectedItem;
            } else {
                parent = this.selectedItem.getParent();
            }

            Contact contact;
            if (change.wasAdded()) {
                contact = (Contact)change.getElementAdded();
                System.out.println("Control add: " + contact);
                TreeItem<Object> contactItem = new TreeItem(contact, new ImageView(this.contactIcon));
                parent.getChildren().add(contactItem);
                this.fixSelectedElements(contactItem, contact);
            } else if (change.wasRemoved()) {
                contact = (Contact)change.getElementRemoved();
                System.out.println("Control removed: " + contact);
                parent.getChildren().remove(this.selectedItem);
                this.fixSelectedElements(parent, (Contact)null);
            }

            this.tree.refresh();
        };
        this.model.groupsProperty().addListener(this.groupListener);
        this.editingContact.validateProperty().addListener((obs, oldv, newv) -> {
            if (newv) {
                this.validate();
            }


        });
        this.contactPane.visibleProperty().set(false);
        this.initializeTreeView();
    }
    @FXML
    public void validate() {
        Object g = this.selectedItem.getValue();
        Group group;
        if (g instanceof Contact) {
            group = (Group)this.selectedItem.getParent().getValue();
        } else {
            group = (Group)g;
        }

        if (this.originalContact != null) {
            this.model.updateExistingContact(this.originalContact, this.editingContact, group);
        } else if (this.originalContact == null) {
            this.model.addContact(this.editingContact, group);
        } else {
            this.tree.refresh();
        }
    }
    private void initializeTreeView(){
        TreeItem<Object> root = new TreeItem("Fichier de contacts");
        root.setExpanded(true);
        this.tree.setRoot(root);
        this.tree.setEditable(true);
        this.tree.setCellFactory((param) -> {
            return new TD3Controller.TextFieldTreeCellImpl();
        });
        this.tree.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                this.selectedItem = newValue;
                this.contactPane.visibleProperty().set(false);
                this.originalContact = null;
                if (this.selectedItem.getValue() instanceof Contact) {
                    this.contactPane.visibleProperty().set(true);
                    this.originalContact = (Contact)this.selectedItem.getValue();
                    this.startEditingOriginal();
                }

            }
        });



    }


    private void chartBindings() {
        this.groupPieChart.setData(this.model.groupPieChartDataProperty());
        this.contactBarChart.getXAxis().setLabel("Villes");
        this.contactBarChart.getYAxis().setLabel("Nb contacts");
        this.model.contactBarChartDataProperty().addListener((obsv, oldv, newv) -> {
            this.contactBarChart.getData().clear();
            this.contactBarChart.getData().add(newv);
        });
    }
    @FXML
    private void reloadGraphics() {
        this.model.reloadGraphics();
    }

    private void fixSelectedElements(TreeItem<Object> item, Contact c) {
        this.selectedItem = item;
        this.tree.getSelectionModel().select(this.selectedItem);
        this.originalContact = c;
    }
    public void startEditingOriginal() {
        this.originalContact.copyTo(this.editingContact);
    }

    @FXML
    public void addElement() {
        if (this.selectedItem != null) {
            if (this.selectedItem.getParent() == null) {
                this.model.addGroup();
            } else if (this.selectedItem.getValue() instanceof Group) {
                this.editingContact.init();
                this.contactPane.visibleProperty().set(true);
                this.originalContact = null;
            } else {
                if (this.selectedItem.getValue() instanceof Contact) {
                    this.editingContact.init();
                    this.contactPane.visibleProperty().set(true);
                    this.originalContact = null;
                }

            }
        }
    }

    @FXML
    public void removeElement() {
        if (this.selectedItem != null) {
            if (this.selectedItem.getValue() instanceof Group) {
                this.model.removeGroup((Group)this.selectedItem.getValue());
            } else if (this.selectedItem.getValue() instanceof Contact) {
                Group group = (Group)this.selectedItem.getParent().getValue();
                this.model.removeContact((Contact)this.selectedItem.getValue(), group);
                this.selectedItem.getParent().getChildren().remove(this.selectedItem);
            }

        }
    }



    @FXML
    private void openFile( ) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Constants.RESOURCE_FOLDER));
        File file = fileChooser.showOpenDialog((Window)null);
        if (file != null) {
            this.model.loadFile(file);
        }

    }

    @FXML
    public void saveFile() {
        this.model.save();
    }

    @FXML
    public void saveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Constants.RESOURCE_FOLDER));
        File file = fileChooser.showSaveDialog((Window)null);
        if (file != null) {
            this.model.saveAs(file);
        }

    }

    @FXML
    public void close() {
        this.model.save();
        System.exit(0);
    }
    private static class TextFieldTreeCellImpl extends TreeCell<Object> {
        private TextField textField;

        public TextFieldTreeCellImpl() {
        }

        public void startEdit() {
            super.startEdit();
            if (this.getTreeItem().getValue() instanceof Group) {
                if (this.textField == null) {
                    this.createTextField();
                }

                this.setText((String)null);
                this.setGraphic(this.textField);
                this.textField.selectAll();
            }
        }

        public void cancelEdit() {
            super.cancelEdit();
            this.setText(this.getItem() != null ? this.getItem().toString() : null);
            this.setGraphic(this.getTreeItem().getGraphic());
        }

        public void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                this.setText((String)null);
                this.setGraphic((Node)null);
            } else if (this.isEditing()) {
                if (this.textField != null) {
                    this.textField.setText(this.getString());
                }

                this.setText((String)null);
                this.setGraphic(this.textField);
            } else {
                this.setText(this.getString());
                this.setGraphic(this.getTreeItem().getGraphic());
            }

        }

        private void createTextField() {
            this.textField = new TextField(this.getString());
            this.textField.setOnKeyReleased((t) -> {
                if (t.getCode() == KeyCode.ENTER) {
                    ((Group)this.getTreeItem().getValue()).nameProperty().set(this.textField.getText());
                    this.commitEdit(this.getTreeItem().getValue());
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    this.cancelEdit();
                }

            });
        }

        private String getString() {
            return this.getItem() == null ? "" : this.getItem().toString();
        }
    }
}
