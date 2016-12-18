package application;

import application.view.HomeViewController;
import application.view.ViewLoader;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Launches the application
 */
public class Launch extends Application{
    public static void main(String[] args)  {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewLoader.getInstance().setStage(primaryStage);
        new HomeViewController().showView();
    }
}
