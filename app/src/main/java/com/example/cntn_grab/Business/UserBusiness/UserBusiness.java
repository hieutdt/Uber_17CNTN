package com.example.cntn_grab.Business.UserBusiness;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.cntn_grab.Data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class UserBusiness {
    private static UserBusiness mInstance;

    private SignUpWithPhoneListener mSignUpWithPhoneListener;
    private SignUpWithEmailPasswordListener mSignUpWithEmailPasswordListener;
    private LogInWithEmailListener mLogInWithEmailListener;

    private User mUser;
    private String verificationId;

    private UserBusiness() {
        mUser = new User();
    }

    public static UserBusiness getInstance() {
        if (mInstance == null)
            mInstance = new UserBusiness();
        return mInstance;
    }

    public boolean hasLoggedInUser() {
        if (mUser == null)
            return false;
        if (mUser.getId() != "")
            return false;

        return true;
    }

    public void signUpWithEmailPassword(Activity activity, String email, String password, String phone) {
        mSignUpWithEmailPasswordListener.signUpWithEmailPasswordDidStart();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    mSignUpWithEmailPasswordListener.signUpWithEmailPasswordDidEnd(true, user);
                } else {
                    mSignUpWithEmailPasswordListener.signUpWithEmailPasswordDidEnd(false, null);
                }
            }
        });

    }

    public void signUpWithPhoneNumber(Activity activity, String phoneNumber) {
        mSignUpWithPhoneListener.signUpWithPhoneDidStart();

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verificationId = s;
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallbacks);
    }

    public void logInWithEmail(Activity activity, String email, String password) {
        mLogInWithEmailListener.logInWithEmailDidStart();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    mLogInWithEmailListener.logInWithEmailDidEnd(true, user);
                } else {
                    mLogInWithEmailListener.logInWithEmailDidEnd(false, null);
                }
            }
        });
    }

    public void setSignUpWithPhoneListener(SignUpWithPhoneListener listener) {
        mSignUpWithPhoneListener = listener;
    }

    public void setSignUpWithEmailPasswordListener(SignUpWithEmailPasswordListener listener) {
        mSignUpWithEmailPasswordListener = listener;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public User getCurrentUser() {
        return mUser;
    }
}

