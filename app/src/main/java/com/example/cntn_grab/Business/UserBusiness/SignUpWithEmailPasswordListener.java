package com.example.cntn_grab.Business.UserBusiness;

import com.google.firebase.auth.FirebaseUser;

public interface SignUpWithEmailPasswordListener {
    void signUpWithEmailPasswordDidStart();
    void signUpWithEmailPasswordDidEnd(Boolean isOk, FirebaseUser user);
}
