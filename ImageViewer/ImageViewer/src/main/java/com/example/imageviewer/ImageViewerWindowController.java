package com.example.imageviewer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageViewerWindowController extends Thread{
    private final List<File> images = new ArrayList<>();
    private int currentImageIndex = 0;
    private boolean paused;
    private boolean running;

    private SlideImage shownImage;

    @FXML
    Parent root;

    @FXML
    private Button stopButton;

    @FXML
    public Slider PictureSlider;

    @FXML
    private ImageView imageView;

    public ImageViewerWindowController()
    {
        paused = false;
        running = true;
    }

    @FXML
    private void handleBtnLoadAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty()) {
            files.forEach((File f) ->
            {
                images.addAll(files);
                displayImage();
            });
        }
    }

        @FXML
    private void handleBtnPreviousAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex =
                    (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    @FXML
    private void handleBtnNextAction()
    {
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();
        }
    }

    @FXML
    public void handleBtnStartSlideshow()
    {
       if(shownImage == null)
       {
           shownImage = new SlideImage(images, (int) PictureSlider.getValue(), currentImageIndex);
           shownImage.valueProperty().addListener((ov, oldImage, newImage) -> {
               imageView.setImage(newImage);
               if(newImage != oldImage){
                   currentImageIndex = shownImage.getIndex();
               }
           }
           );


           Thread usableThread = new Thread(shownImage);
           usableThread.setDaemon(true);
           usableThread.start();
       }
    }

    @FXML
    private void handleBtnStopSlideshow() throws InterruptedException {
    if (shownImage != null){
        currentImageIndex = shownImage.getIndex();
        shownImage.cancel();
        shownImage = null;
        }
    }
    @FXML
    private void shutItDown(){
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    private void displayImage()
    {
        if (!images.isEmpty())
        {
            imageView.setImage(new Image(images.get(currentImageIndex).toURI().toString()));
        }
    }
}