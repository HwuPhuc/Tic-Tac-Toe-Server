package caroserver;

public class Room {
    private final int roomID;
    private final ClientHandler player1;
    private Boolean isReady1;
    private ClientHandler player2;
    private Boolean isReady2;
    private String password;

    public Room(ClientHandler player1) {
        this.player1 = player1;
        this.isReady1 = false;
        this.player2 = null;
        this.isReady2 = false;
        this.roomID = (int) Math.floor(Math.random() * 1000);
        this.password = "";
    }

    public Boolean getIsReady1() {
        return isReady1;
    }

    public void setIsReady1(Boolean isReady1) {
        this.isReady1 = isReady1;
    }

    public Boolean getIsReady2() {
        return isReady2;
    }

    public void setIsReady2(Boolean isReady2) {
        this.isReady2 = isReady2;
    }

    public int getRoomID() {
        return roomID;
    }

    public ClientHandler getPlayer1() {
        return player1;
    }

    public ClientHandler getPlayer2() {
        return player2;
    }

    public void setPlayer2(ClientHandler player2) {
        this.player2 = player2;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public ClientHandler getOpponent(int threadID){
        if (player1.getThreadID() == threadID)
            return player2;
        return player1;
    }
}
