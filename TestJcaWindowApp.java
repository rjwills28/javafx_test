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

import gov.aps.jca.JCALibrary;
import gov.aps.jca.Channel;
import gov.aps.jca.Context;
import gov.aps.jca.dbr.DBR;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;
import gov.aps.jca.Monitor;
import gov.aps.jca.CAStatus;

public class TestJcaWindowApp extends Application {
	//Configurable
	int numWindows = 10;
	int numWidgets = 20;

	final static int MAX_NUM_WIDGETS = 100;

	public void jcaStartMonitor(Text text, String channelName){
        try {
        	Context context = null;
            JCALibrary jca = JCALibrary.getInstance();
            context = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
            Channel channel = context.createChannel(channelName);
            context.pendIO(1.0);

            Monitor monitor = channel.addMonitor(Monitor.VALUE, new MonitorListenerImpl(text));
            context.flushIO();
        } catch (Throwable th) {
            th.printStackTrace();
        } 
    }

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
			int xpos = 50;
			int ypos = 10;
			//Create widgets to add to the group
			for (int i = 0; i < numWidgets; i++) {
				if (i % 10 == 0) {
					ypos = ypos + 30;
					xpos = 50;
				}
				//Create a rectangle
				Rectangle rec = new Rectangle();
				//Set position from top left
				rec.setX(xpos);
				rec.setY(ypos);
				rec.setWidth(28);
				rec.setHeight(28);
				rec.setFill(Color.GREY);
				rec.setStroke(Color.BLACK);
				// Add to group
				objsList.add(rec);

				//Create text
				Text text = new Text(xpos+5, ypos+18, "");
				text.setFill(Color.LIME);
				text.setStrokeWidth(2);
				objsList.add(text);

				String channelName = "TEST:REC"+(n * numWidgets + i);
				jcaStartMonitor(text, channelName);

				// Adjust xpos of next widget
				xpos = xpos + 30;
			}

			//Create a scene of height and width
			TabPane tabPane = new TabPane();
 			Tab tab = new Tab("Tab 1");
 			tab.setContent(tabLayout);
 			tabPane.getTabs().add(tab);
 			layout.setCenter(tabPane);
			Scene scene = new Scene(layout, 400, 400);

			//Give it a colour
			scene.setFill(Color.GREY);

			// Create a new Stage (window)
			Stage secondStage = new Stage();
			secondStage.setTitle("Test Standalone Window " + (n + 1));
			secondStage.setWidth(400);
			secondStage.setHeight(400);
			secondStage.setX(winXpos);
			secondStage.setY(winYpos);
			secondStage.setScene(scene);
			secondStage.show();

			winXpos = winXpos + 350;
		}
	}

	private static class MonitorListenerImpl implements MonitorListener {
		private Text textWidget;

		MonitorListenerImpl(Text text) {
			this.textWidget = text;
		}
        public void monitorChanged(MonitorEvent event) {
            DBR dbr = null;
            if (event.getStatus() == CAStatus.NORMAL) {
                dbr = event.getDBR();
                double[] val = ((double[] ) dbr.getValue());
                Platform.runLater(() -> {
					textWidget.setText(String.valueOf(val[0]));
				});
            } else
                System.err.println("Monitor error: " + event.getStatus());
        }
    }

	

	public static void main(String args[]) {
		launch(args);
	}
}