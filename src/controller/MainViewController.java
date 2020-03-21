package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Vector;

import classes.Symbol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainViewController {
	private File selectedFile;
	@FXML
    private Button file_choose_button;
	@FXML
    private Label input_label;
	@FXML
	private Button compress_btn;
    @FXML
    void openChooser(ActionEvent event) {
    	FileChooser fil_chooser = new FileChooser(); 
    	fil_chooser.setTitle("Select a text file"); 
    	fil_chooser.getExtensionFilters().addAll(new ExtensionFilter("text Files", "*.txt"));
    	selectedFile = fil_chooser.showOpenDialog(file_choose_button.getScene().getWindow());
    }
    @FXML
    void compress(ActionEvent event) {
		try {
			if(selectedFile!=null) {
	    		String details="Selected File size "+selectedFile.length()+" bytes\n"+"File path: "+selectedFile.getAbsolutePath();
	    		input_label.setText(details);
	    		String data;
	    		data = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
	    		System.out.println("File Size "+selectedFile.length()+" bytes");
				Vector<Symbol> characters=new Vector<Symbol>();
				for(int index=0;index<data.length();index++) {
					boolean found=false;
					for(int index2=0;index2<characters.size();index2++) {
						if(characters.get(index2).get_character()==data.charAt(index)) {
							characters.get(index2).increment_frequency(1);
							found=true;
						}
					}
					if(!found) {
						characters.add(new Symbol(data.charAt(index),1));
					}
				}
				Collections.sort(characters,Collections.reverseOrder());
				for(int index=0;index<characters.size();index++) {
					System.out.println(characters.get(index).get_ascii_value()+"->"+characters.get(index).get_frequency());
				}
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
    public void initialize() {
    	input_label.setText("No file selected");
    }

}
