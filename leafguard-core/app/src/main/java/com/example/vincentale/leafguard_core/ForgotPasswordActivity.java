package com.example.vincentale.leafguard_core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";
    private Button validate;
    private EditText emailTextView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        LayoutInflater inflater = this.getLayoutInflater();

        context=this;

        emailTextView= (EditText) this.findViewById(R.id.emailEditText);


       // final Context context= this;

        setContentView(R.layout.activity_forgot_password);
        validate = (Button) this.findViewById(R.id.lostPassword);
    }

    public void SentEmailValidation(View view) {
        //TODO add other cheack to the email adress
        EditText emailTextView = (EditText) findViewById(R.id.emailEditText);

        try{
            String emailAddress = emailTextView.getText().toString();
        }catch(Exception e){
            Log.d(TAG, "can't get email adress");
        }
        String emailAddress = emailTextView.getText().toString();
        //textView.setText(emailAddress);
        //Toast.makeText(ForgotPasswordActivity.this, emailAddress, Toast.LENGTH_LONG).show();
        if (emailAddress.contains("@")) {
            FirebaseAuth auth = FirebaseAuth.getInstance();

            Toast.makeText(ForgotPasswordActivity.this, "email valide",Toast.LENGTH_SHORT).show();

            auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(ForgotPasswordActivity.this, "email envoyer",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(context,"auccun compte associer à cet email",Toast.LENGTH_SHORT);
                                Log.d(TAG, "Email NOT sent.");

                            }
                        }


                    });

        } else  {

            Toast.makeText(ForgotPasswordActivity.this, "email non valide", Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(ForgotPasswordActivity.this, "email non valide",Toast.LENGTH_LONG).show();


    }
}
