
package caroserver;

public class User {
    //Thong tin dang nhap
    private final int userID;
    private String accountName;
    private String password;
    //Thong tin ca nhan
    private String userName;
    private int gamePlayed;
    private int winningGame;
    private int drawedGame;
    //Trang thai nguoi choi
    private Boolean online; //dang online hay offline
    private Boolean playing; //dang choi hay dang treo may

    public User(int userID, String accountName, String password, String userName, int gamePlayed, int winningGame, int drawedGame, Boolean online, Boolean playing) {
        this.userID = userID;
        this.accountName = accountName;
        this.password = password;
        this.userName = userName;
        this.gamePlayed = gamePlayed;
        this.winningGame = winningGame;
        this.drawedGame = drawedGame;
        this.online = online;
        this.playing = playing;
    }

    public User(int userID, String accountName, String password, String userName, int gamePlayed, int winningGame, int drawedGame) {
        this.userID = userID;
        this.accountName = accountName;
        this.password = password;
        this.userName = userName;
        this.gamePlayed = gamePlayed;
        this.winningGame = winningGame;
        this.drawedGame = drawedGame;
    }

    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getDrawedGame() {
        return drawedGame;
    }

    public int getGamePlayed() {
        return gamePlayed;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
    }

    public int getWinningGame() {
        return winningGame;
    }
}
