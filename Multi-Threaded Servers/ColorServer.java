import java.net.ServerSocket;
import java.net.Socket;
/* 1: Michael Owen
 * 2: 2023-04-05
 * 3: Java Version: 20 (build 20+36-2344)
 * 4: 
 * > javac *.java
 * 5:
 * > java ColorServer
 * > java ColorClient
 * 6:
 * a. ColorServer.java
 * b. ColorClient.java
 * c. ColorData.Java
 * d. ColorWorker.java
 */
public class ColorServer {
    public static void main(String[] args) throws Exception
  {
    int q_len = 6; //Simulataneous OS requests
    int serverPort = 45565;
    Socket sock;
    System.out.println("Clark Elliott's Color Server 1.0 starting up, listening at port " + serverPort + ".\n");
    
    //Wait or listen for connections using doorbell socket
    ServerSocket servSock = new ServerSocket(serverPort, q_len);
    System.out.println("ServerSocket awaiting connections..."); 
    while (true) { 
      sock = servSock.accept();   // waiting until client connects
      // Display our server port + what the system gave us via "Get next available port" for the client connection:
      System.out.println("Connection from " + sock); 
      new ColorWorker(sock).start();//Spawn a worker thread to handle, and instantly listen for the next connection.
    }
  }
    
}
import java.io.*;
import java.net.*;
import java.util.Scanner;
/* 1: Michael Owen
 * 2: 2023-04-05
 * 3: Java Version: 20 (build 20+36-2344)
 * 4: 
 * > javac *.java
 * 5:
 * > java ColorServer
 * > java ColorClient
 * 6:
 * a. ColorServer.java
 * b. ColorClient.java
 * c. ColorData.Java
 * d. ColorWorker.java
 */
public class ColorClient {
    private static int clientColorCount = 0;
  public static void main(String argv[]) {
    ColorClient cc = new ColorClient(argv);
    cc.run(argv);
  }

  public ColorClient(String argv[]) { // Constructor
    System.out.println("\nThis is the constructor if you want to use it.\n");
  }

  public void run(String argv[]) {
    
      String serverName;
      if (argv.length < 1) serverName = "localhost"; // scan for server or IP address passed through stdin.
      else serverName = argv[0];

	String colorFromClient = "";
	Scanner consoleIn = new Scanner(System.in);
	System.out.print("Enter your name: ");
	System.out.flush ();
	String userName = consoleIn.nextLine();
	System.out.println("Hi " + userName);
	do {
	  System.out.print("Enter a color, or quit to end: ");
	  colorFromClient = consoleIn.nextLine();
	  if (colorFromClient.indexOf("quit") < 0){ //quit not entered
	    getColor(userName, colorFromClient, serverName);
	  }
	} while (colorFromClient.indexOf("quit") < 0);//quit entered
	System.out.println ("Cancelled by user request.");
	System.out.println (userName + ", You sent and received " + clientColorCount + " colors.");
  }

  void getColor(String userName, String colorFromClient, String serverName)
  {
    try{
      ColorData colorObj = new ColorData(); // Defined in ColorServer, must compile that first.
      colorObj.userName = userName;
      colorObj.colorSent = colorFromClient;
      colorObj.colorCount = clientColorCount;
      Socket socket = new Socket(serverName, 45565);//local port where server is listening
      System.out.println("\nWe have successfully connected to the ColorServer at port 45,565");//client connected to servers port 45565
      OutputStream OutputStream = socket.getOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(OutputStream); //serialize object to be sent
      
      oos.writeObject(colorObj); // send serialized java obj over network to server
      System.out.println("We have sent the serialized values to the ColorServer's server socket");
      InputStream InStream = socket.getInputStream();
      ObjectInputStream ois = new ObjectInputStream(InStream);
      ColorData InObject = (ColorData) ois.readObject(); //randomized color data sent from server to client

      // Important to note! We are maintaining the conversation state while using a connectionless protocol:
      clientColorCount = InObject.colorCount; // conversation state and color count maintained + saved in class variable (connectionless protocol)
      System.out.println("\nFROM THE SERVER:");
      System.out.println(InObject.messageToClient);
      System.out.println("The color sent back is: " + InObject.colorSentBack);
      System.out.println("The color count is: " + InObject.colorCount + "\n");
      System.out.println("Closing the connection to the server.\n");

      socket.close();
    } catch (ConnectException CE){
      System.out.println("\nOh no. The ColorServer refused our connection! Is it running?\n");
      CE.printStackTrace();
    } catch (UnknownHostException UH){
      System.out.println("\nUnknown Host problem.\n"); // Test by commenting out / uncommenting out above.
      UH.printStackTrace();
    } catch(ClassNotFoundException CNF){// class exception printed to console.
	CNF.printStackTrace();
    } catch (IOException IOE){
      IOE.printStackTrace(); // output exception to stdout.
    }
  }
    
}
import java.io.Serializable;
/* 1: Michael Owen
 * 2: 2023-04-05
 * 3: Java Version: 20 (build 20+36-2344)
 * 4: 
 * > javac *.java
 * 5:
 * > java ColorServer
 * > java ColorClient
 * 6:
 * a. ColorServer.java
 * b. ColorClient.java
 * c. ColorData.Java
 * d. ColorWorker.java
 */

public class ColorData implements Serializable {
    String userName;
    String colorSent;
    String colorSentBack;
    String messageToClient;
    int colorCount;
    
}

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
/* 1: Michael Owen
 * 2: 2023-04-05
 * 3: Java Version: 20 (build 20+36-2344)
 * 4: 
 * > javac *.java
 * 5:
 * > java ColorServer
 * > java ColorClient
 * 6:
 * a. ColorServer.java
 * b. ColorClient.java
 * c. ColorData.Java
 * d. ColorWorker.java
 */

class ColorWorker extends Thread {    // Class definition. Worker threads can run simultaneously.
    Socket sock;                        // Class member, socket, local to ColorWorker.
    ColorWorker (Socket s) {sock = s;}  // Constructor, assign arg s (random socket num. for client from operating systen) to local sock
  
    public void run(){
      try{
        // Read I/O object streams in/out from the socket, then read in and deserialize object sent from the client        
        InputStream InStream = sock.getInputStream();
        ObjectInputStream ObjectIS = new ObjectInputStream(InStream);
        ColorData InObject = (ColorData) ObjectIS.readObject(); // We now have random access to the color data object.
        OutputStream outStream = sock.getOutputStream();
        ObjectOutputStream objectOS = new ObjectOutputStream(outStream);
        System.out.println("\nFROM THE CLIENT:\n");
        System.out.println("Username: " + InObject.userName);
        System.out.println("Color sent from the client: " + InObject.colorSent);
        System.out.println("Connections count (State!): " + (InObject.colorCount + 1));
        InObject.colorSentBack = getRandomColor();
        InObject.colorCount++;
        InObject.messageToClient = String.format("Thanks %s for sending the color %s", InObject.userName, InObject.colorSent);
        objectOS.writeObject(InObject);
        System.out.println("Closing the client socket connection...");
        sock.close();//close client socket connection
        
      } catch(ClassNotFoundException CNF){
        CNF.printStackTrace(); //server class - compile first
      } catch (IOException x){
        System.out.println("Server error.");
        x.printStackTrace();
      }
    }
    String getRandomColor(){
      String[] colorArray = new String[]
        {
         "Red", "Blue", "Green", "Yellow", "Magenta", "Silver", "Aqua", "Gray", "Peach", "Orange"
        };
      
      int randomArrayIndex = (int) (Math.random() * colorArray.length);//randomly return color to server and then send to client
      return (colorArray[randomArrayIndex]);
    }
  }
  

