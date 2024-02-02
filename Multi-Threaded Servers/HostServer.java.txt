import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
/*
1. Michael Owen
2. 5/28/2023
3. Java Version: 20
4. > javac *.java
5. > java HostServer
6. HostServer.java

Comments are my own. Code provided by Professor Clark Elliott.

 */
class AgentWorker extends Thread { // AgentListener thread called or instantiated in HostServer's main method, then AgentWorker is instantiated and ran in the Listener thread

    Socket sock;
    // socket for connecting to client
    agentHolder parentAgentHolder;
    // here we store an agent object as a class attribute, which is responsible for keeping state of the agent and holding socket and counter info
    int localPort;
    // current port being used by worker to handle current request


    AgentWorker (Socket s, int prt, agentHolder ah) { // constructor called in Listener thread. Worker thread started upon creation.
        // make local class variable assignments from constructor args/params
        sock = s;
        localPort = prt;
        parentAgentHolder = ah;
    }
    public void run() {

        PrintStream out = null;
        BufferedReader in = null;
        String NewHost = "localhost"; // new host always localhost
        int NewHostMainPort = 4242; // main port we are working under
        String buf = "";
        int newPort;
        Socket clientSock;
        BufferedReader fromHostServer;
        PrintStream toHostServer;

        try {
            out = new PrintStream(sock.getOutputStream()); // where we will be sending updated html header to, via agentListener static method
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            // in captures requests from client

            String inLine = in.readLine();
            // get client input
            StringBuilder htmlString = new StringBuilder(); // string to eventually send back to client


            System.out.println();
            System.out.println("Request line: " + inLine);
            // print request information from client to servers console

            if(inLine.indexOf("migrate") > -1) { //if the input/client-request is to migrate to a diff port

                clientSock = new Socket(NewHost, NewHostMainPort); // migrate. Create new socket at the main port (4212) and localhost address
                fromHostServer = new BufferedReader(new InputStreamReader(clientSock.getInputStream())); // open reader at client sock and send request to main to get next available port
                toHostServer = new PrintStream(clientSock.getOutputStream());
                toHostServer.println("Please host me. Send my port! [State=" + parentAgentHolder.agentState + "]");
                // prints the state to main's console telling the state of the current agent to handle the clients port after migrating
                toHostServer.flush();


                for(;;) { // wait until we get the port to migrate to from client response
                    buf = fromHostServer.readLine();
                    if(buf.indexOf("[Port=") > -1) { // store the string containing the port, break out of loop and extract it.
                        break;
                    }
                }

                // string manipulation to get the port we are migrating to and then convert to an int
                String tempbuf = buf.substring( buf.indexOf("[Port=")+6, buf.indexOf("]", buf.indexOf("[Port=")) );
                newPort = Integer.parseInt(tempbuf);
                System.out.println("newPort is: " + newPort); // print the port we migrated to console

                htmlString.append(AgentListener.sendHTMLheader(newPort, NewHost, inLine)); // add contents for html that will be sent to client browser
                htmlString.append("<h3>We are migrating to host " + newPort + "</h3> \n"); // display migration action in html to clients browser
                htmlString.append("<h3>View the source of this page to see how the client is informed of the new location.</h3> \n");
                // telling client to view html contents to see the transition from the perspective of the client
                htmlString.append(AgentListener.sendHTMLsubmit()); // appends the html submit action to the webpage, which triggers a request when pressed by client

                System.out.println("Killing parent listening loop."); // stop the listening at the parent port
                ServerSocket ss = parentAgentHolder.sock; // get the previous port that we will now close after migration
                ss.close(); // previous port closed


            } else if(inLine.indexOf("person") > -1) { // if not migrating, we update the state of the agent. Not sure if this scan/check is 100% necessary after the migration check/scan
                parentAgentHolder.agentState++;
                htmlString.append(AgentListener.sendHTMLheader(localPort, NewHost, inLine));
                // display to client's browser in html the updated agent state the client is conversing with
                htmlString.append("<h3>We are having a conversation with state   " + parentAgentHolder.agentState + "</h3>\n");
                htmlString.append(AgentListener.sendHTMLsubmit());

            } else {
                // migrating, or text not entered before submitting, error handling
                htmlString.append(AgentListener.sendHTMLheader(localPort, NewHost, inLine));
                htmlString.append("You have not entered a valid request!\n");
                htmlString.append(AgentListener.sendHTMLsubmit());


            }
            AgentListener.sendHTMLtoStream(htmlString.toString(), out); // send over the contents to the socket being handled by current agent
            // using static send method in AgentListener, supplying html formatted-string, and the current socket's out-stream

            sock.close(); // close connection to current agent's socket after sending


        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

}

class agentHolder { // agent holder object, kind of like objects we've serialized in past assignment but object marshalling/serialization not necessary here
    ServerSocket sock; // socket for agent, assigned via constructor called in AgentListener
    int agentState; // integer to hold the current state of the agent

    agentHolder(ServerSocket s) { sock = s;} // instantiated in agentListener to keep track of the state of the agent at the socket
}

class AgentListener extends Thread { // listen to ports and wait for requests. Instantiated and started by HostServer when a new client connects to localhost:4242
    Socket sock; // socket and port variables we're getting passed from main and listening at, eventually letting the worker thread handle
    int localPort;

    AgentListener(Socket As, int prt) { // Listener thread object created and ran in main, scans
        sock = As;
        localPort = prt;
    }
    int agentState = 0; // holds the agentState which will then be assigned to the agentHolder's class variable. Updated/incremented in Worker thread.

    // thread to run at the current port we're listening at after a request
    public void run() {
        BufferedReader in = null;
        PrintStream out = null;
        String NewHost = "localhost";
        System.out.println("In AgentListener Thread");
        try {
            String buf;
            out = new PrintStream(sock.getOutputStream()); // where we will be sending the html data initially
            in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));

            buf = in.readLine(); // get input

            if(buf != null && buf.indexOf("[State=") > -1) { // get the state and perform string manipulation to extract the state and convert to integer
                String tempbuf = buf.substring(buf.indexOf("[State=")+7, buf.indexOf("]", buf.indexOf("[State=")));
                agentState = Integer.parseInt(tempbuf); // if state is specified, as it should when new client initially connects, store it so we can update the state in worke thread
                System.out.println("agentState is: " + agentState); // send the state of the agent to console

            }

            System.out.println(buf);
            StringBuilder htmlResponse = new StringBuilder(); // build string to hold initial html response data for client making initial connection
            htmlResponse.append(sendHTMLheader(localPort, NewHost, buf)); // initial header to be displayed to client
            htmlResponse.append("Now in Agent Looper starting Agent Listening Loop\n<br />\n");
            htmlResponse.append("[Port="+localPort+"]<br/>\n"); // display the initial port before any requests when client connects to webpage
            htmlResponse.append(sendHTMLsubmit());
            sendHTMLtoStream(htmlResponse.toString(), out); // actually send html data to the output stream at the current socket

            ServerSocket servsock = new ServerSocket(localPort,2); // open a connection before we wait. Letting 2 proc. access this port?
            agentHolder agenthold = new agentHolder(servsock); // instantiate holder object and assign the current state (0 initially)
            agenthold.agentState = agentState;

            while(true) {
                sock = servsock.accept(); // wait for a client connection
                // display when we get a connection to console, along with the port
                System.out.println("Got a connection to agent at port " + localPort); // text to console after client submits text
                new AgentWorker(sock, localPort, agenthold).start();
                // now let agent handle the current connection by spawning the worker thread, passing address info and agentHolder object
            }

        } catch(IOException ioe) {
            System.out.println("Either connection failed, or just killed listener loop for agent at port " + localPort);
            // exception that is caught and displayed when client migrates
            System.out.println(ioe);
        }
    }

    static String sendHTMLheader(int localPort, String NewHost, String inLine) {
        // called in worker and listener thread, listener displays initial html
        // worker updates html after client requests and calls this method

        StringBuilder htmlString = new StringBuilder();
        // basic html syntax with request and port info appended to stringBuilder
        htmlString.append("<html><head> </head><body>\n");
        htmlString.append("<h2>This is for submission to PORT " + localPort + " on " + NewHost + "</h2>\n");
        htmlString.append("<h3>You sent: "+ inLine + "</h3>");
        htmlString.append("\n<form method=\"GET\" action=\"http://" + NewHost +":" + localPort + "\">\n");
        htmlString.append("Enter text or <i>migrate</i>:");
        htmlString.append("\n<input type=\"text\" name=\"person\" size=\"20\" value=\"YourTextInput\" /> <p>\n");

        return htmlString.toString();
        // now convert to one html-formatted string, with the help of stringBuilder.
        // could the sendHTMLsubmit actions be included here? Is the below submit method redundant?
    }
    static String sendHTMLsubmit() {
        // used to append onto to the html header so now that it can handle the input action via pressing submit
        return "<input type=\"submit\" value=\"Submit\"" + "</p>\n</form></body></html>\n";
    }

    static void sendHTMLtoStream(String html, PrintStream out) {
        // method for actually sending the response to the browser with the proper info so it can be processed
        // correct header info passed so no issues with browser
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Length: " + html.length());
        out.println("Content-Type: text/html");
        out.println("");
        out.println(html);
        // printing/sending to supplied client socket output stream
    }

}

public class HostServer {
    public static int NextPort = 3000;
    // first port, but really first port is 3001 since we add 1 to each new port needed after client migration request or new client browser connection

    public static void main(String[] a) throws IOException {
        int q_len = 6;   // not important, number of sim. requests for os to handle
        int port = 4242; // main port that we will be working under
        Socket sock;

        ServerSocket servsock = new ServerSocket(port, q_len);
        System.out.println("Elliott/Reagan DIA Master receiver started at port 4242.");
        System.out.println("Connect from 1 to 3 browsers using \"http:\\\\localhost:4242\"\n");
        while(true) { // listen for new requests at main port, or for migrations
            NextPort = NextPort + 1; // increase the port by 1, pause/wait until connection is made with client. First connection will be at localhost at port 3001
            // alternative method could be using servsock.getLocalPort() to get a random free port,
            // however im unsure how this would work here since it requires passing 0 to ServerSocket (i.e. ServerSocket(0))
            // bc we need to specify 4242 initially, create two ServerSocket objects? Maybe handle this in AgentListener?
            sock = servsock.accept(); // socket waiting
            System.out.println("Starting AgentListener at port " + NextPort); // first output to console
            new AgentListener(sock, NextPort).start();
            // instantiate and start listener object thread to listen to requests at the current/provided port
        }

    }
}
