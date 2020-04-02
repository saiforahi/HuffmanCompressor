package classes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class Compressor {
	public Compressor() {
		
	}
	public static void compress(Vector<Symbol> characters,File selectedFile) {
		try {
			if(selectedFile!=null) {
	    		String data = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
				characters=new Vector<Symbol>();
				for(int index=0;index<data.length();index++) {
					boolean found=false;
					for(int index2=0;index2<characters.size();index2++) {
						if(characters.get(index2).get_character()==data.charAt(index)) {          //collecting symbols/characters
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
					System.out.println(characters.get(index).get_character()+"->"+characters.get(index).get_frequency());  //showing frequencies in console in ascending order
				}
		        set_paths(characters,make_huffman_tree(characters).element(),"");   //setting paths
		        generate_file(characters,selectedFile,data);                        // generating output based on huffman coding paths of symbols
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void set_paths(Vector<Symbol> characters,HuffmanNode node,String s) {  //this function recursively set path from symbols by traversing huffman coding tree
    	if(node.left == null && node.right == null) {
    		for(int index=0;index<characters.size();index++) {
    			if(characters.get(index).get_ascii_value()==node.askii) {
    				characters.get(index).set_path(s);
    				break;
    			}
    		}
    		return;
    	}
    	set_paths(characters,node.left,s+"0");
    	set_paths(characters,node.right,s+"1");
    }
	private static void generate_file(Vector<Symbol> characters,File selectedFile,String data) {  //this function generates file after compressing has been done
    	Map<Character,String> map=new HashMap<Character,String>();
    	for(int index=0;index<characters.size();index++) {
    		map.put(characters.get(index).get_character(), characters.get(index).get_path());
    	}
    	String codes="";
    	for(int index=0;index<data.length();index++) {
    		for(int index2=0;index2<characters.size();index2++) {
    			if(characters.get(index2).get_character()==data.charAt(index)) {
    				codes+=characters.get(index2).get_path();
    			}
    		}
    	}
    	try {
    		Data d=new Data(map,codes.getBytes());
    		String fileName=selectedFile.getName().substring(0,selectedFile.getName().lastIndexOf('.'));
    		FileOutputStream fos = new FileOutputStream(selectedFile.getParent()+"\\"+fileName+".sas3");
            ObjectOutputStream objectOut = new ObjectOutputStream(fos);
            d.writeObject(objectOut);
            objectOut.close();
            //os.write(bitSet.toByteArray()); 
            fos.close();
            characters.clear();
            System.out.println("Encoded content: "+codes);
            showInformation("Compressed!",selectedFile);
        } 
        catch (Exception e) {
        	e.printStackTrace();
        } 
    }
	
	public static void showInformation(String title,File selectedFile) {
    	//Runtime.getRuntime().exec("explorer.exe /select," + path);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        String fileName=selectedFile.getName().substring(0,selectedFile.getName().lastIndexOf('.'));
        alert.setContentText("Size :"+new File(selectedFile.getParent()+"\\"+fileName+".sas3").length()+" bytes");
        alert.showAndWait();
    }
    
    private static PriorityQueue<HuffmanNode> make_huffman_tree(Vector<Symbol> characters) {
    	PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>(characters.size());  	// this function is took from 																			
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
            HuffmanNode xtemp1 = queue.peek(); //peek() returns the minimum element from queue
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
}
