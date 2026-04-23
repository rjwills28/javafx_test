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
import javafx.scene.layout.Pane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class TestWindowApp extends Application {
    //Configurables
    /* Preselect:
        1: 1 window, 1000 widgets
        2: 20 windows, 50 widgets
        0: Custom
    */
    int preSelectCase = 2;

    int numWindows = 5;
    int numWidgets = 10;
    int numPerRow = 10;
    int sceneWidth = 400;
    int sceneHeight = 400;
    final static int MAX_NUM_WIDGETS = 1000;

    @Override
    public void start(Stage priStage) throws Exception {
        if (preSelectCase == 1) {
            numWindows = 1;
            numWidgets = 1000;
            numPerRow = 50;
            sceneWidth = 1900;
            sceneHeight = 800;

        } else if (preSelectCase == 2){
            numWindows = 20;
            numWidgets = 50;
            numPerRow = 10;
            sceneWidth = 400;
            sceneHeight = 300;
        }

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
            Pane paneLayout = new Pane();
            ObservableList objsList = paneLayout.getChildren();

            // Starting pos
            int xpos = 20;
            int ypos = 10;
            //Create widgets to add to the group
            for (int i = 0; i < numWidgets; i++) {
                String name = "window"+n+"widget"+i;
                if (i % numPerRow == 0) {
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
                        Instant next_update_log = Instant.now().plusSeconds(5);
                        while(true) {
                            final CountDownLatch done = new CountDownLatch(1);
                            final long update_start = System.currentTimeMillis();
                            if (i == 0) {
                                i = 1;
                                value = "0";
                            } else {
                                i = 0;
                                value = "1";
                            }
                            Platform.runLater(() -> {
                                text.setText(value);
                                done.countDown();
                            });
                            while (! done.await(100, TimeUnit.MILLISECONDS))
                                // wait
                                Thread.sleep(1);

                            Thread.sleep(100);
                            final long ms = System.currentTimeMillis() - update_start;
                            final Instant now = Instant.now();
                            if (now.isAfter(next_update_log) && name.equals("window0widget0")) {
                                System.out.println("-> Update interval (ms) " + ms + " => " + (1000.0 / ((double) ms)) + " Hz");
                                next_update_log = now.plusSeconds(5);
                            }
                        }
                    }
                };
                Thread th = new Thread(task);
                th.setDaemon(true);
                th.start();

                // Adjust xpos of next widget
                xpos = xpos + 36;
            }

            // Make this pane scrollable
            ScrollPane scrollPane = new ScrollPane(paneLayout);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

            //Create a tabbed pane
            TabPane tabPane = new TabPane();
            Tab tab = new Tab("Tab 1");
            tab.setContent(scrollPane);
            tabPane.getTabs().add(tab);

             // Add a border
            BorderPane layout = new BorderPane();
            layout.setCenter(tabPane);

             // Create scene
            Scene scene = new Scene(layout, sceneWidth, sceneHeight);
            scene.setFill(Color.GREY);

            // Create a new Stage (window)
            Stage secondStage = new Stage();
            secondStage.setTitle("Test Standalone Window " + (n + 1));
            secondStage.setWidth(sceneWidth);
            secondStage.setHeight(sceneHeight);
            secondStage.setX(winXpos);
            secondStage.setY(winYpos);
            secondStage.setScene(scene);
            secondStage.show();

            winXpos = winXpos + 350;

            //* Debug to see how many node we are drawing */
            //ArrayList<Node> nodes = new ArrayList<Node>();
            //addAllDescendents(scene.getRoot(), nodes);
            //System.out.println(nodes.size());

        }
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }

    public static void main(String args[]) {
        launch(args);
    }
}