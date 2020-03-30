package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import classes.Data;
import classes.HuffmanNode;
import classes.Sorter;
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
	private Symbol[] inputs;
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
    void decompress() {
    	if(this.selectedFile!=null && this.selectedFile.getName().substring(this.selectedFile.getName().lastIndexOf('.')).equalsIgnoreCase(".sas3")) {
			try {
				FileInputStream fileIn = new FileInputStream(this.selectedFile);
	            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
	            Data newData=(Data) objectIn.readObject();
	            objectIn.close();
	            fileIn.close();
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
						characters.add(new Symbol((int)data.charAt(index),1));
					}
				}
				Collections.sort(characters,Collections.reverseOrder());
				for(int index=0;index<characters.size();index++) {
					System.out.println(characters.get(index).get_character()+"->"+characters.get(index).get_frequency());
				}
		        set_paths(make_huffman_tree(characters).element(),"");
		        inputs=new Symbol[characters.size()];
				for(int index=0;index<characters.size();index++) {
					inputs[index]=characters.get(index);
				}
		        make_codebook();
		        generate_file(data);
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void make_codebook() {      //this function is implemented by following a brief description on https://www.geeksforgeeks.org/canonical-huffman-coding/
    	//System.out.println();
    	
    	Sorter.insertion_sort(inputs,inputs.length);
    	/*for(int index=0;index<inputs.length;index++) {
    		System.out.println((char)inputs[index].get_ascii_value()+"->"+inputs[index].get_path());
    	}
    	System.out.println();*/
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
    public void set_paths(HuffmanNode node,String s) {  //this function recursively set path from symbols by traversing huffman coding tree
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
    public void generate_file(String data) {  //this function generates file after compressing has been done
    	Map<Integer,Integer> map=new HashMap<Integer,Integer>();
    	for(int index=0;index<inputs.length;index++) {
    		map.put(inputs[index].get_ascii_value(), inputs[index].get_frequency());
    	}
    	String codes="";
    	for(int index=0;index<data.length();index++) {
    		for(int index2=0;index2<inputs.length;index2++) {
    			if(inputs[index2].get_character()==data.charAt(index)) {
    				codes+=inputs[index2].get_path();
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
            System.out.println("Bitset: "+codes);
            /*for(int index=0;index<bitSet.length();index++) {
            	if(bitSet.get(index)) {
            		System.out.print(1);
            	}
            	else {
            		System.out.print(0);
            	}
            }*/
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
            queue.add(ztemp3);
        }
		return queue;
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
    	characters=new Vector<Symbol>();
    	//generate_codeBook(newData.get_node(),"");
    	System.out.println(characters.size());
        String temp="";
        for(int index=0;index<newData.get_bits().length();index++) {
        	if(newData.get_bits().get(index)) {
                temp += "1";
            } else {
                temp += "0";
            }
        	for(int index2=0;index2<characters.size();index2++) {
        		if(characters.get(index2).get_path().equalsIgnoreCase(temp)) {
        			text+=characters.get(index2).get_character();
        			temp="";
        			break;
        		}
        	}
        }
        characters.clear();
        System.out.println(text);
    }
    
    public void generate_codeBook(HuffmanNode root,String s) {
    	if(root.left == null && root.right == null) {
    		characters.add(new Symbol(root.askii,root.frequency_value,s));
    		return;
    	}
    	set_paths(root.left,s+"0");
    	set_paths(root.right,s+"1");
    }
} 