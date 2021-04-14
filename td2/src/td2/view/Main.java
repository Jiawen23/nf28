package td2.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import td2.model.Constants;

import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/td2/view/td2view.fxml"));
        Parent root = (Parent)loader.load();
        //String url = "file:resources/icons/address_book.png";
       // stage.getIcons().add(new Image(url));
        stage.setTitle("TD02");
        stage.setScene(new Scene(root, (double) Constants.WIDTH, (double)Constants.HEIGHT, Color.BLACK));
        stage.show();
        } catch (Exception e) {

            // generic exception handling
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
