package com.example.airsoft.Classes;

public class GamesClass {
    private String game_id, date_time, map, winner, usedTeams, memberTeamID;

    public GamesClass() {
    }

    public GamesClass(String game_id, String date_time, String map, String winner, String usedTeams, String memberTeamID) {
        this.game_id = game_id;
        this.date_time = date_time;
        this.map = map;
        this.winner = winner;
        this.usedTeams = usedTeams;
        this.memberTeamID = memberTeamID;
    }

    public String getGame_id() { return game_id; }

    public void setGame_id(String gameID) {
        this.game_id = gameID;
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

    public String getUsedTeams() {
        return usedTeams;
    }

    public void setUsedTeams(String usedTeams) { this.usedTeams = usedTeams; }

    public String getMemberTeamID() {
        return memberTeamID;
    }

    public void setMemberTeamID(String memberTeamID) { this.memberTeamID = memberTeamID; }

}
