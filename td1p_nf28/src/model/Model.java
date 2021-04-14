package model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Model {


    StringProperty imageName;
    List<String> imageNames;
    IntegerProperty imNum;
    public List<String> getImageNames() {
        return imageNames;
    }

    public void setImageNames(List<String> imageNames) {
        this.imageNames = imageNames;
    }

    public void setImageName(String imageName) {
        this.imageName.set(imageName);
    }
    public int getImNum() {
        return imNum.getValue();
    }
    public int getImLen(){
        return imageNames.size();
    }
    public void setImNum(int imNum) {
        this.imNum.setValue(imNum);
    }

    public IntegerProperty imNumProperty() {return imNum; }
    public StringProperty imageNameProperty() { return imageName; }
    public Model(){
        initialize();
        setImageNames();

    }
    public void initialize(){
        imageName=new SimpleStringProperty();
        imNum = new SimpleIntegerProperty();
        imageName.setValue("DSC03297");
        imNum.setValue(0);
    }
    public void setImageNames(){
        File f = new File("src/images");
        imageNames = Arrays.asList(f.list());
//        for(String i : imageNames) {
//            System.out.println(i);
//        }
    }
    public void open(File imageFile){

        imageName.setValue(imageFile.getName());
        System.out.println(imageFile.getName());
    }
    public void openWithFile(File imageFile){

        imageName.setValue(imageFile.getAbsolutePath().toString());
        System.out.println(imageFile.getAbsolutePath().toString());
    }

}
