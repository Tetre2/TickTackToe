package Server_Side;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import com.sun.security.ntlm.Client;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SingeltonServer {

	public static double WORLD_WIDTH = 800;
	public static double WORLD_HEIGHT = 600;
	ServerSocket server;
	int PORT = 444;

	public volatile static int playersReady = 0;

	private static boolean flipFlop = true;
	private static boolean oneTimeOnly = true;

	public static String[] OX = {null,null,null,null,null,null,null,null,null};

	static ArrayList<ServerClient> clients = new ArrayList<ServerClient>();

	public static void main(String[] args) {
		getInstance();
	}

	private static SingeltonServer ME = null;


	private SingeltonServer() {


		try {
			server = new ServerSocket(PORT);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		Runnable connection_listener = () -> {
			while (!(clients.size() == 2)) {
				try {

					ServerClient client = new ServerClient(server.accept());
					clients.add(client);
					clients.get(clients.size()-1).setName(clients.size()+"");
					System.out.println("A CLIENT CONNECTED");

				} catch (IOException e) {
					System.out.println("Conection Error");
				}
			}

			while (true) {
				if(playersReady == 2){
					StartGame();
					break;

				}

			}

		};



		new Thread(connection_listener).start();

		System.out.println("SERVER STARTED ON PORT " + PORT);






	}

	private static void checkForWin(){

		if(checkHorizontal() || checkVertical() || checkDiagonal()){

			System.out.println("WINNNNNNNNNNNNNNNN");
			StartGame();
			
			for (int i = 0; i < clients.size(); i++) {
				clients.get(i).setEnding();
			}
			
		}
		else if(checkNoWin()){
			System.out.println("TO BAD");
			StartGame();
			
		}else{

			StartGame();

		}
	}
	
	private static boolean checkNoWin(){
		int ocupiedSpaces = 0;
		for (int i = 0; i < OX.length; i++) {
			
			if(!OX[i].equals("null")){
				ocupiedSpaces++;
			}
			
		}
		
		if(ocupiedSpaces == 9){
			return true;
		}
		
		return false;
	}

	private static boolean checkHorizontal(){

		//	0	1	2
		//	3	4	5
		//	6	7	8

		for (int i = 0; i < 3; i++) {

			String s1 = "";
			String s2 = "";
			String s3 = "";

			s1 = OX[3*i];
			s2 = OX[3*i + 1];
			s3 = OX[3*i + 2];

			if(!(s1.equals("null")) && !(s2.equals("null")) && !(s3.equals("null"))){

				if(s1.equals(s2) && s2.equals(s3)){

					return true;

				}
				
			}

		}

		return false;

	}

	private static boolean checkVertical(){

		//	0	1	2
		//	3	4	5
		//	6	7	8

		for (int i = 0; i < 3; i++) {

			String s1 = "";
			String s2 = "";
			String s3 = "";

			s1 = OX[i];
			s2 = OX[i + 3];
			s3 = OX[i + 6];

			if(!(s1.equals("null")) && !(s2.equals("null")) && !(s3.equals("null"))){

				if(s1.equals(s2) && s2.equals(s3)){

					return true;

				}
				
			}

		}

		return false;

	}

	private static boolean checkDiagonal(){

		//	0	1	2
		//	3	4	5
		//	6	7	8

		if(!(OX[0].equals("null")) && !(OX[4].equals("null")) && !(OX[8].equals("null"))){

			if(OX[0].equals(OX[4]) && OX[4].equals(OX[8])){
				return true;
			}
			
		}
			
		if(!(OX[2].equals("null")) && !(OX[4].equals("null")) && !(OX[6].equals("null"))){
		
			if(OX[2].equals(OX[4]) && OX[4].equals(OX[6])){
				return true;
			}
			
		}
		
		




		return false;

	}



	public static void updateArr(String[] s){

		OX = s; 
		checkForWin();

	}


	private static void StartGame(){

		if(oneTimeOnly){
			for (int i = 0; i < clients.size(); i++) {
				clients.get(i).startGame();
			}
			oneTimeOnly = false;
		}

		String s = "";

		for (int i = 0; i < OX.length; i++) {
			if(i == 0){
				s+= OX[i];
			}else{
				s+= " "+OX[i];
			}
		}

		System.out.println(s);


		if(flipFlop){
			clients.get(0).sendDataX(s);
			flipFlop = false;
		}else{
			clients.get(1).sendDataO(s);
			flipFlop = true;
		}


	}

	public static SingeltonServer getInstance(){

		if(ME == null){
			ME = new SingeltonServer();
		}

		return ME;

	}


	//	public static void messageAllClients(String msg) {
	//
	//		for (int i = 0; i < clients.size(); i++) {
	//
	//			ServerClient serverClient = clients.get(i);
	//
	//			if (serverClient.getName() == null) {
	//				clients.remove(i);
	//				i--;
	//			} else {
	//				serverClient.printMessage(msg);
	//			}
	//		}
	//
	//	}

	public static Group createTextBtn(String s, Font f){

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
