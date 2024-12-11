package at.htlleonding.imagediscovery;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ImageDiscoveryApplication extends Application {

    private Label fileNameLabel;
    private ImageView imageView;
    private BorderPane borderPane;
    private Scene scene;
    private Stage stage;
    private ImageHandler imageHandler;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        // Create a BorderPane as the root layout
        borderPane = new BorderPane();

        // Create an ImageView for the center area
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        BorderPane.setMargin(imageView, new Insets(5));
        borderPane.setCenter(imageView);

        // Create a FlowPane for the bottom area
        FlowPane flowPane = new FlowPane();
        flowPane.setHgap(10); // Horizontal gap between components

        // Add buttons to the FlowPane
        Button loadButton = new Button("Load Image");
        loadButton.setOnAction((ActionEvent event) -> loadImage(event));
        flowPane.getChildren().add(loadButton);

        //start Button

        Button startButton = new Button("Start");
        startButton.setOnAction((ActionEvent event) -> startDiscover(event));
        flowPane.getChildren().add(startButton);

        //pause Button

        Button pauseButton = new Button("pause");
        pauseButton.setOnAction((ActionEvent event) -> pauseDiscover(event));
        flowPane.getChildren().add(pauseButton);

        // Add a label to the FlowPane
        fileNameLabel = new Label("Please load an image ...");
        flowPane.getChildren().add(fileNameLabel);
        borderPane.setBottom(flowPane);
        BorderPane.setMargin(flowPane, new Insets(5));

        // Load an example initial image into the image view
        File initialImageFile = new File("./house.jpg");
        setImage(initialImageFile);

        // Create a Scene and set it in the Stage
        scene = new Scene(borderPane);
        stage.setTitle("JavaFX Application Without FXML");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    private void pauseDiscover(ActionEvent event) {
        imageHandler.pause();
    }

    private void startDiscover(ActionEvent event) {
        imageHandler.start();
    }

    private void loadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File currentDir = new File(".");
        fileChooser.setInitialDirectory(currentDir);
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
        File imageFile = fileChooser.showOpenDialog(null);
        setImage(imageFile);
    }

    private void setImage(File imageFile) {
        //fileNameLabel.setText(imageFile.getName());
        imageHandler = new ImageHandler(imageFile);
        imageView.setImage(imageHandler.getImage());
        imageView.setFitWidth(imageHandler.getImage().getWidth());
        imageView.setFitHeight(imageHandler.getImage().getHeight());
        // resize window to new image
        this.stage.sizeToScene();
    }

    public static void main(String[] args) {
        launch();
    }
}
