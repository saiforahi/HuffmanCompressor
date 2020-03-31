package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;

import classes.Compressor;
import classes.Data;
import classes.HuffmanNode;
import classes.Symbol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.StageStyle;

public class MainViewController {
	private Vector<Symbol> characters;
	private File selectedFile;
	@FXML
	private Pane input_pane,compress_pane;
	@FXML
	private Label type_label;
	@FXML
    private Button decompress_btn,compress_btn,file_choose_button,open_destination_btn;
	@FXML
	private TextField url_field;
	@FXML
	private TextArea output_text;
	@FXML
	private Label size_label,output_size,ratio_label;
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
			Runtime.getRuntime().exec("explorer.exe /select," + output_text.getText().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
    void decompress() {
    	if(this.selectedFile!=null && this.selectedFile.getName().substring(this.selectedFile.getName().lastIndexOf('.')).equalsIgnoreCase(".sas3")) {
			try {
	            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(this.selectedFile));
	            Data newData=(Data) objectIn.readObject();
	            objectIn.close();
	            regenerate_file(newData);
	            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
    
    public void regenerate_file(Data newData) {
    	System.out.println("Bitset:");
    	for(int index=0;index<newData.get_bits().length();index++) {
        	if(newData.get_bits().get(index)) {
        		System.out.print(1);
        	}
        	else {
        		System.out.print(0);
        	}
        }
    	System.out.println("\n");
    	String text="";
    	System.out.println(characters.size());
        String temp="";
        for(int index=0;index<newData.get_bits().length();index++) {
        	if(newData.get_bits().get(index)) {
                temp += "1";
            } else {
                temp += "0";
            }
        	for(Map.Entry<Character,String>m:newData.getCodeBook().entrySet()) {
        		if(m.getValue().equals(temp)) {
        			text+=m.getKey();
        			temp="";
        			break;
        		}
        	}
        }
        characters.clear();
        System.out.println(text);
        String fileName=this.selectedFile.getName().substring(0,this.selectedFile.getName().lastIndexOf('.'));
        try {
			BufferedWriter out = new BufferedWriter(new FileWriter(this.selectedFile.getParent()+"\\"+fileName+".txt"));
			out.write(text);
			out.close();
			this.selectedFile=null;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    
    /*public void make_codebook() {      //this function is implemented by following a brief description on https://www.geeksforgeeks.org/canonical-huffman-coding/
    	//System.out.println();
    	
    	Sorter.insertion_sort(inputs,inputs.length);
    	for(int index=0;index<inputs.length;index++) {
    		System.out.println((char)inputs[index].get_ascii_value()+"->"+inputs[index].get_path());
    	}
    	System.out.println();
    	for(int index=0;index<inputs.length;index++) {
    		if(index==0) {
    			String path="";
    			for(int index2=0;index2<inputs[index].get_path().length();index2++) {
    				path+="0";
    			}
    			inputs[index].set_path(path);
    		}
    		else if(inputs[index].get_path().length()>inputs[index-1].get_path().length()){
    			int difrnc=inputs[index].get_path().length()-inputs[index-1].get_path().length();
    			String path=Integer.toBinaryString(Integer.valueOf(inputs[index-1].get_path(),2)+ 1);
    			for(int index2=0;index2<difrnc;index2++) {
    				path+="0";
    			}
    			if(path.equalsIgnoreCase("1")) {
    				path="01";
    			}
    			inputs[index].set_path(path);
    		}
    		else {
    			String path=Integer.toBinaryString(Integer.valueOf(inputs[index-1].get_path(),2)+ 1);
    			if(path.equalsIgnoreCase("1")) {
    				path="01";
    			}
    			inputs[index].set_path(path);
    		}
    		System.out.println((char)inputs[index].get_ascii_value()+"->"+inputs[index].get_path());
    		
    	}
    }*/
} 