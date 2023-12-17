import java.io.*;
import java.net.*;
import java.util.*;
/*
1. Michael Owen
2. 4/29/2023
3. Java Version: 20
4. > javac *.java
   > javac *.java
5. > java JokeServer
   > java JokeClient
   > java JokeClientAdmin
6. JokeServer.java

7. Notes: UUID is not an actual UUID format, but a randomly generated int.
   Multiple jokeservers section of checklist not implemented. Type quit, followed by pressing enter,
   to quit client and admin program. Ctr-c to quit server.
 */

class JokeClient implements Serializable
{

    public Integer cUUID;//makeshift client uuid for hashmap lookup in server
    public String cName;//name of client
    public Integer JPSelection;//joke or proverb selector
    public String textForClient;//the actual joke or proverb selected, or cycle message

    public JokeClient(String cName, Integer cUUID) {
        this.cName = cName;
        this.cUUID = cUUID;
    }


    public static void  main(String[] args)
    {

        Scanner sc = new Scanner(System.in);//scan for userInput to get name, assign an uuid, and then begin the program
        System.out.print("Type your name and hit enter to begin: ");
        String clientName = sc.nextLine();
        Integer num_uuid = new Random().nextInt(1,Integer.MAX_VALUE);
        JokeClient jokeClient = new JokeClient(clientName, num_uuid);//create a client passing name from console, and a 'uuid'
        String sLoc; //server location, localhost or 127.0.0.1 if none supplied
        int port = 4545;
        if(args.length > 0)
        {
            sLoc = args[0];//use supplied address if present in args
        }
        else {
            sLoc = "127.0.0.1";//otherwise use default
        }


        String userIn;
        do {
            System.out.println("Press enter to receive a joke or proverb from server, type quit to exit");
            System.out.flush();
            userIn = sc.nextLine();//if enter is pressed, it will be an empty string

            if(userIn.isEmpty())//if userIn string is empty, Enter was pressed
            {

                try {

                    Socket socketConnection = new Socket(sLoc, port);//connect and send serialized object 'jokeclient' to server
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socketConnection.getOutputStream());
                    objectOutputStream.writeObject(jokeClient);//marshal serialized object to server for primary functionality
                    objectOutputStream.flush();
                    InputStream inStream = socketConnection.getInputStream();//retrieve updated object sent back from server
                    ObjectInputStream ois = new ObjectInputStream(inStream);
                    jokeClient = (JokeClient) ois.readObject();

                    System.out.println(jokeClient.textForClient);//output joke or proverb stored in object variable, or cycle notification, to client console

                    socketConnection.close();//close connection
                }
                catch(IOException except) {
                    except.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        while(!userIn.contains("quit"));//continue until user types quit


    }
}


class JokeClientAdmin {

    public static void main(String[] args) {

        boolean stateSwitch = true;//keeps state instance
        int port1 = 5050;
        String sLoc; //default or supplied server as above
        if(args.length > 0)
        {
            sLoc = args[0];//supplied args from userInput
        }
        else
        {
            sLoc = "127.0.0.1";//if no address specified
        }

        Scanner scanner = new Scanner(System.in);//scanner to get input from admin

        String state;
        String consoleIn;
        do {
            System.out.println("Press enter key to change mode, or type quit");//enter to change server mode
            System.out.flush();

            consoleIn = scanner.nextLine();//catching user input

            if(consoleIn.isEmpty()) //empty means enter key was pressed, below is to switch states
            {
                if (stateSwitch) {
                    state = "Proverb";
                } else {
                    state = "Joke";
                }

                try
                {
                    Socket sockConnection = new Socket(sLoc, port1);//open socket at provided address and port specified in instructions
                    InputStreamReader inputStreamReader = new InputStreamReader((sockConnection.getInputStream()));
                    BufferedReader sReader = new BufferedReader(inputStreamReader);//create readers to get data from server
                    PrintStream sSender = new PrintStream(sockConnection.getOutputStream());//sender to return string data to server
                    sSender.println(state);//send state
                    sSender.flush();
                    String serverData = sReader.readLine(); //data from server, ie state declaration to admin console
                    System.out.println(serverData);
                    sockConnection.close();

                } catch (IOException x) {
                    x.printStackTrace();
                }
            }

            if (stateSwitch) //state switch to ensure correct mode
            {
                stateSwitch = false;
            } else
            {
                stateSwitch = true;
            }

        }
        while(!consoleIn.contains("quit"));//continue loop unless quit is typed to console


    }

}
public class JokeServer
{

    public static boolean clearJtracker = false;
    //booleans to toggle when we need to reset the possible jokes or proverbs to be sent to a client
    public static boolean clearPtracker = false;
    public static Map<Integer, Integer> clientsPHistory = new HashMap<>();
    //hashmaps that stores a representation of the clients possible jokes/proverbs as an int value, and the clients uuid as the key
    public static Map<Integer, Integer> clientsJHistory = new HashMap<>();
    public static String jpState = "Joke";//joke or proverb state

    public static void main(String[] argv) throws IOException
    {

        Socket socketConnection;
        int port = 4545;//as directed in instructions
        int q_len = 6;

        System.out.println("Michael Owen's JokeServer starting at port: " + port);

        new AdminHandler().start();

        ServerSocket serverSocket = new ServerSocket(port, q_len);//attempting to initialize waiting/connection to client


        while(true)
        {
            socketConnection = serverSocket.accept();//listening for a client to actually connect
            ServerWorker serverWorker = new ServerWorker(socketConnection);//once accepted/connected we spawn a worker thread
            Thread thread = new Thread(serverWorker);
            thread.start();
            //loops until forced-quit with ctr-c

        }
    }
}




class ServerWorker implements Runnable
{
    Socket socketConnection;
    ServerWorker(Socket socket)
    {
        socketConnection = socket;
    }

    public void run()
    {
        //connects to jokeClient and begins primary functionality of program

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socketConnection.getInputStream());//to receive object
            ObjectOutputStream objectOS = new ObjectOutputStream(socketConnection.getOutputStream());//to send back
            JokeClient client = (JokeClient) objectInputStream.readObject();//getting serialized object

            //System.out.println("Client unique identifier: " + client.cUUID);//to server console for debug purposes

            initHistory(client);//initializes the clients history of jokes/proverbs already viewed or not in a cycle

            shuffler(client);//gets potential proverb or joke and shuffles
            jpChooser(client);//chooses the proverb or joke and assigns it to client serialized object to be sent back through object outputstream below
            objectOS.writeObject(client);

            toggleReset();//ensures jokes and proverb tracker is correctly reset upon viewing every joke or proverb

            socketConnection.close();//end connection for this instance
        }
        catch(IOException | ClassNotFoundException ioexception) {ioexception.printStackTrace();}
    }

    static void toggleReset()//as described above, makes sure we're resetting if we view each joke or proverb
    {
        if(JokeServer.clearJtracker)
        {
            JokeServer.clearJtracker = false;//after reset
        }
        if(JokeServer.clearPtracker)
        {
            JokeServer.clearPtracker = false;//after reset
        }
    }

    static void initHistory(JokeClient client)//
    {
        //1111 means every joke (or proverb) is a candidate (unseen) to be sent/assigned to client object from server. Ordered to match JA,JB,JC,JD etc. Called upon initialization to be safe
        JokeServer.clientsPHistory.putIfAbsent(client.cUUID, 1111);
        JokeServer.clientsJHistory.putIfAbsent(client.cUUID, 1111);

    }


    static void shuffler(JokeClient client)
    {

        if(!JokeServer.jpState.equals("Joke"))
        {
            List<Integer> rng_indicies = new ArrayList<>();//holds indices of potential (ie unseen) jokes or proverbs, which then get shuffled
            Integer jpOptions = JokeServer.clientsPHistory.get(client.cUUID);//retrieve current state of proverbs seen/unseen for current client
            for (int i = 0; i < jpOptions.toString().length(); i++)
            {
                if (jpOptions.toString().charAt(i) == '1')//1 means unseen proverb, so we store the index as a candidate
                {
                    rng_indicies.add(i);
                }
            }
            if (rng_indicies.isEmpty() == false)
            {
                Collections.shuffle(rng_indicies, new Random());//shuffle to randomize the order of candidate proverbs. Still maintains PA->Proverb1
                client.JPSelection = rng_indicies.get(0);

            } else
            {
                client.textForClient = String.format(JokeServer.jpState + " cycle over");//assigns cycle message to client object member variable
                clearClientHist(client);//similar to the initializer, but now it updates to all unseen (1111) after a cycle (2222 -> all seen)
            }

        }

        else {
            //same as above but now for the case of jokes
            List<Integer> rng_indicies = new ArrayList<>();//same as above but now for the case of jokes
            Integer jpOptions = JokeServer.clientsJHistory.get(client.cUUID);//retrieve current state of proverbs seen/unseen for current client
            for (int i = 0; i < jpOptions.toString().length(); i++)
            {
                if (jpOptions.toString().charAt(i) == '1')//1 means unseen joke at i, so we store the index of that unseen joke
                {
                    rng_indicies.add(i);
                }
            }
            if (rng_indicies.isEmpty() == false)
            {

                Collections.shuffle(rng_indicies, new Random());
                client.JPSelection = rng_indicies.get(0);//shuffle to randomize the order of candidate jokes
            } else {
                client.textForClient = String.format(JokeServer.jpState + " cycle over");//else the client has viewed every proverb and reached a cycle. Time to reset.
                clearClientHist(client);

            }
        }

    }

    static void jpChooser(JokeClient client)//chooses and assigns clients joke or proverb based on the available, shuffled, indices correlated to JA,PA, etc.
    {

        if(JokeServer.jpState.equals("Joke"))
        {
            if(JokeServer.clearJtracker == false)
            {
                if (client.JPSelection == 0)
                {
                    client.textForClient = String.format("JA " + client.cName + ": Why are the Irish so wealthy? Because their capital is Dublin");
                }
                else if (client.JPSelection == 1)
                {
                    client.textForClient = String.format("JB " + client.cName + ": What do lawyers wear to work? Lawsuits");
                }
                else if (client.JPSelection == 2)
                {
                    client.textForClient = String.format("JC " + client.cName + ": Why don't eggs tell jokes? They crack up too easily");
                }
                else if (client.JPSelection == 3)
                {
                    client.textForClient = String.format("JD " + client.cName + ": When is a door not a door? When it's ajar");
                }
                else
                {
                    client.textForClient = String.format("something wrong");//debug purposes
                }
                setLastSeen(client.JPSelection,client.cUUID);//changes the current int value after seeing new joke, representing possible options, stored in server's hashmap after seeing a new joke
            }
        }
        else{
            if(JokeServer.clearPtracker == false)
            {
                if ( client.JPSelection == 0)
                {
                    client.textForClient = String.format("PA " + client.cName + ": Six feet of earth make all men equal");
                }
                else if (client.JPSelection == 1)
                {
                    client.textForClient = String.format("PB " + client.cName + ": Beggars can't be choosers");
                }
                else if (client.JPSelection == 2)
                {
                    client.textForClient = String.format("PC " + client.cName + ": Ignorance is bliss");
                }
                else if (client.JPSelection == 3)
                {
                    client.textForClient = String.format("PD " + client.cName + ": Familiarity breeds contempt");
                }
                else
                {
                    client.textForClient = String.format("something wrong");//debug purposes
                }
                setLastSeen(client.JPSelection,client.cUUID);//as above with jokes, changes the current value stored in hashmap after seeing a new proverb

            }
        }

    }

    static void setLastSeen(int lastSeenPos, int clientID)
    {
        //functionality of setLastSeen method described above after generating a joke. If JB is the first joke seen by client, value updated from 1111 to 1211
        ArrayList<Character> clientJPs = new ArrayList<>();
        int numb;
        char[] tempJPs  = new char[4];
        if(JokeServer.jpState.equals("Joke"))
        {

            Integer clientJokeTracker = JokeServer.clientsJHistory.get(clientID);
            for(int i = 0; i < clientJokeTracker.toString().length(); i++)
            {
                clientJPs.add(clientJokeTracker.toString().charAt(i));
            }
            clientJPs.set(lastSeenPos,'2');//set seen for that specific joke
            tempJPs[0] = clientJPs.get(0);
            tempJPs[1] = clientJPs.get(1);
            tempJPs[2] = clientJPs.get(2);
            tempJPs[3] = clientJPs.get(3);
            numb = Integer.parseInt(new String(tempJPs));//convert back to integer
            //System.out.println("numb: " + numb); debug purposes
            JokeServer.clientsJHistory.put(clientID,numb);//store in jokeservers joke hashmap corresponding to clients unique id and the viewed/unseen jokes

        }
        else
        {
            //same as above but now for proverbs
            Integer clientProvTracker = JokeServer.clientsPHistory.get(clientID);
            for(int i = 0; i < clientProvTracker.toString().length(); i++)
            {
                clientJPs.add(clientProvTracker.toString().charAt(i));
            }
            clientJPs.set(lastSeenPos,'2');//set seen for that specific proverb
            tempJPs[0] = clientJPs.get(0);
            tempJPs[1] = clientJPs.get(1);
            tempJPs[2] = clientJPs.get(2);
            tempJPs[3] = clientJPs.get(3);
            numb = Integer.parseInt(new String(tempJPs));//convert back to an integer value
            JokeServer.clientsPHistory.put(clientID,numb);//store in jokeservers prov hashmap corresponding to clients unique id and the viewed/unviewed jokes

        }
    }

    static void clearClientHist(JokeClient currentClient)
    {
        if(!JokeServer.jpState.equals("Joke"))//aka if proverb mode
        {
            JokeServer.clientsPHistory.put(currentClient.cUUID, 1111);//similar to initializer but for resetting after a cycle. Resetting for proverbs.
            JokeServer.clearPtracker = true;//indicates that a cycle has happened now, we reset the index of possible proverb below by setting to zero
            currentClient.JPSelection = 0;

        }
        else
        {
            JokeServer.clientsJHistory.put(currentClient.cUUID, 1111);//Resetting for jokes.
            JokeServer.clearJtracker = true;//indicates that a cycle has happened, now we reset the index of possible joke below by setting to zero
            currentClient.JPSelection = 0;
        }

    }

}
class StateHandler implements Runnable
{
    //works with clientadmin to update and communicate the state after changes
    Socket sConnection;
    StateHandler(Socket socket)
    {
        sConnection = socket;
    }
    public void run()
    {

        try
        {
            String state;//to be assigned to the state change performed by admin. Create connection to retrieve state.
            BufferedReader br = new BufferedReader(new InputStreamReader(sConnection.getInputStream()));
            PrintStream ops = new PrintStream(sConnection.getOutputStream());

            state = br.readLine(); //reads state change from admin and prints it to console and switches it on server
            System.out.println("Server's mode = " + state);//server console
            ops.println("Mode set to: " + state); //sent new state to admin's console responsible for switching mode
            JokeServer.jpState = state; //actually set the state for the server

            sConnection.close();
        }

        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

}
class AdminHandler extends Thread
{

    public void run()//run method for executing thread
    {
        int cAdminPort = 5050;
        int q_len = 6;
        Socket sConnection;

        try
        {
            ServerSocket serverSocket = new ServerSocket(cAdminPort, q_len);//reach clientadmin
            while (true)
            {
                sConnection = serverSocket.accept();//clientadmin has been reached
                StateHandler stateHandler = new StateHandler(sConnection);
                Thread thread = new Thread(stateHandler);
                thread.start();//spawn a State Handler/Worker to handle the changing of server states
            }
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

}
/*
CONSOLE LOG
-----------------
JokeServer: (mode starts on Joke at default, mode is displayed to server console only when changed by admin):

> java JokeServer
Michael Owen's JokeServer starting at port: 4545
Server's mode = Proverb
Server's mode = Joke
Server's mode = Proverb
Server's mode = Joke
Server's mode = Proverb
Server's mode = Joke



JokeClient (to show interleaving and multiple clients - if a joke is followed by a proverb, or vice versa, the admin changed the mode):
JA,JB, PA, PB, etc. always correspond to the same joke/proverb even with randomizing/shuffling and interleaving of multiple clients

Client 1: Joe

>java JokeClient
Type your name and hit enter to begin: Joe
Press enter to receive a joke or proverb from server, type quit to exit

JB Joe: What do lawyers wear to work? Lawsuits
Press enter to receive a joke or proverb from server, type quit to exit

JC Joe: Why don't eggs tell jokes? They crack up too easily
Press enter to receive a joke or proverb from server, type quit to exit
                                                                         --admin changes mode here
PC Joe: Ignorance is bliss
Press enter to receive a joke or proverb from server, type quit to exit

PB Joe: Beggars can't be choosers
Press enter to receive a joke or proverb from server, type quit to exit

PA Joe: Six feet of earth make all men equal
Press enter to receive a joke or proverb from server, type quit to exit
                                                                         --admin changes mode here
JD Joe: When is a door not a door? When it's ajar
Press enter to receive a joke or proverb from server, type quit to exit

JA Joe: Why are the Irish so wealthy? Because their capital is Dublin
Press enter to receive a joke or proverb from server, type quit to exit

Joke cycle over
Press enter to receive a joke or proverb from server, type quit to exit
                                                                        --admin changes mode here
PD Joe: Familiarity breeds contempt
Press enter to receive a joke or proverb from server, type quit to exit

Proverb cycle over
Press enter to receive a joke or proverb from server, type quit to exit


Client 2: Mike

>java JokeClient
Type your name and hit enter to begin: Mike
Press enter to receive a joke or proverb from server, type quit to exit

PD Mike: Familiarity breeds contempt
Press enter to receive a joke or proverb from server, type quit to exit

PC Mike: Ignorance is bliss
Press enter to receive a joke or proverb from server, type quit to exit

PA Mike: Six feet of earth make all men equal
Press enter to receive a joke or proverb from server, type quit to exit
                                                                       --admin changes mode here
JC Mike: Why don't eggs tell jokes? They crack up too easily
Press enter to receive a joke or proverb from server, type quit to exit

JB Mike: What do lawyers wear to work? Lawsuits
Press enter to receive a joke or proverb from server, type quit to exit

JA Mike: Why are the Irish so wealthy? Because their capital is Dublin
Press enter to receive a joke or proverb from server, type quit to exit
                                                                       --admin changes mode here
PB Mike: Beggars can't be choosers
Press enter to receive a joke or proverb from server, type quit to exit

Proverb cycle over
Press enter to receive a joke or proverb from server, type quit to exit
                                                                       --admin changes mode here
JD Mike: When is a door not a door? When it's ajar
Press enter to receive a joke or proverb from server, type quit to exit

Joke cycle over
Press enter to receive a joke or proverb from server, type quit to exit



JokeClientAdmin:

> java JokeClientAdmin
Press enter key to change mode, or type quit

Mode set to: Proverb
Press enter key to change mode, or type quit

Mode set to: Joke
Press enter key to change mode, or type quit

Mode set to: Proverb
Press enter key to change mode, or type quit

Mode set to: Joke
Press enter key to change mode, or type quit

Mode set to: Proverb
Press enter key to change mode, or type quit

Mode set to: Joke
Press enter key to change mode, or type quit



 */

/*
D2L POST (reply) 1:
I have my main data structures on the server. For the client class, I have member variables (such as the clients name, a unique identifier, etc.) which get serialized over to the server.
I used a rng to act as an uuid for the client, which I use as a key for hashmaps stored in the server. The key is mapped to a value which represents the four jokes/proverbs; the value also
indicates which of the four have, and haven't, been viewed by the client. This value gets updated each time a joke or proverb is sent to the client, and then after all four jokes or proverbs
have been viewed (i.e. cycle incurred) the value gets reset s.t. all jokes/proverbs become available again. My randomization/shuffling of available jokes or proverbs occurs on the server
side, but I'm sure there are many ways to do this. I'm not finished yet, but that's the basic design of my current implementation. I think the changes that you made in your implementation make sense.

D2L POST (reply regarding shuffling/randomization) 2:
Thanks for the tip, I totally forgot about Collections.shuffle(). Definitely saved me some time, much appreciated!

D2L POST (reply) 3:
Good suggestion! I've watched a few of the videos in this playlist before, and they do a great job explaining java socket programming fundamentals.

 */