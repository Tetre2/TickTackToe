package Server_Side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerClient {

	private BufferedReader from_client;
	private PrintStream to_client;
	private String name;

	public String[] OX = new String[9];

	public ServerClient(Socket connection) {

		try {

			from_client = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			to_client = new PrintStream(connection.getOutputStream());

		} catch (IOException e) {

			e.printStackTrace();
		}

		Runnable input_listener = () -> {
			while (true) {
				try {
					String input = from_client.readLine();

					if (input.charAt(0) == '/') {
						if(input.charAt(1) == '1'){

							String cmd = input.substring(2);

							SingeltonServer.updateArr(cmd.split(" "));

						}
					}

					if (input.charAt(0) == '&') {

						SingeltonServer.playersReady++;

					} 


				} catch (IOException e) {
					// Tappat connection
					System.out.println("Conection Lost to client");
					break;
				}
			}
		};

		new Thread(input_listener).start();

	}

	public void sendDataO(String s){
		to_client.println("&"+s);
	}

	public void sendDataX(String s){
		to_client.println("%"+s);
	}

	public void toClient(String msg) {
		to_client.println(msg);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String s) {
		this.name = s;
		to_client.println("!"+name);
	}

	public void startGame(){
		to_client.println("/startgame");
	}
	
	public void setEnding(){
		to_client.println("/endgame");
	}


}
