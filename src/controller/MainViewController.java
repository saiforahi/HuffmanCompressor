package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    	//Runtime.getRuntime().exec("explorer.exe /select," + path);
    }
    @FXML
    void compress(ActionEvent event) {
		try {
			if(this.selectedFile!=null) {
	    		String data = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
				characters=new Vector<Symbol>();
				for(int index=0;index<data.length();index++) {
					boolean found=false;
					for(int index2=0;index2<characters.size();index2++) {
						if(characters.get(index2).get_character()==data.charAt(index)) {
							characters.get(index2).increment_frequency(1);
							found=true;
							break;
						}
					}
					if(!found) {
						characters.add(new Symbol(data.charAt(index),1));
					}
				}
				Collections.sort(characters,Collections.reverseOrder());
		        set_paths(make_huffman_tree(characters).element(),"");
		        generate_file(data);
	    	}
		} catch (IOException e) {
			e.printStackTrace();
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
    	Map<Integer,Integer> map=new HashMap<Integer,Integer>();
    	for(int index=0;index<characters.size();index++) {
    		map.put(characters.get(index).get_ascii_value(), characters.get(index).get_frequency());
    	}
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
    		Data d=new Data(map,bitSet);
    		String fileName=this.selectedFile.getName().substring(0,this.selectedFile.getName().lastIndexOf('.'));
            OutputStream os = new FileOutputStream(selectedFile.getParent()+"\\"+fileName+".sas3");
            ObjectOutputStream objectOut = new ObjectOutputStream(os);
            objectOut.writeObject(d);
            objectOut.close();
            //os.write(bitSet.toByteArray()); 
            os.close();
            characters.clear();
            output_text.setText(selectedFile.getParent()+"\\"+fileName+".sas3");
            output_size.setText("Size :"+new File(this.selectedFile.getParent()+"\\"+fileName+".sas3").length()+" bytes");
            showInformation("Alert","Completed");
        } 
        catch (Exception e) {
        	e.printStackTrace();
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
    
    public PriorityQueue<HuffmanNode> make_huffman_tree(Vector<Symbol> characters) {
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
		return queue;
    }
    
    public void regenerate_file() {
    	
    }
}
