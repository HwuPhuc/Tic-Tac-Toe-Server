package caroserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientHandler implements Runnable{
    private final Socket client;
    private final int threadID;
    private User user;
    private Room room;
    
    private BufferedReader in;
    private BufferedWriter out;

    private BufferedReader readFromFile;
    private BufferedWriter writeToFile;
    private Boolean isClosed;
    private String IP;
    
    public ClientHandler(Socket client, int threadID) {
        this.client = client;
        this.threadID = threadID;
        isClosed = false;
        if(this.client.getInetAddress().getHostAddress().equals("127.0.0.1")){
            IP = "127.0.0.1";
        }
        else{
            IP = this.client.getInetAddress().getHostAddress();
        }
    }

    public int getThreadID() {
        return threadID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void joinRoom() throws IOException{
        write("joined room," + room.getRoomID() + "," + room.getPassword() + "," + room.getOpponent(this.threadID).user.getUserName() + "," + this.user.getUserName());
        room.getOpponent(this.threadID).write("a friend joined your room," + room.getRoomID() + "," + room.getPassword() + "," + room.getOpponent(this.threadID).user.getUserName() + "," + this.user.getUserName());
    }
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            
            String received;
            String fileName = "user.txt";
            ArrayList<String> dataFromFile;
            ArrayList<String> copy = new ArrayList<>();
            while(!isClosed){
                received = in.readLine();
                if(received == null){
                    break;
                }
                String[] msgSplit = received.split(",");
                Boolean duplicateLog = false;
                Boolean checkLogin = false; //Dang nhap that bai
                if(msgSplit[0].equals("login")){
                    dataFromFile = readAFile(fileName);
                    if(!CaroServer.users.isEmpty()){
                        for(User u : CaroServer.users){
                            if(msgSplit[1].equals(u.getAccountName()) && msgSplit[2].equals(u.getPassword())){ 
                                // Account nay dang duoc login o noi khac
                                write("duplicate login");
                                duplicateLog = true;
                            }
                        }
                    }
                    if(!duplicateLog){ // Account nay dang khong duoc login o noi khac
                        for(String str : dataFromFile){
                            String[] dataSplit = str.split(",");
                            if(msgSplit[1].equals(dataSplit[1]) && msgSplit[2].equals(dataSplit[2])){
                                CaroServer.admin.aClientJoined(dataSplit[3]);
                                write("Login successfully," + dataSplit[0] + "," + dataSplit[1] + "," + dataSplit[2] + ","
                                        + dataSplit[3] + "," + dataSplit[4] + "," + dataSplit[5] + "," + dataSplit[6]);
                                this.user = new User(
                                        Integer.parseInt(dataSplit[0]),
                                        dataSplit[1],
                                        dataSplit[2],
                                        dataSplit[3],
                                        Integer.parseInt(dataSplit[4]),
                                        Integer.parseInt(dataSplit[5]),
                                        Integer.parseInt(dataSplit[6])
                                );
                                CaroServer.users.add(user);
                                CaroServer.admin.playerOnline(CaroServer.users.size());
                                checkLogin = true; //Dang nhap thanh cong
                                break;
                            }
                        }
                        if(!checkLogin){
                            write("Wrong account name or password");
                        }
                    }
                }
                
                if(msgSplit[0].equals("register")){
                    dataFromFile = readAFile(fileName);
                    for(String str : dataFromFile){
                        String[] dataSplit = str.split(",");
                        if (msgSplit[1].equals(dataSplit[1])){
                            write("This account name is existed"); // Trung ten tai khoan
                            break;
                        }
                        else if (msgSplit[3].equals(dataSplit[3])){ // Trung username
                            write("This username is existed");
                            break;
                        }
                        else{
                            write("Register successfully"); // Dang ky thanh cong
                            appendALineToFile(fileName, (dataFromFile.size() + 1) + "," + msgSplit[1] +
                                    "," + msgSplit[2] + "," + msgSplit[3] + ",0,0,0");
                            break;
                        }
                    }
                }
                
                dataFromFile = readAFile(fileName);
                if(msgSplit[0].equals("create room")){
                    room = new Room(this);
                    room.setPassword(msgSplit[1]);
                    write("Room created successfully," + room.getRoomID() + "," + room.getPassword() + "," + user.getUserName());
                }
                Boolean joined = false;
                if(msgSplit[0].equals("join room")){
                    int roomID = Integer.parseInt(msgSplit[1]);
                    String roomPW = msgSplit[2];
                    for (ClientHandler ch : CaroServer.clients){
                        if(ch.room != null && ch.room.getRoomID() == roomID &&
                                ch.room.getPassword().equals(roomPW) && ch.room.getPlayer2() == null){
                            ch.room.setPlayer2(this);
                            this.room = ch.room;
                            joinRoom();
                            joined = true;
                            break;
                        }
                    }
                    if(!joined){
                        write("Room not existed");
                    }
                }
                if(msgSplit[0].equals("ready")){
                    int roomID = Integer.parseInt(msgSplit[1]);
                    String username = msgSplit[2];
                    for (ClientHandler ch : CaroServer.clients){
                        if(ch.room.getRoomID() == roomID){
                            if(this.equals(room.getPlayer1())){ 
                                room.setIsReady1(true);
                                if(room.getIsReady2()){
                                    write("match begin," + roomID + ",X," + user.getUserName() + ",O,"
                                            + this.room.getOpponent(threadID).user.getUserName());
                                    room.getOpponent(this.threadID).write("match begin," + roomID + ",X,"
                                            + user.getUserName() + ",O," + this.room.getOpponent(threadID).user.getUserName());
                                    break;
                                }
                                else{
                                    room.getOpponent(this.threadID).write("your opponent is ready," + this.user.getUserName());
                                    break;
                                }
                            }
                            
                            else if(this.equals(room.getPlayer2())){
                                room.setIsReady2(true);
                                if(room.getIsReady1()){
                                    write("match begin," + roomID + ",X," +  user.getUserName() + ",O,"
                                            + this.room.getOpponent(threadID).user.getUserName());
                                    room.getOpponent(this.threadID).write("match begin," + roomID + ",X,"
                                            + user.getUserName() + ",O," + this.room.getOpponent(threadID).user.getUserName());
                                    break;
                                }
                                else{
                                    room.getOpponent(this.threadID).write("your opponent is ready," + this.user.getUserName());
                                    break;
                                }
                            }
                        }
                    }
                }
                if(msgSplit[0].equals("caro")){
                    room.getOpponent(threadID).write(received);
                } 
                if(msgSplit[0].equals("win")){
                    room.getOpponent(threadID).write(received);
                    
                    for(String str : dataFromFile){
                        String[] line = str.split(",");
                        System.out.println(str);
                        if(msgSplit[1].equals(line[3])){ //update thanh tich nguoi thang
                            int matches = Integer.parseInt(line[4]) + 1;
                            int win = Integer.parseInt(line[5]) + 1;
                            line[4] = String.valueOf(matches);
                            line[5] = String.valueOf(win);
                            str = "";
                            for(String str2 : line){
                                str += str2;
                                str += ",";
                            }
                            str = str.substring(0, str.length() - 1);
                            copy.add(str);
                        }
                        else if(line[3].equals(room.getOpponent(threadID).user.getUserName())){ // update thanh tich nguoi thua
                            int matches = Integer.parseInt(line[4]) + 1;
                            line[4] = String.valueOf(matches);
                            str = "";
                            for(String str2 : line){
                                str += str2;
                                str += ",";
                            }
                            str = str.substring(0, str.length() - 1);
                            copy.add(str);
                        }
                        else{
                            copy.add(str);
                        }
                    }
                    String dataToFile = "";
                    for(String str : copy){
                        dataToFile += str;
                        dataToFile += "\n";
                    }
                    dataToFile = dataToFile.substring(0, dataToFile.length() - 1);
                    writeALineToFile(fileName, dataToFile);
                }
                ArrayList<String> copy2 = new ArrayList<>();
                if(msgSplit[0].equals("draw")){
                    room.getOpponent(threadID).write(received);
                    
                    for(String str : dataFromFile){
                        String[] line = str.split(",");
                        System.out.println(str);
                        if(msgSplit[1].equals(line[3])){ //update thanh tich player 1
                            int matches = Integer.parseInt(line[4]) + 1;
                            int draw = Integer.parseInt(line[6]) + 1;
                            line[4] = String.valueOf(matches);
                            line[6] = String.valueOf(draw);
                            str = "";
                            for(String str2 : line){
                                str += str2;
                                str += ",";
                            }
                            str = str.substring(0, str.length() - 1);
                            copy2.add(str);
                        }
                        else if(line[3].equals(room.getOpponent(threadID).user.getUserName())){ // update thanh tich player 2
                            int matches = Integer.parseInt(line[4]) + 1;
                            int draw = Integer.parseInt(line[6] + 1);
                            line[4] = String.valueOf(matches);
                            line[6] = String.valueOf(draw);
                            str = "";
                            for(String str2 : line){
                                str += str2;
                                str += ",";
                            }
                            str = str.substring(0, str.length() - 1);
                            copy2.add(str);
                        }
                        else{
                            copy2.add(str);
                        }
                    }
                    String dataToFile = "";
                    for(String str : copy2){
                        dataToFile += str;
                        dataToFile += "\n";
                    }
                    dataToFile = dataToFile.substring(0, dataToFile.length() - 1);
                    writeALineToFile(fileName, dataToFile);
                }
                if(msgSplit[0].equals("left room")){
                    if(room != null){
                        room.getOpponent(threadID).write("your opponent left the room");
                        room.getOpponent(threadID).room = null;
                        this.room = null;
                    }
                }
                if(msgSplit[0].equals("chatWR")){
                    room.getOpponent(threadID).write(received);
                }
                if(msgSplit[0].equals("chatRoom")){
                    room.getOpponent(threadID).write(received);
                }
                if(msgSplit[0].equals("exit mid game")){
                    if(room != null){
                        room.getOpponent(threadID).write("you win bc your opponent have forfeited the game");
                        room.getOpponent(threadID).room = null;
                    }
                    for(String str : dataFromFile){
                        String[] line = str.split(",");
                        System.out.println(str);
                        if(msgSplit[1].equals(line[3])){ //update thanh tich nguoi thang
                            int matches = Integer.parseInt(line[4]) + 1;
                            int win = Integer.parseInt(line[5]) + 1;
                            line[4] = String.valueOf(matches);
                            line[5] = String.valueOf(win);
                            str = "";
                            for(String str2 : line){
                                str += str2;
                                str += ",";
                            }
                            str = str.substring(0, str.length() - 1);
                            copy.add(str);
                        }
                        else if(line[3].equals(room.getOpponent(threadID).user.getUserName())){ // update thanh tich nguoi thua
                            int matches = Integer.parseInt(line[4]) + 1;
                            line[4] = String.valueOf(matches);
                            str = "";
                            for(String str2 : line){
                                str += str2;
                                str += ",";
                            }
                            str = str.substring(0, str.length() - 1);
                            copy.add(str);
                        }
                        else{
                            copy.add(str);
                        }
                    }
                    String dataToFile = "";
                    for(String str : copy){
                        dataToFile += str;
                        dataToFile += "\n";
                    }
                    dataToFile = dataToFile.substring(0, dataToFile.length() - 1);
                    writeALineToFile(fileName, dataToFile);
                    this.room = null;
                }
                if(msgSplit[0].equals("exit game room")){
                    if(room != null){
                        room.getOpponent(threadID).write("your opponent left the game room");
                        room.getOpponent(threadID).room = null;
                        this.room = null;
                    }
                }
                if(msgSplit[0].equals("leave server")){
                    isClosed = true;
                    if(this.user != null){
                        CaroServer.admin.aClientLeft(this.user.getUserName());
                    }
                    CaroServer.clients.remove(this);
                    CaroServer.users.remove(this.user);
                    CaroServer.admin.playerOnline(CaroServer.users.size());
                    if(this.room != null){
                        if(room.getOpponent(threadID) != null){
                            try {
                                room.getOpponent(threadID).write("your opponent left the room");
                                room.getOpponent(threadID).room = null;
                                this.room = null;
                            } catch (IOException ex1) {
                                
                            }
                        }
                    }
                }
            }
        }

        catch (IOException ex) {
            
        }
        finally{
            try {
                client.close();
                in.close();
                out.close();
            } catch (IOException ex) {
                
            }
        }
    }
    public void write(String msgToSend) throws IOException{
        out.write(msgToSend);
        out.newLine();
        out.flush();
    }
    public ArrayList<String> readAFile(String fileName) throws FileNotFoundException, IOException{
        ArrayList<String> data = new ArrayList<String>();
        readFromFile = new BufferedReader(new FileReader(fileName));
        String line;
        while((line = readFromFile.readLine()) != null){
             data.add(line);
        }
        return data;
    }
    public void writeALineToFile(String fileName, String line) throws IOException{
        writeToFile = new BufferedWriter(new FileWriter(fileName));
        writeToFile.write(line);
        writeToFile.close();
    }
    public void appendALineToFile(String fileName, String line) throws IOException{
        writeToFile = new BufferedWriter(new FileWriter(fileName,true));
        writeToFile.append("\n" + line);
        writeToFile.close();
    }
}
