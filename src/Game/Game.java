package Game;

import Client_Side.ConnectingClient;
import Server_Side.SingeltonServer;
import javafx.scene.Group;

public class Game extends Group{

	public String[] OX = new String[9];
	private double squareSize = (SingeltonServer.WORLD_WIDTH+SingeltonServer.WORLD_WIDTH)/10;
	private String turn;
	private ConnectingClient connectingClient;
	private boolean oneTurn = false;


	public Game(String[] ox, String s, ConnectingClient connectingClient){
		this.connectingClient = connectingClient;
		turn = s;

		OX = ox;

		Group g = new Group();

		for (int y = 0; y < 3; y++) {

			for (int x = 0; x < 3; x++) {

				Group square = createSmalSquare((3*y) + x, OX[(3*y) + x]);
				square.setTranslateX((square.getLayoutBounds().getWidth()-squareSize/15)*x);
				square.setTranslateY((square.getLayoutBounds().getHeight()-squareSize/15)*y);

				g.getChildren().add(square);

			}

		}

		g.setTranslateX(SingeltonServer.WORLD_WIDTH/2 - g.getLayoutBounds().getWidth()/2);
		g.setTranslateY(SingeltonServer.WORLD_HEIGHT/2 - g.getLayoutBounds().getHeight()/2);
		this.getChildren().add(g);




	}

	private Group createSmalSquare(int i, String string){



		Square s = new Square(squareSize);
		s.setNumberInGrid(i);
		if(!(string == null)){

			if(string.equals("X")){
				s.setX();
			}
			else if(string.equals("O")){
				s.setO();
			}
			else if((string.equals("null ") || (string.equals("null")))){

			}
			else if(string.equals("")){

			}else{
				System.out.println("FEEEEEEEEEEEEEEL");
			}

		}


		s.setOnMouseClicked(event->{
			if(!oneTurn){

				if(!s.isOcupied()){

					

					if(!(turn == null) ){

						if(turn.equals("X")){
							s.setX();
							oneTurn= true;
							OX[s.getNumberInGrid()] = turn;
						}
						else if(turn.equals("O")){
							s.setO();
							oneTurn= true;
							OX[s.getNumberInGrid()] = turn;
						}
						else{
							System.out.println("Turn is NULL");
						}
					}
					
					connectingClient.dataToServer(OX);

				}			
			}
		});

		return s;

	}


}
