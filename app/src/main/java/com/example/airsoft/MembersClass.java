package com.example.airsoft;

public class MembersClass {
    private String nickname, fio;

    public MembersClass() {
    }

    public MembersClass(String fio, String nickname) {
        this.nickname = nickname;
        this.fio = fio;

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


}
