package com.example.airsoft;

public class GamesClass {
    private String date_time, map, winner;

    public GamesClass() {
    }

    public GamesClass(String date_time, String map, String winner) {
        this.date_time = date_time;
        this.map = map;
        this.winner = winner;

    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String datetime) {
        this.date_time = datetime;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String Map) {
        this.map = Map;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String Winner) { this.winner = Winner; }
}
