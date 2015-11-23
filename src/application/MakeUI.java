package application;

import static application.Config.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MakeUI {

	Button reset, play, pause;
	Label turnLabel;
	ComboBox<Double> timeStep;
	CheckBox showGridLines;
	ImageView menuImg;
	VBox vbox;
	int turnCount = 1;

	public MakeUI() {

		// Create background
		menuImg = new ImageView(new Image(getClass().getResourceAsStream("/bg_clipped2.png")));
        menuImg.setFocusTraversable(true);
        menuImg.setLayoutX(600);
        menuImg.setLayoutY(0);
        menuImg.setFitWidth(300);
        menuImg.setFitHeight(600);

        // Add Rules
		Text rulesTitle = new Text(RULESTITLE);
		rulesTitle.setFont(TITLEFONT);
		rulesTitle.setFill(WHITE);
		Text rules = new Text("1. If current square is white it changes to black, the Ant rotates"
				+ " 90 degrees to the right and moves forward one square. \n \n"
				+ "2. If current square is black it changes to white, the Ant rotates"
				+ " 90 degrees to the left and moves forward one square.  \n");
		rules.setWrappingWidth(280);
		rules.setFont(MAINFONT);
		rules.setFill(WHITE);

		// Turn count text
		turnLabel = new Label("Turn Count: " + turnCount);
		turnLabel.setFont(MAINFONT);
		turnLabel.setTextFill(WHITE);

		// Reset/Play/Stop button
        reset = new Button("Reset");
        play = new Button("Play");
        pause = new Button("Pause");

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
    	timeStep = new ComboBox<Double>(options);
    	timeStep.setPromptText("timestep (ms)");

    	// HBox containing buttons
        HBox hbox = new HBox(8);
        hbox.getChildren().addAll(
        		pause,
        		play,
        		reset);

        // VBox containing text and buttons
        vbox = new VBox(10);
        vbox.getChildren().addAll(
        		rulesTitle,
        		rules,
        		hbox,
        		timeStep,
        		turnLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX(620);
        vbox.setLayoutY(73);
	}



}
