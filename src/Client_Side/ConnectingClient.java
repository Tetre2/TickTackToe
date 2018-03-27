package Client_Side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import Game.Game;
import Server_Side.SingeltonServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class ConnectingClient extends Application{

	SingeltonServer sigelton;

	private double WORLD_WIDTH = sigelton.WORLD_WIDTH;
	private double WORLD_HEIGHT = sigelton.WORLD_HEIGHT;

	private BufferedReader from_server;
	private PrintStream to_server;

	private Socket connection;

	private String player;

	private boolean oneTap = true;

	//	private Group root = new Group();
	//	private Scene scene = new Scene(root, WORLD_WIDTH, WORLD_HEIGHT);

	private Stage primaryStage;

	private String[] empty ={null,null,null,null,null,null,null,null,null};
	private String[] OX;


	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;





		try {

			connection = new Socket("192.168.29.56", 444);
			from_server = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			to_server = new PrintStream(connection.getOutputStream());

		} catch (IOException e) {

			e.printStackTrace();
		}

		Runnable output_listener = () -> {
			//kollar vad som kommer från servern
			while (true) {
				try {

					String input = from_server.readLine();

					if(input.charAt(0) == '/'){

						String cmd = input.substring(1).toLowerCase();

						if (cmd.equals("startgame")) {
							
							Platform.runLater(new Runnable(){
								@Override
								public void run() {
									refreshGame(empty, null);
								}								   
							});
						}
						
						if (cmd.equals("endgame")) {
							
							Platform.runLater(new Runnable(){
								@Override
								public void run() {
									setEndScene(OX, null);
								}								   
							});
						}
					}


					if(input.charAt(0) == '!'){
						String cmd = input.substring(1);
						player = cmd;
						System.out.println("Player"+player);

						Platform.runLater(new Runnable(){
							@Override
							public void run() {
								setReadyScene();
								primaryStage.setTitle("Player"+player);
								primaryStage.show();
							}								   
						});

					}

					if (input.charAt(0) == '%') {
						String cmd = input.substring(1);

						OX = cmd.split(" ");
						
						Platform.runLater(new Runnable(){
							@Override
							public void run() {
								refreshGame(OX, "X");
							}								   
						});

					} 

					if (input.charAt(0) == '&') {
						String cmd = input.substring(1);

						OX = cmd.split(" ");
						
						Platform.runLater(new Runnable(){
							@Override
							public void run() {
								refreshGame(OX, "O");
							}								   
						});
						

					}



				} catch (IOException e) {
					System.out.println("DISCONNETED FROM SERVER");
					connection = null;
					break;
				}
			}
		};

		new Thread(output_listener).start();


	}
	
	public void setEndScene(String[] ox, String s){
		
		Group group = new Group();
		Game g = new Game(ox, s, this);
		g.setScaleX(0.5);
		g.setScaleY(0.5);
		g.setTranslateX(-g.getLayoutBounds().getWidth()/2);
		group.getChildren().add(g);
		
		Group btn = createTextBtn("Quit", null);
		btn.setTranslateX(WORLD_WIDTH*2/3);
		btn.setTranslateY(WORLD_HEIGHT/3);
		
		btn.setOnMouseClicked(event->{
			
			System.exit(0);
			
		});
		
		group.getChildren().add(btn);
		
		Scene scene = new Scene(group, WORLD_WIDTH, WORLD_HEIGHT);


		this.primaryStage.setScene(scene);
		
	}

	public void dataToServer(String[] arr){

		String s = "";

		for (int i = 0; i < arr.length; i++) {
			if(i == 0){
				s+= arr[i];
			}else{
				s+= " "+arr[i];
			}
		}

		to_server.println("/1"+s);

	}


	private void refreshGame(String[] ox, String s){

		Group group = new Game(ox, s, this);
		Scene scene = new Scene(group, WORLD_WIDTH, WORLD_HEIGHT);


		this.primaryStage.setScene(scene);

	}

	private void setReadyScene(){

		Group group = new Group();
		Scene scene = new Scene(group, WORLD_WIDTH, WORLD_HEIGHT);

		Text t = new Text("You are Player" + player + "\nNot Ready ");
		t.setTranslateX(200);
		t.setTranslateY(200);

		Group setReady = createTextBtn("READY!", null);

		setReady.setOnMouseClicked(event->{

			if(oneTap){
				//Skicka till server en ready, där kollar servern efter två och sedan starta.
				to_server.println("&");
				t.setText("You are Player" + player + "\nReady ");
				oneTap = false;
			}

		});

		setReady.setTranslateX(WORLD_WIDTH*5/6);
		setReady.setTranslateY(WORLD_HEIGHT/2);


		group.getChildren().addAll(setReady, t);

		this.primaryStage.setScene(scene);

	}


	public static void main(String[] args) {
		launch(args);
	}

	public Group createTextBtn(String s, Font f){

		Text t = new Text(s);
		t.setFont(f);

		double textWidth = t.getLayoutBounds().getWidth();
		double textHight = t.getLayoutBounds().getHeight();

		Text x = new Text("x");
		x.setFont(f);
		double wordSpacing = x.getLayoutBounds().getWidth();

		Rectangle r = new Rectangle(textWidth+ wordSpacing*2, textHight+ textHight/2);
		r.setArcHeight(textHight/3);
		r.setArcWidth(textHight/3);
		r.setStrokeWidth(textHight/15);
		r.setStroke(Color.BLACK);
		r.setFill(Color.rgb(204, 204, 255));


		t.setTranslateX(wordSpacing);
		t.setTranslateY(textHight + textHight/8);

		Group g = new Group();
		g.getChildren().addAll(r,t);



		g.setOnMouseEntered(event->{

			r.setFill(Color.rgb(255, 153, 102));

		});

		g.setOnMouseExited(event->{

			r.setFill(Color.rgb(204, 204, 255));

		});


		return g;

	}
	
}
