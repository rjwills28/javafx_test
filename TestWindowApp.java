import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;

public class TestWindowApp extends Application {
	@Override
	public void start(Stage priStage) throws Exception {
		//Create the root node Group object
		Group group = new Group();
		ObservableList objsList = group.getChildren();

		//Create a rectangle
		Rectangle rec = new Rectangle();
		//Set position from top left
		rec.setX(100);
		rec.setY(100);
		rec.setWidth(20);
		rec.setHeight(20);
		rec.setFill(Color.GREY);
		rec.setStroke(Color.BLACK);
		// Add to group
		objsList.add(rec);

		//Create text
		Text text = new Text(105, 115, "0");
		text.setFill(Color.GREEN);
		text.setStrokeWidth(2);
		objsList.add(text);

		//Create a scene of height and width
		Scene scene = new Scene(group, 400, 400);

		//Give it a colour
		scene.setFill(Color.GREY);

		//Title
		priStage.setTitle("Test Standalone Window Application");

		//Add scene to stage
		priStage.setScene(scene);

		//Display
		priStage.show();
	}

	public static void main(String args[]) {
		launch(args);
	}
}