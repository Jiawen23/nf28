package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.Model;

import java.io.File;

public class Controller {
    SepiaTone sepiaTone ;
    double sepiaLevel;
    @FXML
    private Label effect;
    @FXML
    private ImageView imageView;
    @FXML
    private MenuItem open;
    @FXML
    private Button before;
    @FXML
    private Button after;
    Model model;
   public  Controller(){

       initialize();

    }

    public void initialize(){
        this.model = new Model();
        this.open = new MenuItem();
        this.before = new Button();
        this.after = new Button();
        this.effect = new Label();
        this.sepiaTone = new SepiaTone();
        this.sepiaLevel = 0;
        //Image image = new Image();
        this.binding();

    }

    private void binding(){

        model.imageNameProperty().addListener((obs, oldVal, newVal) -> changeImage(newVal) );
        model.imNumProperty().addListener((obs, oldVal, newVal) -> changeImage2((Integer) newVal) );
        effect.setOnMouseReleased(event -> sepiaEffect());
        this.open.setOnAction( ( event ) ->  this.open());
        this.before.setOnAction(( event )->this.change2());
        this.after.setOnAction(( event )-> this.change1());


    }

    @FXML
    private void change1() {
        change("1");
    }
    @FXML
    private void change2() {
        change("-1");
    }


    private void change(String val) {
        System.out.println("i'm here");

           if (val.equals("1")) {
               if(this.model.getImNum() == (this.model.getImLen()-1)){
                   this.model.setImNum(0);

               }else{
                   this.model.setImNum(this.model.getImNum()+1);
               }

           } else if (val.equals("-1")) {
               if(this.model.getImNum() == 0){
                   this.model.setImNum(this.model.getImLen()-1);

               }else{
                   this.model.setImNum(this.model.getImNum()-1);
               }

           }


    }
    private void changeImage2(Integer newVal) {
        System.out.println(this.model.getImageNames().get(newVal));
        Image image = new Image("file:src/images/"+this.model.getImageNames().get(newVal));
        imageView.setImage(image);

    }
    private void changeImage(String newVal) {
        Image image = new Image(String.valueOf("file:"+newVal));
        imageView.setImage(image);

    }

    @FXML
    private void open() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            this.model.openWithFile(selectedFile);
        }
    }

    public void sepiaEffect() {
        this.sepiaLevel+=0.2;
        if(this.sepiaLevel> 1){
            this.sepiaLevel = 0 ;
        }
        sepiaTone.setLevel(sepiaLevel);
       imageView.setEffect(sepiaTone);

    }


}
