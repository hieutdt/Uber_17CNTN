package com.example.cntn_grab.Data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    protected String id;
    protected String email;
    protected String name;
    protected String phoneNumber;
    protected boolean isVerified;
    protected Type type;

    private DatabaseReference mDatabaseRef;

    public User() {
        this.type = Type.PASSENGER;
    }

    public User(String id, String email, String name, String phoneNumber, Boolean isVerified, Type type) {
        this.id = id;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(id);
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        mDatabaseRef.child("type").setValue(type);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
