package application.controller;

import application.model.ImageConverter;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Controller {
    private static final Controller onlyInstance = new Controller();

    private Controller(){}

    public static Controller getInstance() {
        return onlyInstance;
    }

    public void convert(List<File> originalImages, String savePath, int newWidth, int newHeight) throws IOException {
        new ImageConverter().convertAndSave(originalImages, savePath, newWidth, newHeight);
    }
}
