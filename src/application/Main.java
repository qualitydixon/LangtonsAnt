package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static application.Config.*;


public class Main extends Application {

	// Initial conditions
	int turnCount = 1;
	int a = ANTSTARTX;
	int b = ANTSTARTY;
	// The variable direction is which way Ant is pointing
	String direction = "UP";
	int right = 1, left = -1;
	boolean isRight = true;
    final Timeline loop = new Timeline();
    Rectangle currentSquare = new Rectangle();
    Label turnLabel;

	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root,WINDOWWIDTH,WINDOWHEIGHT,BACKGROUND);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle(HEADERTEXT);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Set background
		ImageView menuImg = new ImageView(new Image(getClass().getResourceAsStream("/bg_clipped2.png")));
        menuImg.setFocusTraversable(true);
        menuImg.setLayoutX(600);
        menuImg.setLayoutY(0);
        menuImg.setFitWidth(300);
        menuImg.setFitHeight(600);
        root.getChildren().add(menuImg);

		// Add Rules
		Text rulesTitle = new Text(RULESTITLE);
		rulesTitle.setFont(Font.font ("BetecknaLowerCaseMedium", 20));
		rulesTitle.setFill(WHITE);
		Text rules = new Text("1. If current square is white it changes to black, the Ant rotates"
				+ " 90 degrees to the right and moves forward one square. \n \n"
				+ "2. If current square is black it changes to white, the Ant rotates"
				+ " 90 degrees to the left and moves forward one square.  \n");
		rules.setWrappingWidth(280);
		rules.setFont(Font.font ("Ubuntu Mono", 16));
		rules.setFill(WHITE);

		// Turn count text
		turnLabel = new Label("Turn Count: " + turnCount);
		turnLabel.setFont(Font.font("Ubuntu Mono", 16));
		turnLabel.setTextFill(WHITE);

		// Reset/Play/Stop button
        Button reset = new Button("Reset");
        Button play = new Button("Play");
        Button pause = new Button("Pause");

        // Make all buttons the same size
        reset.setMinWidth(BTNWIDTH);
        play.setMinWidth(BTNWIDTH);
        pause.setMinWidth(BTNWIDTH);

        // Combo box
        ObservableList<Double> options =
        	    FXCollections.observableArrayList(
        	    	5.0,
        	        10.0,
        	        100.0,
        	        500.0,
        	        1000.0
        	    );
    	final ComboBox<Double> timeStep = new ComboBox<Double>(options);
    	timeStep.setPromptText("timestep (ms)");

    	// HBox containing buttons
        HBox hbox = new HBox(8);
        hbox.getChildren().addAll(
        		pause,
        		play,
        		reset);

        // VBox containing text and buttons
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
        		rulesTitle,
        		rules,
        		hbox,
        		timeStep,
        		turnLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX(620);
        vbox.setLayoutY(73);
        root.getChildren().add(vbox);

        Grid mainGrid = new Grid();
        for(Rectangle[] i : mainGrid.grid) {
        	for(Rectangle j : i) {
        		root.getChildren().add(j);
        	}
        }

        // Create and start timeline
		makeTimeline(timeStep, mainGrid);

		// Set reset button action
		reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                resetGrid(mainGrid);
                currentSquare = mainGrid.grid[ANTSTARTX][ANTSTARTY];
                turnCount = 1;
            	a = ANTSTARTX;
            	b = ANTSTARTY;
                loop.playFromStart();
            }
        });

		// Set play button event
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                loop.play();
            }
        });

        // Set pause button event
        pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                loop.pause();
            }
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

	// Set all squares back to white
	public void resetGrid(Grid gd) {
		for(Rectangle[] i : gd.grid) {
			for (Rectangle j : i) {
				j.setFill(WHITE);
				j.setStrokeWidth(.5);
			}
		}
	}

	// Method to wrap timeline in a ChangeListener. Could probably be combined with startLoop
	public void makeTimeline(ComboBox<Double> timeOptions, Grid gr) {
        loop.setCycleCount(Timeline.INDEFINITE);

        timeOptions.valueProperty().addListener(new ChangeListener<Double>() {
            @Override
            public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                startLoop(loop, newValue, gr);
            }
        });

        // Default selection is 100 ms
        timeOptions.getSelectionModel().select(2);
    }

    public void startLoop(Timeline tl, double deltaT, Grid gr) {
        KeyFrame keyFrame = new KeyFrame(
            Duration.millis(deltaT),
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
					if(turnCount%2==0 && currentSquare.getFill()==WHITE){
						a = a + right;
						isRight = true;
					}else if(turnCount%2==0 && currentSquare.getFill()==OFFBLACK){
						a = a + left;
						isRight = false;
					}else if(turnCount%2==1 && currentSquare.getFill()==WHITE){
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
					turnCount++;
					turnLabel.setText("Turn Count: " + turnCount);

                }
            }
        );

        // Timeline must be stopped to reset keyframes
        tl.stop();
        tl.getKeyFrames().setAll(keyFrame);
        tl.play();
    }


	public static void main(String[] args) {
		launch(args);
	}
}
