package com.example.airsoft.Classes;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemberTeamClass {
    private String member;
    private String team;
//    private String usedTeams;

    public MemberTeamClass() {
    }

    public MemberTeamClass(String member) {
        this.member = member;
        this.team = team;
        //this.usedTeams = usedTeams;

    }

    public String getMember() {
        return member;
    }

    public void setMember(String Member) {
        this.member = Member;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String Team) {
        this.team = Team;
    }

//    public String getUsedTeams() {return usedTeams; }
//
//    public void setUsedTeams(String usedTeams) {
//        this.usedTeams = usedTeams;
//    }


}
