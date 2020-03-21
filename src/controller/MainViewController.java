package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

import classes.Symbol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainViewController {
	@FXML
    private Button file_choose_button;

    @FXML
    void openChooser(ActionEvent event) {
    	try {
    		FileChooser fil_chooser = new FileChooser(); 
        	fil_chooser.setTitle("Select a text file"); 
        	fil_chooser.getExtensionFilters().addAll(new ExtensionFilter("text Files", "*.txt"));
        	File selectedFile = fil_chooser.showOpenDialog(file_choose_button.getScene().getWindow());
        	String data="";
			data = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
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
					characters.add(new Symbol(data.charAt(index),0));
				}
			}
			System.out.println(characters.size());
			for(int index=0;index<characters.size();index++) {
				System.out.println(characters.get(index).get_character());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }

}
