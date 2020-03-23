package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Vector;
import classes.HuffmanNode;
import classes.Symbol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.StageStyle;

public class MainViewController {
	private Vector<Symbol> characters;
	private File selectedFile;
	@FXML
    private Button file_choose_button;
	@FXML
	private Button compress_btn;
	@FXML
	private TextField url_field;
	@FXML
	private Label size_label;
    @FXML
    void openChooser(ActionEvent event) {
    	FileChooser fil_chooser = new FileChooser(); 
    	fil_chooser.setTitle("Select a text file"); 
    	fil_chooser.getExtensionFilters().addAll(new ExtensionFilter("text Files", "*.txt"));
    	selectedFile = fil_chooser.showOpenDialog(file_choose_button.getScene().getWindow());
    	url_field.setText(selectedFile.getAbsolutePath());
    	size_label.setText("Size: "+selectedFile.length()+" bytes");
    }
    @FXML
    void compress(ActionEvent event) {
		try {
			if(this.selectedFile!=null) {
	    		
	    		String data = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
	    		System.out.println("File Size "+selectedFile.length()+" bytes");
				characters=new Vector<Symbol>();
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
				PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>(characters.size());  	// from this line to line number 91 is took from 																			
				for (int index = 0; index < characters.size(); index++) {								// https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/
		            HuffmanNode hn = new HuffmanNode();
		            hn.askii = characters.get(index).get_ascii_value();
		            hn.frequency_value = characters.get(index).get_frequency();
		            hn.left = null; 
		            hn.right = null;  
		            queue.add(hn); 
		        } 
		        // Here we will extract the two minimum value 
		        // from the heap each time until 
		        // its size reduces to 1, extract until 
		        // all the nodes are extracted. 
		        while (queue.size() > 1) {
		            // first min extract. 
		            HuffmanNode xtemp1 = queue.peek(); 
		            queue.poll();
		            // second min extarct. 
		            HuffmanNode ytemp2 = queue.peek(); 
		            queue.poll();
		            // new node f which is equal 
		            HuffmanNode ztemp3 = new HuffmanNode(); 
		            // to the sum of the frequency of the two nodes 
		            // assigning values to the f node. 
		            ztemp3.frequency_value = xtemp1.frequency_value + ytemp2.frequency_value; 
		            ztemp3.askii = 0; 
		            // first extracted node as left child. 
		            ztemp3.left = xtemp1; 
		            // second extracted node as the right child. 
		            ztemp3.right = ytemp2; 
		            // add this node to the priority-queue. 
		            queue.add(ztemp3);
		        }
		        set_paths(queue.element(),"");
		        generate_file(data);
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
    public void initialize() {
    	size_label.setText("Size: ");
    	url_field.textProperty().addListener((obs, oldText, newText) -> {
    		if(!newText.equals(null) && new File(url_field.getText()).exists()) {
    			this.selectedFile=new File(url_field.getText());
        		size_label.setText("Size: "+this.selectedFile.length()+" bytes");
        	}
        	else {
        		size_label.setText("Not a file");
        	}
    	});
    }
    public void set_paths(HuffmanNode node,String s) {
    	if(node.left == null && node.right == null) {
    		for(int index=0;index<this.characters.size();index++) {
    			if(this.characters.get(index).get_ascii_value()==node.askii) {
    				this.characters.get(index).set_path(s);
    				break;
    			}
    		}
    		return;
    	}
    	set_paths(node.left,s+"0");
    	set_paths(node.right,s+"1");
    }
    public void generate_file(String data) {
    	String codes="";
    	for(int index=0;index<data.length();index++) {
    		for(int index2=0;index2<characters.size();index2++) {
    			if(characters.get(index2).get_character()==data.charAt(index)) {
    				codes+=characters.get(index2).get_path();
    			}
    		}
    	}
    	BitSet bitSet = new BitSet(codes.length());
    	int bitcounter = 0;
    	for(Character c : codes.toCharArray()) {
    	    if(c.equals('1')) {
    	        bitSet.set(bitcounter);
    	    }
    	    bitcounter++;
    	}
    	try { 
            OutputStream os = new FileOutputStream(selectedFile.getParent()+"\\compressed.sas3"); 
            os.write(bitSet.toByteArray()); 
            System.out.println("Successfully"+ " byte inserted\n"+Arrays.toString(bitSet.toByteArray())); 
            os.close();
            showInformation("Alert","Completed");
        } 
        catch (Exception e) { 
        	showInformation("Error", e.getMessage());
        } 
    }
    
    public static void showInformation(String title, String message) {
    	//Runtime.getRuntime().exec("explorer.exe /select," + path);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
