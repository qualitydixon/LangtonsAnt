package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Rectangle;
import static application.Config.*;


public class Main extends Application {

	// Initial conditions
	int a = ANTSTARTX, b = ANTSTARTY;
	int right = 1, left = -1;

	boolean isRight = true;

    Timeline loop = new Timeline();
	String direction = "UP";
    Rectangle currentSquare = new Rectangle();

	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root,WINDOWWIDTH,WINDOWHEIGHT,BACKGROUND);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle(HEADERTEXT);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Create menu and add elements
		MakeUI menu = new MakeUI();
		root.getChildren().addAll(
				menu.menuImg,
				menu.vbox);

		// Add squares
        Grid mainGrid = new Grid();
        for(Rectangle[] i : mainGrid.grid) {
        	for(Rectangle j : i) {
        		root.getChildren().add(j);
        	}
        }

        // Create and start timeline
		makeTimeline(menu.timeStep, mainGrid, menu);

		// Set reset button action
		menu.reset.setOnAction((event) -> {
                mainGrid.resetGrid();
                currentSquare = mainGrid.grid[ANTSTARTX][ANTSTARTY];
                menu.turnCount = 1;
            	a = ANTSTARTX;
            	b = ANTSTARTY;
                loop.playFromStart();
        });

		// Set play button event
        menu.play.setOnAction((event) -> {
                loop.play();
        });

        // Set pause button event
        menu.pause.setOnAction((event) -> {
                loop.pause();
            });


	}

	// Determine the new direction
	public static String newDir(String dir, boolean ifRight) {
		String ndir = "";
		// Switch statement to set new direction
		switch (dir) {
		case "UP":
			if(ifRight){ndir="RIGHT";}
			else{ndir="LEFT";}
			break;
		case "DOWN":
			if(ifRight){ndir="LEFT";}
			else{ndir="RIGHT";}
			break;
		case "LEFT":
			if(ifRight){ndir="UP";}
			else{ndir="DOWN";}
			break;
		case "RIGHT":
			if(ifRight){ndir="DOWN";}
			else{ndir="UP";}
		}
		return ndir;
	}

	// Change stroke of current square
	public static void setOnStroke(Rectangle cs) {
		cs.setStrokeWidth(.5);
		cs.setStroke(ANTONCOLOR);
		cs.setStrokeWidth(2.5);
	}

	// Method to wrap timeline in a ChangeListener. Could probably be combined with startLoop
	public void makeTimeline(ComboBox<Double> timeOptions, Grid gr, MakeUI mu) {
        loop.setCycleCount(Timeline.INDEFINITE);

        timeOptions.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
            	KeyFrame keyFrame = new KeyFrame(
                        Duration.millis(newValue),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {

                            	// For directions UP and RIGHT, moving to the right is in the + direction
                                if(direction == "UP" || direction == "RIGHT") {
                                	right = 1;
                                	left = -1;
                                } else {
                                	right = -1;
                                	left = 1;
                                }

            					// Determine next square based on color of Square and whether turn is even or odd
            					if(mu.turnCount%2==0 && currentSquare.getFill()==WHITE){
            						a = a + right;
            						isRight = true;
            					}else if(mu.turnCount%2==0 && currentSquare.getFill()==OFFBLACK){
            						a = a + left;
            						isRight = false;
            					}else if(mu.turnCount%2==1 && currentSquare.getFill()==WHITE){
            						b = b + right;
            						isRight = true;
            					}else {
            						b = b + left;
            						isRight = false;
            					}
            					direction = newDir(direction, isRight);

            					// Change color of current square
            					if(currentSquare.getFill()==WHITE){
            						currentSquare.setFill(OFFBLACK);
            					}else{currentSquare.setFill(WHITE);}

            					// Change stroke back to Black
            					currentSquare.setStroke(OFFBLACK);
            					currentSquare.setStrokeWidth(.5);

            					// Set new current square
            					currentSquare = gr.grid[a][b];
            					setOnStroke(currentSquare);

            					// Update turn count
            					mu.turnCount++;
            					mu.turnLabel.setText("Turn Count: " + mu.turnCount);

                            }
                        }
                    );

                    // Timeline must be stopped to reset keyframes
                    loop.stop();
                    loop.getKeyFrames().setAll(keyFrame);
                    loop.play();
            }
        });

        // Default selection is 100 ms
        timeOptions.getSelectionModel().select(2);
    }

	public static void main(String[] args) {
		launch(args);
	}
}
