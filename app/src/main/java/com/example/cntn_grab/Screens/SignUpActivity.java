package com.example.cntn_grab.Screens;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cntn_grab.Business.UserBusiness.SignUpWithEmailPasswordListener;
import com.example.cntn_grab.Business.UserBusiness.UserBusiness;
import com.example.cntn_grab.Data.Type;
import com.example.cntn_grab.Data.User;
import com.example.cntn_grab.Helpers.DialogHelper;
import com.example.cntn_grab.Helpers.DialogHelperListener;
import com.example.cntn_grab.Helpers.LoadingHelper;
import com.example.cntn_grab.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements DialogHelperListener {
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mNameEditText;
    private EditText mRetypePasswordEditText;
    private EditText mPhoneNumberEditText;
    private Button mSignUpButton;

    private String mEmail;
    private String mName;
    private String mPassword;
    private String mRetypePassword;
    private String mPhoneNumber;

    private DatabaseReference mDatabaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_up);

        mEmailEditText = findViewById(R.id.sign_up_email);
        mNameEditText = findViewById(R.id.sign_up_name);
        mPasswordEditText = findViewById(R.id.sign_up_password);
        mRetypePasswordEditText = findViewById(R.id.sign_up_password_retype);
        mPhoneNumberEditText = findViewById(R.id.sign_up_phone);
        mSignUpButton = findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmailEditText.getText().toString();
                mName = mNameEditText.getText().toString();
                mPassword = mPasswordEditText.getText().toString();
                mRetypePassword = mRetypePasswordEditText.getText().toString();
                mPhoneNumber = mPhoneNumberEditText.getText().toString();

                signUp(mEmail, mName, mPassword, mRetypePassword, mPhoneNumber);
            }
        });
    }

    void signUp(String email, final String name, String password, String rePassword, final String phoneNumber) {
        UserBusiness.getInstance().setSignUpWithEmailPasswordListener(new SignUpWithEmailPasswordListener() {
            @Override
            public void signUpWithEmailPasswordDidStart() {
                LoadingHelper.getInstance().showLoading(SignUpActivity.this);
            }

            @Override
            public void signUpWithEmailPasswordDidEnd(Boolean isOk, FirebaseUser user) {
                LoadingHelper.getInstance().hideLoading(SignUpActivity.this);
                DialogHelper.getInstance().setListener(SignUpActivity.this);

                if (isOk) {
                    String uid = user.getUid();
                    String name = mName;
                    String email = user.getEmail();
                    String phone = mPhoneNumber;
                    String type = "PASSENGER";

                    // Set current user is this User
                    User newUser = new User(uid, email, name, phone, true, type);
                    UserBusiness.getInstance().setUser(newUser);

                    // Update user info to database
                    updateToDatabase(newUser);

                } else {
                    DialogHelper.getInstance().showFailedDialog(SignUpActivity.this);
                    UserBusiness.getInstance().setUser(null);
                }
            }
        });

//        if (password.equals(rePassword)) {
//            DialogHelper.getInstance().showCustomDialog("Đăng ký thất bại", "Nhập lại mật khẩu không trùng khớp", SignUpActivity.this);
//            return;
//        }

        UserBusiness.getInstance().signUpWithEmailPassword(SignUpActivity.this, email, password, phoneNumber);
    }

    private void updateToDatabase(User newUser) {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseRef.child(newUser.getId()).setValue(newUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DialogHelper.getInstance().showSuccessDialog(SignUpActivity.this);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("TON HIEU", e.toString());

                DialogHelper.getInstance().showFailedDialog(SignUpActivity.this);
            }
        });
    }

    // DialogHelperListener
    public  void successDialogOnClick() {
        this.finish();
    }

    public void failedDialogOnClick() {

    }

    public void customDialogOnClick() {
    }
}
