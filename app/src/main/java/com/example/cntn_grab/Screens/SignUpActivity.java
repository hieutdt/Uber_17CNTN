package com.example.cntn_grab.Screens;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cntn_grab.Business.UserBusiness.SignUpWithEmailPasswordListener;
import com.example.cntn_grab.Business.UserBusiness.UserBusiness;
import com.example.cntn_grab.Helpers.LoadingHelper;
import com.example.cntn_grab.R;
import com.google.firebase.auth.FirebaseUser;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class SignUpActivity extends AppCompatActivity {
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mRetypePasswordEditText;
    private EditText mPhoneNumberEditText;
    private Button mSignUpButton;

    private String mEmail;
    private String mPassword;
    private String mRetypePassword;
    private String mPhoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        mEmailEditText = findViewById(R.id.sign_up_email);
        mPasswordEditText = findViewById(R.id.sign_up_password);
        mRetypePasswordEditText = findViewById(R.id.sign_up_password_retype);
        mPhoneNumberEditText = findViewById(R.id.sign_up_phone);
        mSignUpButton = findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmailEditText.getText().toString();
                mPassword = mEmailEditText.getText().toString();
                mRetypePassword = mRetypePasswordEditText.getText().toString();
                mPhoneNumber = mPhoneNumberEditText.getText().toString();

                signUp(mEmail, mPassword, mRetypePassword, mPhoneNumber);
            }
        });
    }

    void signUp(String email, String password, String rePassword, String phoneNumber) {
        UserBusiness.getInstance().setSignUpWithEmailPasswordListener(new SignUpWithEmailPasswordListener() {
            @Override
            public void signUpWithEmailPasswordDidStart() {
                LoadingHelper.getInstance().showLoading(SignUpActivity.this);
            }

            @Override
            public void signUpWithEmailPasswordDidEnd(Boolean isOk, FirebaseUser user) {
                LoadingHelper.getInstance().hideLoading(SignUpActivity.this);

                if (isOk) {
                    MaterialDialog mDialog = new MaterialDialog.Builder(SignUpActivity.this)
                            .setTitle("Delete?")
                            .setMessage("Are you sure want to delete this file?")
                            .setCancelable(false)
                            .setPositiveButton("Delete", R.drawable.ic_dialog_close_light, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    // Delete Operation
                                }
                            })
                            .setNegativeButton("Cancel", R.drawable.ic_mr_button_connected_26_light, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .build();

                    // Show Dialog
                    mDialog.show();
                }
            }
        });

        UserBusiness.getInstance().signUpWithEmailPassword(SignUpActivity.this, email, password, phoneNumber);
    }
}
