package classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

public class Decompressor {
	public static void decompress(File selectedFile) {
		if(selectedFile!=null && selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.')).equalsIgnoreCase(".sas3")) {
			try {
	            ObjectInputStream objectIn = new ObjectInputStream(new FileInputStream(selectedFile));
	            //Data newData=(Data) objectIn.readObject();
	            Data newData=new Data();
	            newData.readObject(objectIn);
	            objectIn.close();
	            regenerate_file(newData,selectedFile);
	            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
	private static void regenerate_file(Data newData,File selectedFile) {
    	String codes=new String(newData.get_bytes());
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
        String fileName=selectedFile.getName().substring(0,selectedFile.getName().lastIndexOf('.'));
        try {
			BufferedWriter out = new BufferedWriter(new FileWriter(selectedFile.getParent()+"\\"+fileName+".txt"));
			out.write(decodedContent);
			out.close();
			showInformation("Decoded!",selectedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
}
