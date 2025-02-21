package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import classes.Compressor;
import classes.Decompressor;
import classes.Symbol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainViewController {
	private Vector<Symbol> characters;
	private File selectedFile;
	@FXML
	private Pane input_pane,compress_pane;
	@FXML
	private Label type_label;
	@FXML
    private Button decompress_btn,compress_btn,file_choose_button,open_destination_btn,open_destination_btn1;// && output.exists()
	@FXML
	private TextField url_field;
	@FXML
	private TextArea output_text,output_text1;
	@FXML
	private Label size_label,de_size,output_size,ratio_label;
    @FXML
    void openChooser(ActionEvent event) {
    	FileChooser fil_chooser = new FileChooser(); 
    	fil_chooser.setTitle("Select a text file"); 
    	fil_chooser.getExtensionFilters().addAll(new ExtensionFilter("Files", "*.txt","*.sas3"));
    	selectedFile = fil_chooser.showOpenDialog(file_choose_button.getScene().getWindow());
    	url_field.setText(selectedFile.getAbsolutePath());
    }
    @FXML
    void open_destination(ActionEvent event) {
    	try {
    		if (Desktop.isDesktopSupported() && this.selectedFile!=null) {
    			 File file = new File(this.selectedFile.getParent());
    			 Desktop desktop = Desktop.getDesktop();
    	         desktop.open(file);
    	      }
    		
			//Runtime.getRuntime().exec("explorer.exe /select," + output_text.getText().substring(0,output_text.getText().lastIndexOf('.')));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
    void decompress() {
    	Decompressor.decompress(selectedFile);
    	String fileName=this.selectedFile.getName().substring(0,this.selectedFile.getName().lastIndexOf('.'));
    	File output=new File(selectedFile.getParent()+"\\"+fileName+".txt");
    	if(output.exists() && output.length()>0) {
    		output_text1.setText(selectedFile.getParent()+"\\"+fileName+".txt");
    		de_size.setText("Size :"+new File(selectedFile.getParent()+"\\"+fileName+".txt").length()+" bytes");
    	}
    	
    }
    @FXML
    void compress(ActionEvent event) {
    	Compressor.compress(characters, selectedFile);
    	String fileName=this.selectedFile.getName().substring(0,this.selectedFile.getName().lastIndexOf('.'));
    	File output=new File(selectedFile.getParent()+"\\"+fileName+".sas3");
    	if(output.exists() && output.length()>0) {
    		output_text.setText(selectedFile.getParent()+"\\"+fileName+".sas3");
            output_size.setText("Size :"+new File(selectedFile.getParent()+"\\"+fileName+".sas3").length()+" bytes");
    	}
    	
    }
    
    
    @FXML
    public void initialize() {
    	size_label.setText("Size: ");
    	//input_pane.setBorder(new Border(new BorderStroke(Color.DARKRED,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    	url_field.textProperty().addListener((obs, oldText, newText) -> {
    		if(!newText.equals(null) && new File(url_field.getText()).exists()) {
    			this.selectedFile=new File(url_field.getText());
        		size_label.setText("Size: "+this.selectedFile.length()+" bytes");
        		if(this.selectedFile.getName().substring(this.selectedFile.getName().lastIndexOf('.')).equalsIgnoreCase(".txt")) {
        			compress_pane.setDisable(false);
        			decompress_btn.setDisable(true);
        			type_label.setText("type: Not Compressed");
        		}
        		else if(this.selectedFile.getName().substring(this.selectedFile.getName().lastIndexOf('.')).equalsIgnoreCase(".sas3")) {
        			compress_pane.setDisable(true);
        			decompress_btn.setDisable(false);
        			type_label.setText("type: Compressed");
        		}
        	}
        	else {
        		size_label.setText("Not a file");
        	}
    	});
    }
    
} 