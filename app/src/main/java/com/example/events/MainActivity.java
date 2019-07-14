package com.example.events;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton login;
    private TextView first_name;
    private CallbackManager callmanager;
    private FirebaseAuth mAuth;
    public static final String TAG="FACELOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // login.registerCallback
        login=findViewById(R.id.login_button);
        first_name=findViewById(R.id.f_name);
        callmanager=CallbackManager.Factory.create();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
//        login.setReadPermissions(Arrays.asList("email","public profile"));
        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this,Arrays.asList("email","public profile"));
        //inorder to respod to login result we need to register some callback methods
        LoginManager.getInstance().registerCallback(callmanager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            updateUI();
        }
        
    }

    private void updateUI()
    {
        Toast.makeText(this, "you are already logged in", Toast.LENGTH_SHORT).show();
        Intent go=new Intent(MainActivity.this,Home.class);
        startActivity(go);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callmanager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker token_tracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
        {
            if (currentAccessToken==null)
            {
                first_name.setText("");
                Toast.makeText(MainActivity.this, "user logout", Toast.LENGTH_SHORT).show();
            }
            else
            {
                getinfofromFB(currentAccessToken);
            }
        }
    };

    public void getinfofromFB(AccessToken newaccesstoken)
    {
         GraphRequest graphRequest=GraphRequest.newMeRequest(newaccesstoken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                try {
                    String first_nam=object.getString("first name");

                    first_name.setText(first_nam);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //now we need to specify the parameters for the request in the form of bundle object
        Bundle parameters=new Bundle();
        parameters.putString("fields","first name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }
}
