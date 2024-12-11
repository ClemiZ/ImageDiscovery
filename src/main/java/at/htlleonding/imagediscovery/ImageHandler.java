package at.htlleonding.imagediscovery;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ImageHandler extends Thread{

    private Image sourceImage;
    private final WritableImage targetImage;
    private final PixelReader sourcePixelReader;
    private final PixelWriter targetPixelWriter;

    private volatile boolean isPaused = false;
    private final Lock lock = new ReentrantLock();
    private final Condition pauseCondition = lock.newCondition();


    public ImageHandler(File imageFile) {
        sourceImage = new Image(imageFile.toURI().toString());
        targetImage = new WritableImage(
                (int) sourceImage.getWidth(),
                (int) sourceImage.getHeight());
        sourcePixelReader = sourceImage.getPixelReader();
        targetPixelWriter = targetImage.getPixelWriter();
        initializeTargetImage();
    }

    public void discover() {

        lock.lock();
        try {
            while (isPaused) {
                pauseCondition.await(); // Pauses the thread
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        
        for (int i = 0; i < targetImage.getWidth(); i++) {
            final int async_x = i;
            Platform.runLater(() -> {
                for (int j = 0; j < targetImage.getHeight(); j++) {
                    Color color = sourcePixelReader.getColor(async_x,j);
                    targetPixelWriter.setColor(async_x, j, color);
                }
            });

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initializeTargetImage(){
        for (int i = 0; i < targetImage.getWidth(); i++) {
            for (int j = 0; j < targetImage.getHeight(); j++) {
                Color color = new Color(Math.random()
                                        , Math.random()
                                        , Math.random()
                                        , 1);
                targetPixelWriter.setColor(i, j, color);
            }
        }

    }
    @Override
    public void run() {
        this.discover();
    }

    public Image getImage() {
        return targetImage;
    }

    public void pause() {
        synchronized (lock){
            isPaused = true;
            lock.lock();
        }
    }
    public void resume(){
        synchronized (lock){
            isPaused = false;
            lock.notify();
        }

    }
}
