package assignment.vehicles;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ouda
 */
public class VehiclesController implements Initializable {

    @FXML
    private MenuBar mainMenu;
    @FXML
    private ImageView image;
    @FXML
    private BorderPane VehiclePortal;
    @FXML
    private Label title;
    @FXML
    private Label about;
    @FXML
    private Button play;
    @FXML
    private Button puase;
    @FXML
    private ComboBox size;
    @FXML
    private TextField name;
    Media media;
    MediaPlayer player;
    OrderedDictionary database = null;
    VehicleRecord vehicle = null;
    int vehicleSize = 1;

    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

    public void find() {
        DataKey key = new DataKey(this.name.getText(), vehicleSize);
        try {
            vehicle = database.find(key);
            showVehicle();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
    }

    public void delete() {
        VehicleRecord previousVehicle = null;
        try {
            previousVehicle = database.predecessor(vehicle.getDataKey());
        } catch (DictionaryException ex) {

        }
        VehicleRecord nextVehicle = null;
        try {
            nextVehicle = database.successor(vehicle.getDataKey());
        } catch (DictionaryException ex) {

        }
        DataKey key = vehicle.getDataKey();
        try {
            database.remove(key);
        } catch (DictionaryException ex) {
            System.out.println("Error in delete "+ ex);
        }
        if (database.isEmpty()) {
            this.VehiclePortal.setVisible(false);
            displayAlert("No more vehicles in the database to show");
        } else {
            if (previousVehicle != null) {
                vehicle = previousVehicle;
                showVehicle();
            } else if (nextVehicle != null) {
                vehicle = nextVehicle;
                showVehicle();
            }
        }
    }

    private void showVehicle() {
        play.setDisable(false);
        puase.setDisable(true);
        if (player != null) {
            player.stop();
        }
        String img = vehicle.getImage();
        Image vehicleImage = new Image("file:src/main/resources/assignment/vehicles/images/" + img);
        image.setImage(vehicleImage);
        title.setText(vehicle.getDataKey().getVehicleName());
        about.setText(vehicle.getAbout());
    }

    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/assignment/vehicles/images/UMIcon.png"));
            stage.setTitle("Dictionary Exception");
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

    public void getSize() {
        switch (this.size.getValue().toString()) {
            case "Aerial":
                this.vehicleSize = 1;
                break;
            case "Land":
                this.vehicleSize = 2;
                break;
            case "Water":
                this.vehicleSize = 3;
                break;
            default:
                break;
        }
    }

    public void first() {
        try {
            vehicle = database.smallest();
            showVehicle();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
    }

    public void last() {
    	try {
            vehicle = database.largest();
            showVehicle();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
    }

    public void next() {
    	if (vehicle != null) {
            try {
                DataKey key = vehicle.getDataKey();
                VehicleRecord nextVehicle = database.successor(key);
                if (nextVehicle != null) {
                    vehicle = nextVehicle;
                    showVehicle();
                } else {
                    displayAlert("No more vehicles with larger keys in the database");
                }
            } catch (DictionaryException ex) {
                displayAlert(ex.getMessage());
            }
        }
    }

    public void previous() {
    	if (vehicle != null) {
            try {
                DataKey key = vehicle.getDataKey();
                VehicleRecord previousVehicle = database.predecessor(key);
                if (previousVehicle != null) {
                    vehicle = previousVehicle;
                    showVehicle();
                } else {
                    displayAlert("No more vehicles with smaller keys in the database");
                }
            } catch (DictionaryException ex) {
                displayAlert(ex.getMessage());
            }
    	}
    }

    public void play() {
        String filename = "src/main/resources/assignment/vehicles/sounds/" + vehicle.getSound();
        media = new Media(new File(filename).toURI().toString());
        player = new MediaPlayer(media);
        play.setDisable(true);
        puase.setDisable(false);
        player.play();
    }

    public void puase() {
        play.setDisable(false);
        puase.setDisable(true);
        if (player != null) {
            player.stop();
        }
    }

    public void loadDictionary() {
        Scanner input;
        int line = 0;
        try {
            String vehicleName = "";
            String description;
            int size = 0;
            input = new Scanner(new File("VehiclesDatabase.txt"));
            while (input.hasNext()) // read until  end of file
            {
                String data = input.nextLine();
                switch (line % 3) {
                    case 0:
                        size = Integer.parseInt(data);
                        break;
                    case 1:
                        vehicleName = data;
                        break;
                    default:
                        description = data;
                        database.insert(new VehicleRecord(new DataKey(vehicleName, size), description, vehicleName + ".mp3", vehicleName + ".jpg"));
                        break;
                }
                line++;
            }
        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: VehiclesDatabase.txt");
            System.out.println(e.getMessage());
        } catch (DictionaryException ex) {
            Logger.getLogger(VehiclesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.VehiclePortal.setVisible(true);
        this.first();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = new OrderedDictionary();
        size.setItems(FXCollections.observableArrayList(
                "Aerial", "Land", "Water"
        ));
        size.setValue("Aerial");
    }

}
