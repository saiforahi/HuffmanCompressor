package classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.Vector;

public class Decompressor {
	public static void decompress(File selectedFile,Vector<Symbol> characters) {
		if(selectedFile!=null && selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.')).equalsIgnoreCase(".sas3")) {
			try {
	            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(selectedFile));
	            Data newData=(Data) objectIn.readObject();
	            objectIn.close();
	            
	            regenerate_file(newData,selectedFile,characters);
	            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
	private static void regenerate_file(Data newData,File selectedFile,Vector<Symbol> characters) {
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
        String fileName=selectedFile.getName().substring(0,selectedFile.getName().lastIndexOf('.'));
        try {
			BufferedWriter out = new BufferedWriter(new FileWriter(selectedFile.getParent()+"\\"+fileName+".txt"));
			out.write(text);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
