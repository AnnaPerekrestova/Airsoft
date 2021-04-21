package com.example.airsoft.Classes;

public class RequestClass {
    private String requestKey, playerFIO, userUID, teamName, status;

    public RequestClass(String requestKey) {
        this.requestKey=requestKey;

    }
    public String getPlayerFIO(){return playerFIO;}
    public void setPlayerFIO(String playerFIO){this.playerFIO=playerFIO;}

    public String getUserUID(){return userUID;}
    public void setUserUID(String userUID){this.userUID=userUID;}

    public String getTeamName(){return teamName;}
    public void setTeamName(String teamName){this.teamName=teamName;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}

    public String getRequestKey() {return requestKey;}

    public void setRequestKey(String requestKey) {this.requestKey = requestKey;}
}
