package com.example.airsoft.Classes;

public class TeamClass {
    private String teamKey, teamName, teamCity, teamAge, teamMeanAge;

    public TeamClass(String teamKey) {
        this.teamKey = teamKey;

    }

    public String getTeamKey(){return teamKey;}
    public void setTeamKey(String teamKey){this.teamKey=teamKey;}

    public String getTeamName(){return teamName;}
    public void setTeamName(String teamName){this.teamName=teamName;}

    public String getTeamCity(){return teamCity;}
    public void setTeamCity(String teamCity){this.teamCity=teamCity;}

    public String getTeamAge(){return teamAge;}
    public void setTeamAge(String teamAge){this.teamAge=teamAge;}

    public String getTeamMeanAge(){return teamMeanAge;}
    public void setTeamMeanAge(String teamMeanAge){this.teamMeanAge=teamMeanAge;}
}
