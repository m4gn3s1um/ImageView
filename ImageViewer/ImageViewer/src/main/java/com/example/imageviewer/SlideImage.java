package com.example.imageviewer;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import java.io.File;
import java.util.List;

public class SlideImage extends Task<Image>{

    private final List<File> images;

    private int time;

    private int index;

    public SlideImage(List<File> images, int time, int index){
        this.images = images;
        this.time = time;
        this.index = index;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getIndex() {
        return index;
    }

    @Override
    protected Image call() throws Exception {

            while (!isCancelled()) {
                Image slideshowImage = new Image(images.get(index).toURI().toString());
                updateValue(slideshowImage);

             try {
                    Thread.sleep(time * 1000L);
                } catch (InterruptedException i) {
                 if (isCancelled()) {
                        break;
                 }
              }
             index = (index + 1) % images.size();
            }
            return null;
    }
}
