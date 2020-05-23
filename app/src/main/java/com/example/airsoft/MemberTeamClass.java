package com.example.airsoft;

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


    public MemberTeamClass() {
    }

    public MemberTeamClass(String member) {
        this.member = member;
        this.team = team;

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


}
