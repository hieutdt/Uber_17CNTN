package com.example.cntn_grab.Business.UserBusiness;

import com.google.firebase.auth.FirebaseUser;

public interface LogInWithEmailListener {
    void logInWithEmailDidStart();
    void logInWithEmailDidEnd(Boolean isOk, FirebaseUser firebaseUser);
}
