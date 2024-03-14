package com.example.demo1;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;


public class HelloApplication extends Application {

    private static final int THUMBNAIL_SIZE = 100;
    private static final int DISPLAYED_IMAGES = 5;
    private static final int TOTAL_IMAGES = 15;
    private int currentImageIndex = 0;
    private ImageView selectedImageView;

    @Override
    public void start(Stage primaryStage) {
        BorderPane imageBox = createImageBox(DISPLAYED_IMAGES);
        StackPane stackPane = createImageViewPane();

        StackPane root = new StackPane(stackPane, imageBox);
        Scene scene = new Scene(root, 800, 600);

        // Applying CSS to the scene
        String cssPath = getClass().getResource("/styles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        primaryStage.setTitle("Image Display App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createImageBox(int displayCount) {
        BorderPane imageBox = new BorderPane();
        HBox thumbnailBox = new HBox();
        thumbnailBox.getStyleClass().add("thumbnail-box"); // Add style class
        thumbnailBox.setAlignment(Pos.CENTER);
        VBox buttonBox = new VBox();
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        for (int i = 1; i <= displayCount; i++) {
            String filename = "image" + i + ".jpg.jpg";
            try (InputStream inputStream = getClass().getResourceAsStream("/" + filename)) {
                if (inputStream != null) {
                    Image thumbnail = new Image(inputStream, THUMBNAIL_SIZE, THUMBNAIL_SIZE, true, true);
                    ImageView thumbnailImageView = new ImageView(thumbnail);
                    thumbnailImageView.getStyleClass().add("thumbnail-image"); // Add style class

                    final int currentIndex = i - 1;
                    thumbnailImageView.setOnMouseClicked(event -> displayFullSizeImage(currentIndex));

                    thumbnailBox.getChildren().add(thumbnailImageView);
                } else {
                    System.err.println("Could not load image: " + filename);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (displayCount < TOTAL_IMAGES) {
            Button showMoreButton = new Button("Show More >>>");
            showMoreButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    HelloApplication.this.showMoreImages(thumbnailBox, displayCount);
                }
            });
            buttonBox.getChildren().add(showMoreButton);
        }

        imageBox.setCenter(thumbnailBox);
        imageBox.setBottom(buttonBox);

        return imageBox;
    }

    private void showMoreImages(HBox thumbnailBox, int currentDisplayCount) {
        int remainingImages = TOTAL_IMAGES - currentDisplayCount;
        int imagesToDisplay = Math.min(remainingImages, DISPLAYED_IMAGES);

        for (int i = currentDisplayCount + 1; i <= currentDisplayCount + imagesToDisplay; i++) {
            String filename = "image" + i + ".jpg.jpg";
            try (InputStream inputStream = getClass().getResourceAsStream("/" + filename)) {
                if (inputStream != null) {
                    Image thumbnail = new Image(inputStream, THUMBNAIL_SIZE, THUMBNAIL_SIZE, true, true);
                    ImageView thumbnailImageView = new ImageView(thumbnail);
                    thumbnailImageView.getStyleClass().add("thumbnail-image"); // Add style class

                    final int currentIndex = i - 1;
                    thumbnailImageView.setOnMouseClicked(event -> displayFullSizeImage(currentIndex));

                    thumbnailBox.getChildren().add(thumbnailImageView);
                } else {
                    System.err.println("Could not load image: " + filename);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayFullSizeImage(int index) {
        currentImageIndex = index;
        String filename = "image" + (currentImageIndex + 1) + ".jpg.jpg";
        try (InputStream inputStream = getClass().getResourceAsStream("/" + filename)) {
            if (inputStream != null) {
                Image fullSizeImage = new Image(inputStream);
                selectedImageView.setImage(fullSizeImage);
                selectedImageView.setVisible(true);
            } else {
                System.err.println("Could not load full-size image: " + filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StackPane createImageViewPane() {
        selectedImageView = new ImageView();
        selectedImageView.setPreserveRatio(true);
        selectedImageView.setFitWidth(600);
        selectedImageView.setVisible(false);

        StackPane stackPane = new StackPane(selectedImageView);
        stackPane.setAlignment(Pos.CENTER);

        stackPane.getStyleClass().add("image-view-pane");

        return stackPane;
    }

    public static void main(String[] args) {
        launch();
    }
}
