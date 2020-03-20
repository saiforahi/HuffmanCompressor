package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
				//BorderPane root = new BorderPane();
				//URL url = Paths.get("./src/main/resources/fxml/Fxml.fxml").toUri().toURL();
				Parent mainPane=FXMLLoader.load(Main.class.getResource("/ui/MainView.fxml"));
				Scene scene = new Scene(mainPane);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setTitle("HuffmanCompressor");
				primaryStage.show();
				
				
				//Stage stage = new Stage(); // Create a new stage
				//stage.setTitle("Second Stage"); // Set the stage title
				// Set a scene with a button in the stage
				//stage.setScene(new Scene(new Button("New Stage"), 100, 100));
				//stage.show(); // Display the stage
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
