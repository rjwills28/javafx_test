import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TestWindowApp extends Application {
	//Configurable
	int numWindows = 10;
	int numWidgets = 20;

	final static int MAX_NUM_WIDGETS = 100;

	@Override
	public void start(Stage priStage) throws Exception {
		int winXpos = 0;
		int winYpos = 0;

		// Set max number of widget
		if (numWidgets > MAX_NUM_WIDGETS) {
			numWidgets = MAX_NUM_WIDGETS;
		}

		// Create n window
		for (int n = 0; n < numWindows; n++) {
			//Adjust position on screen
			if (n % 5 == 0 && n < 20 && n != 0) {
				winXpos = 0;
				winYpos = winYpos + 300;
			}

			//Create the root node object
			BorderPane layout = new BorderPane();
			BorderPane tabLayout = new BorderPane();
 			ObservableList objsList = tabLayout.getChildren();

			// Starting pos
			int xpos = 20;
			int ypos = 10;
			//Create widgets to add to the group
			for (int i = 0; i < numWidgets; i++) {
				if (i % 10 == 0) {
					ypos = ypos + 30;
					xpos = 30;
				}
				//Create a rectangle
				Rectangle rec = new Rectangle();
				//Set position from top left
				rec.setX(xpos);
				rec.setY(ypos);
				rec.setWidth(33);
				rec.setHeight(25);
				rec.setFill(Color.GREY);
				rec.setStroke(Color.BLACK);
				// Add to group
				objsList.add(rec);

				//Create text
				Text text = new Text(xpos+5, ypos+17, "");
				text.setFill(Color.LIME);
				text.setStrokeWidth(2);
				objsList.add(text);

				//Update widget values at 10Hz
				Task<Void> task = new Task<Void>() {
					String value = "";

					@Override
					public Void call() throws Exception{
						int i = 0;
						while(true) {
							if (i == 0) {
								i = 1;
								value = "0";
							} else {
								i = 0;
								value = "1";
							}
							Platform.runLater(() -> {
								text.setText(value);
							});
							Thread.sleep(100);
						}
					}
				};
				Thread th = new Thread(task);
				th.setDaemon(true);
				th.start();

				// Adjust xpos of next widget
				xpos = xpos + 36;
			}

			//Create a scene of height and width
			TabPane tabPane = new TabPane();
 			Tab tab = new Tab("Tab 1");
 			tab.setContent(tabLayout);
 			tabPane.getTabs().add(tab);
 			layout.setCenter(tabPane);
			Scene scene = new Scene(layout, 420, 400);

			//Give it a colour
			scene.setFill(Color.GREY);

			// Create a new Stage (window)
			Stage secondStage = new Stage();
			secondStage.setTitle("Test Standalone Window " + (n + 1));
			secondStage.setWidth(420);
			secondStage.setHeight(400);
			secondStage.setX(winXpos);
			secondStage.setY(winYpos);
			secondStage.setScene(scene);
			secondStage.show();

			winXpos = winXpos + 350;
		}
	}

	public static void main(String args[]) {
		launch(args);
	}
}