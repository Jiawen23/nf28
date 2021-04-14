package td3.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import td3.controller.TD3Controller;
import td3.model.Constants;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/td3/view/td3.fxml"));
            Parent root = (Parent)loader.load();
            TD3Controller controller=(TD3Controller)loader.getController();;
            //String url = "file:resources/icons/address_book.png";
            // stage.getIcons().add(new Image(url));
            stage.setTitle("TD03");
            stage.setScene(new Scene(root, (double) Constants.WIDTH, (double)Constants.HEIGHT, Color.BLACK));
            stage.show();
            stage.setOnCloseRequest((evt) -> {

                controller.saveFile();
            });
        } catch (Exception e) {

            // generic exception handling
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
