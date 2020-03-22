package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Vector;
import classes.HuffmanNode;
import classes.Symbol;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainViewController {
	public static String path;
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
    	String details="Selected File size "+selectedFile.length()+" bytes\n"+"File path: "+selectedFile.getAbsolutePath();
		input_label.setText(details);
    }
    @FXML
    void compress(ActionEvent event) {
		try {
			if(this.selectedFile!=null) {
	    		
	    		String data = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
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
				PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>(characters.size());  	// from this line to line number 91 is took from 																			
				for (int index = 0; index < characters.size(); index++) {								// https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/
		            HuffmanNode hn = new HuffmanNode();
		            hn.askii = characters.get(index).get_ascii_value();
		            hn.frequency_value = characters.get(index).get_frequency();
		            hn.left = null; 
		            hn.right = null;  
		            queue.add(hn); 
		        } 
		        HuffmanNode root = null; 
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
		            // marking the f node as the root node. 
		            root = ztemp3; 
		            // add this node to the priority-queue. 
		            queue.add(ztemp3);
		        }
				for(int index=0;index<characters.size();index++) {
					get_path(queue.element(),characters.get(index).get_character(),"");
					characters.get(index).set_path(MainViewController.path);
				}
				for(int index=0;index<characters.size();index++) {
					System.out.println(characters.get(index).get_character()+"->"+characters.get(index).get_path());
				}
				//byte[] byteArray=
				printCode(root,"");
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
    public static void get_path(HuffmanNode node,char value,String s) {
    	if(node.left==null && node.right==null && node.askii==(int)value) {
    		MainViewController.path=s;
    		return;
    	}
    	get_path(node.left,value,s+"0");
    	get_path(node.right,value,s+"1");
    }
    //below snippet of code is took from https://www.geeksforgeeks.org/huffman-coding-greedy-algo-3/
    public static void printCode(HuffmanNode root, String s) 
    {
        if (root.left == null && root.right == null) { 
            // c is the character in the node 
            //System.out.println((char)root.askii + ":" + s);
            MainViewController.path=s;
            return; 
        }
        // if we go to left then add "0" to the code.                       
        // if we go to the right add"1" to the code. 
        // recursive calls for left and 
        // right sub-tree of the generated tree. 
        printCode(root.left, s + "0"); 
        printCode(root.right, s + "1"); 
    } 

}
