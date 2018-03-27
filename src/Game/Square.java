package Game;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Square extends Group{

	private boolean isOcupied = false;
	private String XO;
	private Group O;
	private Group X;
	private int numberInGrid;
	
	

	public Square(double size) {

		Rectangle r = new Rectangle(size, size);
		
		r.setStrokeWidth(size/15);
		r.setStroke(Color.BLACK);
		r.setFill(Color.TRANSPARENT);
		
		//--------------
		
		Rectangle r1 = new Rectangle(size/20, size);
		Rectangle r2 = new Rectangle(size/20, size);
		r1.setTranslateX(size/2 - r1.getLayoutBounds().getWidth()/2);
		r2.setTranslateX(size/2 - r2.getLayoutBounds().getWidth()/2);
		r1.setRotate(45);
		r2.setRotate(-45);
		X = new Group();
		X.getChildren().addAll(r1,r2);
		X.setVisible(false);
		
		
		//--------------
		
		Circle cir = new Circle(size/2,size/2,size/4);
		cir.setStrokeWidth(size/15);
		cir.setStroke(Color.BLACK);
		cir.setFill(Color.TRANSPARENT);
		O = new Group();
		O.getChildren().addAll(cir);
		O.setVisible(false);
		
		this.getChildren().addAll(r,X,O);
	}
	
	public int getNumberInGrid() {
		return numberInGrid;
	}

	public void setNumberInGrid(int numberInGrid) {
		this.numberInGrid = numberInGrid;
	}
	
	public boolean isOcupied(){
		return isOcupied;
	}
	
	public String getXO(){
		return XO;
	}
	
	
	public void setX(){
		isOcupied = true;
		XO = "X";
		X.setVisible(true);
	}
	
	public void setO(){
		isOcupied = true;
		XO = "O";
		O.setVisible(true);
	}
	
	
}
