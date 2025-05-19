
package caroserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import view.frmAdminServer;

public class CaroServer {
    private static final int PORT = 7777;
    private static int playerOnline = 0;
    public static frmAdminServer admin;
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static ServerSocket serverSocket;
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        serverSocket = null;
        int threadNum = 0;
        try {
            serverSocket = new ServerSocket (PORT);
        } catch (IOException ex) {
            System.exit(0);
        }
        admin = new frmAdminServer();
        admin.run();
        while(true){
            try {
                if(serverSocket.isClosed()){
                    break;
                }
                System.out.println("Server is opened and waiting for client");
                Socket clientSocket = serverSocket.accept();
                System.out.println("A client has just connected to the server");
                ClientHandler clientHandler = new ClientHandler(clientSocket, threadNum++);
                clients.add(clientHandler);
                threadNum++;
                Thread t;
                t = new Thread(clientHandler);
                t.start();
            }
            catch (IOException ex) {
                
            }
        }
    }
    public static int aPlayerOut(){
        playerOnline--;
        return playerOnline;
    }
}
