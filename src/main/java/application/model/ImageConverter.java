package application.model;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class uses Thumbnailator library to convert images
 * @author WolfAlexander
 */
public class ImageConverter {

    /**
     * Converts list of images to given parameters and saves in given location
     * @param originalImages - list of images to be converted
     * @param savePath - path to location where converted images will be stored
     * @param newWidth - width of converted images
     * @param newHeight - height of converted images
     * @throws IOException in case if during conversion or saving error occur
     * @throws IllegalArgumentException in case any parameter are in illegal form
     */
    public void convertAndSave(List<File> originalImages, String savePath, int newWidth, int newHeight) throws IOException, IllegalArgumentException{
        checkParameters(originalImages, savePath, newWidth, newHeight);

        for (File image : originalImages){
            BufferedImage convertedImage = convert(image, newWidth, newHeight);
            String newImageName = "converted" + image.getName();
            save(convertedImage, savePath, newImageName);
        }
    }

    /**
     * Checks specific order if parameters given to convertAndSave() method are valid
     * @param originalImages - should be list of files
     * @param savePath - should be a non-empty string path
     * @param newWidth - should be an Integer bigger than 0
     * @param newHeight - should be an integer bigger than 0
     * @throws IllegalArgumentException - in case any parameter is illegal
     */
    private void checkParameters(List<File> originalImages, String savePath,  int newWidth, int newHeight) throws IllegalArgumentException{
        if(originalImages == null)
            throw new IllegalArgumentException("No images were given for conversion!");
        else if(savePath.isEmpty())
            throw new IllegalArgumentException("Path to saving location cannot be empty!");
        else if(newWidth < 0)
            throw new IllegalArgumentException("Width of a new image cannot be less than 0!");
        else if(newHeight < 0)
            throw new IllegalArgumentException("Height of a new image cannot be less than 0!");
        else if(newWidth == 0 && newHeight == 0)
            throw new IllegalArgumentException("Both width and height cannot be 0!");
    }


    /**
     * Converts given image depending on which parameters are given
     * @param image - image that will be converted
     * @param newWidth - new width of the image
     * @param newHeight - new height of the image
     * @return converted image
     * @throws IOException in case if during conversion error occur
     */
    private BufferedImage convert(File image, int newWidth, int newHeight) throws IOException{
        if(onlyWidthGiven(newWidth, newHeight))
            return convertUsingGivenWidthAutoHeight(image, newWidth);
        else if(onlyHeightGiven(newWidth, newHeight))
            return convertUsingGivenHeightAutoWidth(image, newHeight);
        else
            return convertUsingGivenWidthAndHeight(image, newWidth, newHeight);
    }

    /**
     * Checks if only valid width (>0) given to the converter
     * @param width - given width
     * @param height - given height
     * @return true if the only valid parameter is width
     */
    private boolean onlyWidthGiven(int width, int height){
        return width > 0 && height == 0;
    }

    /**
     * Checks if only valid height (>0) is given to the converter
     * @param width - given width
     * @param height - given height
     * @return true if the only valid parameter is height
     */
    private boolean onlyHeightGiven(int width, int height){
        return height > 0 && width == 0;
    }

    /**
     * This method will convert image by using given width and automatically adjust height
     * @param originalImage - image that will be converted
     * @param width - wanted width of the converted image
     * @return converted image
     * @throws IOException in case conversion fails
     */
    private BufferedImage convertUsingGivenWidthAutoHeight(File originalImage, int width) throws IOException {
        return Thumbnails.of(originalImage).width(width).asBufferedImage();
    }

    /**
     * This method will convert image by using given width and automatically adjust height
     * @param originalImage - image that will be converted
     * @param height - wanted height of the converted image
     * @return converted image
     * @throws IOException in case conversion fails
     */
    private BufferedImage convertUsingGivenHeightAutoWidth(File originalImage, int height) throws IOException {
        return Thumbnails.of(originalImage).height(height).asBufferedImage();
    }

    /**
     * Converts image by using both given width and height
     * @param originalImage - image that will be converted
     * @param width - wanted width of the converted image
     * @param height - wanted height of the converted image
     * @return converted image
     * @throws IOException in case conversion fails
     */
    private BufferedImage convertUsingGivenWidthAndHeight(File originalImage, int width, int height) throws IOException {
        return Thumbnails.of(originalImage).width(width).height(height).asBufferedImage();
    }

    /**
     * Saves image in given location with given name
     * @param image - image to be saved
     * @param savePath - location to where image will be saved
     * @param imageName - name that will be given to the image
     * @throws IOException in case if image cannot be saved
     */
    private void save(BufferedImage image, String savePath, String imageName) throws IOException {
        File newImagePath = new File(savePath + "\\" + imageName);
        ImageIO.write(image, "jpg", newImagePath);
    }
}
