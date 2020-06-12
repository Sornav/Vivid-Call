 package com.noob.vividcall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

 public class Registration<firebaseUser> extends AppCompatActivity {
     //WRITTEN BY SORNAV
     // SCHOOL OF COMPUTER ENGINEERING,KIIT
     private CountryCodePicker ccp;
     private EditText phoneText;
     private EditText codeText;
     private Button continueAndNext;
     private String checker = "", phoneNumber = "";
     private RelativeLayout relativeLayout;
     private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
     private FirebaseAuth mAuth;
     private String mVerificationId;
     private PhoneAuthProvider.ForceResendingToken mResendToken;
     private ProgressDialog loadingBar;

     //HERE WE NEED TO VERIFY 2 THINGS 1ST IS THE MOBILE NUMBER AND THEN THE OTP SO WE DIVIDED INTO TWO PARTS
//
//                                  '--------IF OTP ENTERED BLANK SHOW ERROR
//        *MOBILE NUMBER VERIFIED---'
//                                  '--------IF OTP NOT ENTERED BLANK THEN VERIFY IT---CALL signInWithPhoneAuthCredential FUNCTION TO VERIFY--IF VERIFIED SEND TO DESIRED ACTIVITY
//             OR
//
//                                   '------IF MOBILE NUMBER NOT ENTERED BLANK THEN VERIFY IT----CALL  PhoneAuthProvider.getInstance().verifyPhoneNumber AND PASS MCALLBACKS WHHICH HAS ALL THE FUNCTIONS REQUIRED--IF TRUE THEN MOBILE VERIFIED NOW CHECK OTP
//     *MOBILE NUMBER NOT VERIFIED---'
//                                   '-------IF MOBILE MOBILE NUMBER ENTERED BLANK SHOW ERROR
//
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_registration);
         mAuth = FirebaseAuth.getInstance();
         loadingBar = new ProgressDialog(this);
         phoneText = findViewById(R.id.phoneText);
         codeText = findViewById(R.id.codeText);
         continueAndNext = findViewById(R.id.continueNextButton);
         relativeLayout = findViewById(R.id.phoneAuth);
         phoneText = findViewById(R.id.phoneText);
         ccp = (CountryCodePicker) findViewById(R.id.ccp);
         ccp.registerCarrierNumberEditText(phoneText);
         continueAndNext.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (continueAndNext.getText().equals("Submit") || checker.equals("Code Sent"))//HERE WE CHECK IF WE ARE AT THE SUBMIT OR THE CONTINUE SCREEN AND WE SEE THAT THIS CASE FOR THE SUBMIT WINDOW BUTTON(IF PHONE NUMBER ALREADY VERIFIED)
                                                    {                                                                                                 // Line 63 to 74 is after you press continue when it shows submit THE IF CASE IS WHEN WE ENTER NOTHING BUT STILL PRESS ENTER KEY AND THE
                                                        String verificationCode = codeText.getText().toString();                                      //AND THE ELSE CASE IS FOR WHEN WE TYPE SOMETHING AND WE NEED TO VERIFY IF IT IS CORRECT OR NOT,WE SHOW A LOADING BAR AND THEN
                                                        if (verificationCode.equals("")) //CHECKING IF BLANK OR NOT                                  //PASS THE DATA AND THE ID FOR VERFIFCATION.
                                                        {
                                                            Toast.makeText(Registration.this, "Please Write Verification Code First", Toast.LENGTH_LONG).show();

                                                        } else //IF NOT BLANK THEN VERIFY WHATS WRITTEN
                                                        {
                                                            loadingBar.setTitle("Code Verification");
                                                            loadingBar.setMessage("Please Wait while we verify your code");
                                                            loadingBar.setCanceledOnTouchOutside(false);
                                                            loadingBar.show();
                                                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                                                            signInWithPhoneAuthCredential(credential);//THIS VERIFIES THE OTP
                                                        }
                                                    } else//IF WE ARE AT THE MAIN HOME PAGE WHEN THE BUTTON IS CONTINUE(IF PHONE NUMBER NOT YET VERIFIED)
                                                    {
                                                        phoneNumber = ccp.getFullNumberWithPlus();
                                                        if (!phoneNumber.equals(""))//CHECK IF IT IS NOT EMPTY
                                                        {

                                                            loadingBar.setTitle("Phone Number Verification on Progress");
                                                            loadingBar.setMessage("Please Wait, while your Phone Number is Verified");
                                                            loadingBar.setCanceledOnTouchOutside(false);
                                                            loadingBar.show();


                                                            PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, Registration.this, mCallbacks);//VERIFYING PHONE


                                                        } else {
                                                            Toast.makeText(Registration.this, "Please Enter a Valid Number", Toast.LENGTH_LONG).show();
                                                        }

                                                    }
                                                }

                                            }


         );
         mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()//HERE WE CHECK IF PHONE VERIFICATION IS COMPLETE OR NOT AND MAKE VARIOUS FUNCTIONS
         {
             @Override
             public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {//IF PHONE NUMBER IS CORRECT
                 signInWithPhoneAuthCredential(phoneAuthCredential);//PASS DATA TO FUNCTION TO CHECK IF OTP IS CORRECT OR NOT
             }

             @Override
             public void onVerificationFailed(FirebaseException e)//IF PHONE NUMBER INCORRECT
             {
                 loadingBar.dismiss();
                 Toast.makeText(Registration.this, "Invalid Phone Number", Toast.LENGTH_LONG).show();
                 relativeLayout.setVisibility(View.VISIBLE);
                 continueAndNext.setText("Continue");
                 codeText.setVisibility(View.GONE);
             }

             @Override
             public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)//IF OTP SENT
             {
                 super.onCodeSent(s, forceResendingToken);
                 mVerificationId = s;
                 mResendToken = forceResendingToken;

                 relativeLayout.setVisibility(View.GONE);
                 checker = "Code Sent";
                 continueAndNext.setText("Submit");
                 codeText.setVisibility(View.VISIBLE);
                 loadingBar.dismiss();
                 Toast.makeText(Registration.this, "Code has been Sent", Toast.LENGTH_LONG);
             }
         };
     }
     //WE CHECK IF THE USER IS ALREADY SIGNED IN THEN NO NEED TO AGAIN SIGNIN
      @Override
      protected void onStart()  {
         super.onStart();
     FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
          if(firebaseUser != null)

     {
         Intent homeIntent = new Intent(Registration.this, MainActivity.class);
         startActivity(homeIntent);
         finish();
     }

 }







        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) //USED TO CHECK IF VERIFICATION OTP IS CORRECT
        {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())//IF SUCCESSFUL THEN THEN SEND USER TO THE INTENDED PAGE/ACTIVITY
                            {
                               loadingBar.dismiss();
                               Toast.makeText(Registration.this,"Successfully Logged in",Toast.LENGTH_LONG).show();
                               sendUserTomainActivity();
                            } else//OTHERWISE SHOW AN ERROR
                                {
                                loadingBar.dismiss();;
                                 String s=task.getException().toString();
                                Toast.makeText(Registration.this,s,Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                     );
        }
        public void sendUserTomainActivity()//USED TO SEND USER TO NEXT ACTIVITY
        {
            Intent intent=new Intent(Registration.this, MainActivity.class);
            startActivity(intent);
            finish();

    }
}
