package classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

public class Decompressor {
	public static void decompress(File selectedFile) {
		if(selectedFile!=null && selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.')).equalsIgnoreCase(".sas3")) {
			try {
	            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(selectedFile));
	            Data newData=(Data) objectIn.readObject();
	            objectIn.close();
	            
	            regenerate_file(newData,selectedFile);
	            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
	private static void regenerate_file(Data newData,File selectedFile) {
    	System.out.println("Encoded data:");
    	String codes=new String(newData.get_bytes());
    	System.out.println(codes);
    	System.out.println("\n");
    	String decodedContent="";
        String temp="";
        for(int index=0;index<codes.length();index++) {
        	temp+=codes.charAt(index);
        	for(Map.Entry<Character,String>m:newData.getCodeBook().entrySet()) {
        		if(m.getValue().equals(temp)) {
        			decodedContent+=m.getKey();
        			temp="";
        			break;
        		}
        	}
        }
        System.out.println(decodedContent);
        String fileName=selectedFile.getName().substring(0,selectedFile.getName().lastIndexOf('.'));
        try {
			BufferedWriter out = new BufferedWriter(new FileWriter(selectedFile.getParent()+"\\"+fileName+".txt"));
			out.write(decodedContent);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
