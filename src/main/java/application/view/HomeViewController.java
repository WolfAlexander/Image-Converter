package application.view;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Controls interaction with home_view and model controller
 */
public class HomeViewController extends ViewController{
    private final FileChooser fileChooser = new FileChooser();
    private HashMap<String, File> mapOfImages = new HashMap<>();

    @FXML private CheckBox widthCheckbox;
    @FXML private CheckBox heightCheckbox;
    @FXML private TextField newWidthInput;
    @FXML private TextField newHeightInput;
    @FXML private Label messageToUser;

    @FXML private TableView tableOfImages;
    @FXML private TableColumn deleteColumn;
    @FXML private TableColumn imageNameColumn;

    @FXML private Button convertBttn;
    @FXML private Button chooseFilesBttn;

    /**
     * Shows this view
     */
    @Override
    public void showView() {
        ViewLoader.getInstance().showView(ViewLoader.ViewType.HOME, this);
    }

    /**
     * This method gets called when user clicks a button to chose images(files)
     * This will give user a way to choose file and show chosen files on the screen
     */
    @FXML
    public void chooseFiles(){
        List<File> chosenImages = fileChooser.showOpenMultipleDialog(ViewLoader.getInstance().getStage());

        if(chosenImages != null)
            chosenImages.forEach(ci->{mapOfImages.put(ci.getName(), ci); });

        updateImageTable();
    }

    /**
     * This method gets called when user clicks convert button
     * Initiates conversion process
     */
    @FXML
    public void convert(){
        try{
            int width = 0;
            int height = 0;

            if(!newWidthInput.getText().isEmpty())
                width = Integer.parseInt(newWidthInput.getText());

            if(!newHeightInput.getText().isEmpty())
                height = Integer.parseInt(newHeightInput.getText());

            if(areImagesChosen()){
                new Thread(new ConversionTask(new ArrayList<File>(mapOfImages.values()), width, height)).start();
            }else
                messageToUser.setText("No images are chosen!");
        }catch (NumberFormatException ex){
            messageToUser.setText("Dimensions has to be a number!");
        }
    }

    /**
     * Checks of user have chosen any images
     * @return true if user have chose images
     */
    private boolean areImagesChosen(){
        return mapOfImages != null && mapOfImages.size() > 0;
    }

    /**
     * This gets called when width checkbox is selected
     * This will show a field for user to enter a width
     */
    @FXML
    public void widthCheckboxChecked(){
        if(widthCheckbox.isSelected())
            newWidthInput.setVisible(true);
        else
            newWidthInput.setVisible(false);

    }

    /**
     * This gets called when height checkbox is selected
     * This will show a field for user to enter a height
     */
    @FXML
    public void heightCheckboxChecked(){
        if(heightCheckbox.isSelected())
            newHeightInput.setVisible(true);
        else
            newHeightInput.setVisible(false);
    }

    /**
     * This is a task that will perform conversion by calling business logic controller
     */
    private class ConversionTask extends Task{
        private List<File> listOfImages;
        private int newWidth;
        private int newHeight;

        /**
         * Creates conversion task
         * @param listOfImages - list of images that will be converted
         * @param newWidth - new width of the images
         * @param newHeight - new height of the images
         */
        ConversionTask(List<File> listOfImages, int newWidth, int newHeight) {
            this.listOfImages = listOfImages;
            this.newWidth = newWidth;
            this.newHeight = newHeight;
        }

        /**
         * Execution of the task
         */
        @Override
        protected Object call() throws Exception {
            try {


                preExecProcedures();
                controller.convert(listOfImages, "C:\\Users\\Alexander\\Desktop\\image\\small", newWidth, newHeight);
                postExecProcedures();
            } catch (IOException e) {
                showUserMessage(e.getMessage());
            }

            return null;
        }

        /**
         * Procedures to do before calling model
         */
        private void preExecProcedures(){
            showUserMessage("Converting...");
            disableButtons();
        }

        /**
         * Procedures to do after successful execution
         */
        private void postExecProcedures(){
            enableButtons();
            mapOfImages.clear();
            updateImageTable();
            heightCheckbox.setSelected(false);
            heightCheckboxChecked();
            widthCheckbox.setSelected(false);
            widthCheckboxChecked();
            newWidthInput.setText("");
            newHeightInput.setText("");

            showUserMessage("Conversion completed!");
        }

        /**
         * Shows message in the screen
         * @param message - message to be shown
         */
        private void showUserMessage(String message){
            Platform.runLater(()->{
                messageToUser.setText(message);
            });
        }

        /**
         * Control buttons become disabled
         */
        private void disableButtons() {
            Platform.runLater(() -> {
                convertBttn.setDisable(true);
                chooseFilesBttn.setDisable(true);
            });
        }

        /**
         * Control buttons become enabled
         */
        private void enableButtons(){
            Platform.runLater(()->{
               convertBttn.setDisable(false);
               chooseFilesBttn.setDisable(false);
            });
        }
    }

    /**
     * Updates table of images
     */
    private void updateImageTable(){
        ObservableList tableData = FXCollections.observableArrayList();

        mapOfImages.forEach((k,v)->{
            tableData.add(new ImageTableData(k));
        });

        deleteColumn.setCellValueFactory(
                new PropertyValueFactory<ImageTableData, String>("imageName")
        );

        deleteColumn.setCellFactory(
                (t) -> new DeleteButtonCell()
        );

        imageNameColumn.setCellValueFactory(
                new PropertyValueFactory<ImageTableData, String>("imageName")
        );

        tableOfImages.getItems().setAll(tableData);
    }

    /**
     * Special class that represents one image in the table
     */
    public static class ImageTableData{
        private final SimpleStringProperty imageName;

        public ImageTableData(String imageName) {
            this.imageName = new SimpleStringProperty(imageName);
        }

        public String getImageName() {
            return imageName.get();
        }

        public SimpleStringProperty imageNameProperty() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName.set(imageName);
        }
    }

    /**
     * A delete button that will be shown in the image table for each image
     */
    private class DeleteButtonCell extends TableCell{
        private Button deleteButton;

        public DeleteButtonCell() {
            this.deleteButton = new Button("Delete");
            this.deleteButton.getStyleClass().add("cell-button");
        }

        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            this.deleteButton.setOnAction((e)->{
                mapOfImages.remove(item);
                updateImageTable();
            });

            if(!empty)
                setGraphic(deleteButton);
            else
                setGraphic(null);
        }
    }
}