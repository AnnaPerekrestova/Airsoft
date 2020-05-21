package com.example.airsoft;

public class MembersClass {
    private String member_id, nickname, fio;

    public MembersClass() {
    }

    public MembersClass(String member_id, String fio, String nickname) {
        this.nickname = nickname;
        this.fio = fio;
        this.member_id = member_id;

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

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String memberID) {
        this.member_id = memberID;
    }
}
