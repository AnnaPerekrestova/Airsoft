package com.example.airsoft.Classes;

import android.util.Log;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MemberTeamClass {
    private String member;
    private Boolean playF;

    public MemberTeamClass(String member) {
        this.member = member;
        this.playF = playF;

    }

    public String getMember() {
        return member;
    }

    public void setMember(String Member) {
        this.member = Member;
    }

    public Boolean getPlay() {
        return playF;
    }

    public void setPlayf(Boolean playF) {
        this.playF = playF;
    }

}
