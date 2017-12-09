package com.foodcityapp.esa.foodcityapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private EditText emailTxt, passTxt;
    private Button signBtn, regBtnlog;
    private FirebaseAuth Lauth;
    private DatabaseReference LDbRef;
    private ProgressDialog progress;
    private SignInButton googleSignIn;

    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Lauth = FirebaseAuth.getInstance();
        LDbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        LDbRef.keepSynced(true);

        emailTxt = (EditText) findViewById(R.id.emailTxtLogin);
        passTxt = (EditText) findViewById(R.id.passTxtLogin);
        signBtn = (Button) findViewById(R.id.signinBtn);
        regBtnlog = (Button) findViewById(R.id.regBtnlog);
        googleSignIn = (SignInButton) findViewById(R.id.googleSignInButton);


        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        progress = new ProgressDialog(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        regBtnlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            progress.setMessage("Preparing to Sign in ...");
            progress.show();
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(LoginActivity.this, "Google authentication failed...",
                        Toast.LENGTH_SHORT).show();

                progress.dismiss();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Lauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = Lauth.getCurrentUser();
                            CheckUserExist();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                        progress.dismiss();
                    }
                });
    }

    private void checkLogin() {
        String emailLoginStr = emailTxt.getText().toString().trim();
        String passLogStr = passTxt.getText().toString().trim();

        if(!TextUtils.isEmpty(emailLoginStr) && !TextUtils.isEmpty(passLogStr)){

            progress.setMessage("Signing In ...");
            progress.show();
            Lauth.signInWithEmailAndPassword(emailLoginStr, passLogStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        CheckUserExist();
                        progress.dismiss();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_LONG).show();
                        progress.dismiss();
                    }
                }
            });
        }
    }

    private void CheckUserExist() {

        if(Lauth.getCurrentUser()!=null) {
        final String userId = Lauth.getCurrentUser().getUid();


            LDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(userId)) {
                        Intent mainItent = new Intent(LoginActivity.this, MainActivity.class);
                        mainItent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainItent);
                    } else {

                        Intent setupItent = new Intent(LoginActivity.this, AccountSetupActivity.class);
                        setupItent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupItent);
                       // Toast.makeText(LoginActivity.this, "Invalid user", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
