package application;

import java.util.ArrayList;
import java.util.List;
import java.lang.System;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static application.Config.*;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		//BorderPane root = new BorderPane();
		Group root = new Group();
		Scene scene = new Scene(root,WINDOWWIDTH,WINDOWHEIGHT,BACKGROUND);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("Langton's Ant");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		// Set background
		ImageView table = new ImageView(new Image(getClass().getResourceAsStream("/bg_clipped2.png")));
        table.setFocusTraversable(true);
        table.setLayoutX(600);
        table.setLayoutY(0);
        table.setFitWidth(300);
        table.setFitHeight(600);
        root.getChildren().add(table);

		// Add Rules
		Text rulesTitle = new Text(RULESTITLE);
		rulesTitle.setFont(Font.font ("BetecknaLowerCaseMedium", 20));
		rulesTitle.setFill(WHITE);
		Text rules = new Text("1. If current square is white it changes to black, the Ant rotates"
				+ " 90 degrees to the right and moves forward one square. \n \n"
				+ "2. If current square is black it changes to white, the Ant rotates"
				+ " 90 degrees to the left and moves forward one square.  \n");
		rules.setWrappingWidth(270);
		rules.setFont(Font.font ("Sans Regular", 16));
		rules.setFill(WHITE);

		// Reset/Play/Stop button
        Button reset = new Button("Reset");
        Button play = new Button("Play");
        Button pause = new Button("Pause");

        VBox vbox = new VBox(8);
        vbox.getChildren().addAll(rulesTitle, rules);
        vbox.setLayoutX(610);
        vbox.setLayoutY(73);
        root.getChildren().add(vbox);

        HBox hbox = new HBox(10);
        hbox.setLayoutX(658);
        hbox.setLayoutY(300);
        hbox.getChildren().addAll(pause, play, reset);
        root.getChildren().add(hbox);

		// DRAW GRID
		Rectangle[][] grid = new Rectangle[GRIDSIZE][GRIDSIZE];

		int x = 0;
		int y = 0;

		for (int i=0;i<grid[0].length;i++) {
			for (int j=0;j<grid.length;j++) {
				grid[i][j] = new Rectangle();
				grid[i][j].setFill(Color.GHOSTWHITE);
				grid[i][j].setStroke(Color.BLACK);
				grid[i][j].setStrokeWidth(.5);
				grid[i][j].setX(x);
				grid[i][j].setY(y);
				grid[i][j].setWidth(RECTSIZE);
				grid[i][j].setHeight(RECTSIZE);
				x+=RECTSIZE;

				root.getChildren().add(grid[i][j]);
			}
			y+=RECTSIZE;
			x=0;
		}



		Timeline loop = new Timeline(new KeyFrame(Duration.millis(TIMESTEP), new EventHandler<ActionEvent>() {

			int a = ANTSTARTX;
			int b = ANTSTARTY;
			int i = 1;

			// direction is which way Ant is pointing
			String direction = "UP";
			int left = 1, right = 1;
			boolean RIGHT = true;

			// Set Ant initial position
			Rectangle currentSquare = grid[a][b];

			@Override
            public void handle(ActionEvent t) {

					// Switch to values of left and right. Could be simplified as UP = RIGHT & DOWN = LEFT.
					switch (direction) {
					case "UP":
						left = 1;
						right = -1;
						break;
					case "DOWN":
						left = -1;
						right = 1;
						break;
					case "LEFT":
						left = -1;
						right = 1;
						break;
					case "RIGHT":
						left = 1;
						right = -1;
					}

					// Determine next square based on color of Square and whether turn is even or odd
					if(i%2==0 && currentSquare.getFill()==Color.GHOSTWHITE){
						a = a + right;
						RIGHT = true;
					}else if(i%2==0 && currentSquare.getFill()==OFFBLACK){
						a = a + left;
						RIGHT = false;
					}else if(i%2==1 && currentSquare.getFill()==Color.GHOSTWHITE){
						b = b + right;
						RIGHT = true;
					}else {
						b = b + left;
						RIGHT = false;
					}

					// Switch statement to set new direction
					switch (direction) {
					case "UP":
						if(RIGHT){direction="RIGHT";}
						else{direction="LEFT";}
						break;
					case "DOWN":
						if(RIGHT){direction="LEFT";}
						else{direction="RIGHT";}
						break;
					case "LEFT":
						if(RIGHT){direction="UP";}
						else{direction="DOWN";}
						break;
					case "RIGHT":
						if(RIGHT){direction="DOWN";}
						else{direction="UP";}
					}

					// Change color of current square
					if(currentSquare.getFill()==Color.GHOSTWHITE){
						currentSquare.setFill(OFFBLACK);
					}else{currentSquare.setFill(Color.GHOSTWHITE);}

					// Change stroke back to Black
					currentSquare.setStroke(OFFBLACK);

					// Set new current square
					currentSquare = grid[a][b];
					currentSquare.setStrokeWidth(.5);

					// Red stroke indicates current position of Ant
					currentSquare.setStroke(ANTON);
					currentSquare.setStrokeWidth(1.5);

					// Iterate i
					i++;

			}
			}));

			loop.setCycleCount(Timeline.INDEFINITE);
	        loop.play();

	     // Set reset button event
	        reset.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {
	                for(Rectangle[] i : grid) {
	                	for(Rectangle j : i) {
	                		j.setFill(WHITE);
	                		j.setStrokeWidth(.5);
	                	}
	                }
	                //currentSquare = grid[ANTSTARTX][ANTSTARTY];


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

	public static void main(String[] args) {
		launch(args);
	}
}
