package com.example.airsoft.Classes;

public class MembersClass {
    private String nickname, fio, statistic;

    public MembersClass() {
    }

    public MembersClass(String nickname) {
        this.nickname = nickname;

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

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }

}
