package application.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * This singleton class that handles loading and setting different views to the application screen
 */
public class ViewLoader {
    private HashMap<ViewType, URL> viewToFXML = new HashMap<>();
    private static ViewLoader viewLoader = new ViewLoader();
    private Stage stage;
    private final int PRIMARY_STAGE_WIDTH = 1024;
    private final int PRIMARY_STAGE_HEIGHT = 756;

    /**
     * Different types views that will be used for identifying views
     */
    public enum ViewType{
        HOME
    }

    /**
     * Private constructor to make this class an singleton object
     * Also initializes needed properties
     */
    private ViewLoader(){
        viewToFXML.put(ViewType.HOME, getClass().getResource("/home_view.fxml"));
    }

    /**
     * @return the only instance of this class
     */
    public static ViewLoader getInstance(){
        return viewLoader;
    }

    /**
     * Sets a stage that scenes should be shown at
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * This method start sequence of other methods to show right view on the screen
     * @param view - type of the view
     * @param controller controller that will handle actions made on the view
     */
    public void showView(ViewType view, ViewController controller){
        showScene(getScene(view, controller, PRIMARY_STAGE_WIDTH, PRIMARY_STAGE_HEIGHT));
    }

    public Stage getStage(){
        return stage;
    }

    /**
     * Loads a view
     * @param view - view type that maps to the location of the scene
     * @return Scene - the view
     */
    private Scene getScene(ViewType view, ViewController controller, int sceneWidth, int sceneHeight){
        try {
            FXMLLoader loader = new FXMLLoader(viewToFXML.get(view));
            setController(loader, controller);
            Scene scene = new Scene(loader.load(), sceneWidth, sceneHeight);
            scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toString());
            return scene;
        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Adds view to the primary window
     * @param scene - the scene that is should be added to stage
     */
    private void showScene(Scene scene){
        this.stage.setScene(scene);

        if(!this.stage.isShowing())
            this.stage.show();
    }

    private void setController(FXMLLoader loader, final ViewController controller){
        loader.setControllerFactory(param -> controller);
    }
}
