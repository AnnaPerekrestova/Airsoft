package com.example.airsoft.Classes;

public class RequestToGameClass {
    private String requestKey, teamName, gameName, playersCount, description, status;
    private boolean payment;

    public RequestToGameClass(String requestKey) {
        this.requestKey=requestKey;

    }
    public String getGameName(){return gameName;}
    public void setGameName(String gameName){this.gameName=gameName;}

    public String getPlayersCount(){return playersCount;}
    public void setPlayersCount(String playersCount){this.playersCount=playersCount;}

    public String getTeamName(){return teamName;}
    public void setTeamName(String teamName){this.teamName=teamName;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}

    public boolean getPayment(){return payment;}
    public void setPayment(boolean payment){this.payment=payment;}

    public String getRequestKey() {return requestKey;}
    public void setRequestKey(String requestKey) {this.requestKey = requestKey;}
}
