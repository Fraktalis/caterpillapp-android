package com.fraktalis.caterpillapp.leafguard_core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
        setContentView(R.layout.activity_forgot_password);
        LayoutInflater inflater = this.getLayoutInflater();

        context=this;

        emailTextView= (EditText) this.findViewById(R.id.emailEditText);




        validate = (Button) this.findViewById(R.id.lostPassword);
        validate.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                sendEmailValidation2();

            }
        });
    }

    public void sendEmailValidation2() {
        //TODO add other cheack to the email adress
        //EditText emailTextView = (EditText) findViewById(R.id.emailEditText);

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
                                Toast.makeText(ForgotPasswordActivity.this, "email envoyé",Toast.LENGTH_LONG).show();

                            }
                        }


                    });

        } else  {

            Toast.makeText(ForgotPasswordActivity.this, "email non valide", Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(ForgotPasswordActivity.this, "email non valide",Toast.LENGTH_LONG).show();


    }



}
