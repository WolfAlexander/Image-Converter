package application.view;

import application.controller.Controller;

/**
 * This class should be extended by all view controllers
 */
abstract class ViewController {
    protected final Controller controller = Controller.getInstance();

    /**
     * Implementation of this method will be showing a view to the screen
     */
    abstract void showView();
}
