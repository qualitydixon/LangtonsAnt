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
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import static application.Config.*;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root,WINDOWWIDTH,WINDOWHEIGHT,Color.ALICEBLUE);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setTitle("Langton's Ant");
		primaryStage.setScene(scene);
		primaryStage.show();

		// DRAW GRID
		Rectangle[][] grid = new Rectangle[GRIDSIZE][GRIDSIZE];

		int x = 0;
		int y = 0;

		for (int i=0;i<grid[0].length;i++) {
			for (int j=0;j<grid.length;j++) {
				grid[i][j] = new Rectangle();
				grid[i][j].setFill(Color.GHOSTWHITE);
				grid[i][j].setStroke(Color.BLACK);
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
					}else if(i%2==0 && currentSquare.getFill()==Color.BLACK){
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
						currentSquare.setFill(Color.BLACK);
					}else{currentSquare.setFill(Color.GHOSTWHITE);}

					// Change stroke back to Black
					currentSquare.setStroke(Color.BLACK);

					// Set new current square
					currentSquare = grid[a][b];

					// Yellow stroke indicates current position of Ant
					currentSquare.setStroke(Color.YELLOW);
					
					// Iterate i
					i++;

			}
			}));


			loop.setCycleCount(Timeline.INDEFINITE);
	        loop.play();
//			final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
//			rectBasicTimeline.setFill(Color.RED);
//			root.getChildren().add(rectBasicTimeline);
//
//			final Timeline timeline = new Timeline();
//			timeline.setCycleCount(Timeline.INDEFINITE);
//			timeline.setAutoReverse(false);
//			final KeyValue kv = new KeyValue(rectBasicTimeline.xProperty(), 300);
//			final KeyFrame kf = new KeyFrame(Duration.millis(1000), kv);
//			timeline.getKeyFrames().add(kf);
//			timeline.play();


	}

	public static void main(String[] args) {
		launch(args);
	}
}
