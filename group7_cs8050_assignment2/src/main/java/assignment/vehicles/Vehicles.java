package assignment.vehicles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Ouda
 */
public class Vehicles extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("vehicles-view.fxml"));

        Scene scene = new Scene(root);

        stage.getIcons().add(new Image("file:src/main/resources/assignment/vehicles/images/UMIcon.png"));
        stage.setTitle("Vehicles Portal");

        stage.setScene(scene);
        stage.show();


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
