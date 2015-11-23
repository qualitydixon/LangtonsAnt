package application;

import static application.Config.*;

import javafx.scene.shape.Rectangle;

public class Grid {

	Rectangle[][] grid;
	public Grid() {

		// DRAW GRID
        this.grid = new Rectangle[GRIDSIZE][GRIDSIZE];

		int x = 0;
		int y = 0;

		for (int i=0;i<grid[0].length;i++) {
			for (int j=0;j<grid.length;j++) {
				grid[i][j] = new Rectangle();
				grid[i][j].setFill(WHITE);
				grid[i][j].setStroke(OFFBLACK);
				grid[i][j].setStrokeWidth(.5);
				grid[i][j].setX(x);
				grid[i][j].setY(y);
				grid[i][j].setWidth(RECTSIZE);
				grid[i][j].setHeight(RECTSIZE);
				x+=RECTSIZE;
			}
			y+=RECTSIZE;
			x=0;
		}
	}

	// Set all squares back to white
	public void resetGrid() {
		for(Rectangle[] i : this.grid) {
			for (Rectangle j : i) {
				j.setFill(WHITE);
				j.setStrokeWidth(.5);
			}
		}
	}

}
