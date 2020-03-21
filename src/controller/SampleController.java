package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class SampleController {

    @FXML
    private Pane pane;

    @FXML
    private Label name_label;

    @FXML
    private Button fileChoose;

    @FXML
    void openChooser(ActionEvent event) {

    }
    
    @FXML
    public void initialize() {
        System.out.println("second");
    }
    
    public SampleController() {
    	
    }
}
