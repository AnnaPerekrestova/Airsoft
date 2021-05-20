package com.example.airsoft.Classes;

public class PlayerClass {
    private String playerUID, nickname, fio, statisticGames, statisticPercent;

    public PlayerClass(String playerUID) {
        this.playerUID = playerUID;

    }

    public String getPlayerUID() {
        return playerUID;
    }

    public void setPlayerUID(String playerUID) {
        this.playerUID = playerUID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nick) {
        this.nickname = nick;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String FIO) {
        this.fio = FIO;
    }

    public String getStatisticGames() {
        return statisticGames;
    }

    public void setStatisticGames(String statisticGames) {
        this.statisticGames = statisticGames;
    }

    public String getStatisticPercent() {
        return statisticPercent;
    }

    public void setStatisticPercent(String statisticPercent) { this.statisticPercent = statisticPercent; }

}
