package com.example.airsoft.Classes;

public class GameClass {
    private String gameKey, gameName, gameOrgcomID, gameStatus, gameDateTime, gamePolygonID, gameWinner, gameDescription;

    public GameClass(String gameKey){
        this.gameKey=gameKey;
    }

    public String getGameKey(){return gameKey;}
    public void setGameKey(String gameKey){this.gameKey=gameKey;}

    public String getGameName(){return gameName;}
    public void setGameName(String gameName){this.gameName=gameName;}

    public String getGameOrgcomID(){return gameOrgcomID;}
    public void setGameOrgcomID(String gameOrgcomID){this.gameOrgcomID=gameOrgcomID;}

    public String getGamePolygonID(){return gamePolygonID;}
    public void setGamePolygonID(String gamePolygonID){this.gamePolygonID=gamePolygonID;}

    public String getGameDateTime(){return gameDateTime;}
    public void setGameDateTime(String gameDateTime){this.gameDateTime=gameDateTime;}

    public String getGameStatus(){return gameStatus;}
    public void setGameStatus(String gameStatus){this.gameStatus=gameStatus;}

    public String getGameDescription(){return gameDescription;}
    public void setGameDescription(String gameDescription){this.gameDescription=gameDescription;}

    public String getGameWinner(){return gameWinner;}
    public void setGameWinner(String gameWinner){this.gameWinner=gameWinner;}
}
