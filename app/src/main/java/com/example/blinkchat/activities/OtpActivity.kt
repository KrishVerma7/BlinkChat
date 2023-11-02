package com.example.blinkchat.activities

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.PhoneAuthProvider

import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.blinkchat.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import java.util.concurrent.TimeUnit

const val PHONE_NUMBER = "phoneNumber"

class OtpActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var phoneNumber: String? = null
    lateinit var verifyTv: TextView
    lateinit var waitingTv: TextView
    lateinit var resendBtn: Button
    lateinit var counterTv: TextView
    lateinit var sentcodeEt: EditText
    var mVerificationId: String? = null
    var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    lateinit var progressDialog: ProgressDialog

    //    lateinit var progressBar: ProgressBar
    private var mCounterDown: CountDownTimer? = null
    private var timeLeft: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        initViews()
        showTimer(60000)
        startVerify()
    }

    private fun startVerify() {
        startPhoneNumberVerification(phoneNumber!!)
        showTimer(60000)
        progressDialog = createProgressDialog("Sending a verification code", false)
        progressDialog.show()

    }

    private fun initViews() {
        val verificationBtn: MaterialButton = findViewById(R.id.verificationBtn)
        resendBtn = findViewById(R.id.resendBtn)
        verifyTv = findViewById(R.id.verifyTv)
        sentcodeEt = findViewById(R.id.sentcodeEt)
//        progressBar = findViewById(R.id.pbLoading)

        phoneNumber = intent.getStringExtra(PHONE_NUMBER)
        verifyTv.text = getString(R.string.verify_number, phoneNumber)
        setSpannableString()

        verificationBtn.setOnClickListener(this)
        resendBtn.setOnClickListener(this)

        //copied from firebase documentation - " https://firebase.google.com/docs/auth/android/phone-auth?hl=en&authuser=0 "
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")

                if (::progressDialog.isInitialized) {
                    progressDialog.dismiss()
                }

                val smsMessageSent = credential.smsCode
                if (!smsMessageSent.isNullOrBlank())
                    sentcodeEt.setText(smsMessageSent)
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (::progressDialog.isInitialized) {
                    progressDialog.dismiss()
                }

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e("Exception:", "FirebaseAuthInvalidCredentialsException", e)
                    Log.e("=========:", "FirebaseAuthInvalidCredentialsException " + e.message)


                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Log.e("Exception:", "FirebaseTooManyRequestsException", e)
                }

                // Show a message and update the UI
                notifyUserAndRetry("Your Phone Number might be wrong or connection error.Retry again!")

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                progressDialog.dismiss()
                counterTv.isVisible = false
                // Save verification ID and resending token so we can use them later
                Log.e("onCodeSent==", "onCodeSent:$verificationId")
                mVerificationId = verificationId
                mResendToken = token
            }
        }
    }

    private fun setSpannableString() { //used to make a text with clickable action, just like mention "@krish_verma_7" on whatsapp chat.
        waitingTv = findViewById(R.id.waitingTv)

        val span = SpannableString(
            getString(
                R.string.waiting_text,
                phoneNumber
            )
        )   //This line creates a SpannableString by formatting a string resource.
        val clickSpan: ClickableSpan = object :
            ClickableSpan() { //This code defines a ClickableSpan object, which is used to create a clickable portion of the text.
            override fun onClick(textView: View) {
                showLoginActivity()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ds.linkColor
            }
        }
        span.setSpan(
            clickSpan,
            span.length - 13,
            span.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )      //This line applies the clickableSpan to a specific portion of the span. It makes the text within the specified range (from span.length - 13 to span.length) clickable.
        waitingTv.movementMethod = LinkMovementMethod.getInstance()
        waitingTv.text = span
    }

    private fun notifyUserAndRetry(message: String) {
        MaterialAlertDialogBuilder(this).apply {
            setMessage(message)
            setPositiveButton("Ok") { _, _ ->
                showLoginActivity()
            }

            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            setCancelable(false)
            create()
            show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("timeLeft", timeLeft)
        outState.putString(PHONE_NUMBER, phoneNumber)
    }

    //this method will send a code to a given phone number as an SMS
    private fun startPhoneNumberVerification(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,       // Phone number to verify
            60,        // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this,      // Activity (for callback binding)
            callbacks
        ) // OnVerificationStateChangedCallbacks
    }

    private fun showLoginActivity() {
        startActivity(
            Intent(this, LoginActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )    //these are flags used to clear the back stacks, helpful in dealing with stack of activities
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (::progressDialog.isInitialized) {
                        progressDialog.dismiss()
                    }
                    //First Time Login
                    if (task.result?.additionalUserInfo?.isNewUser == true) {
                    showSignUpActivity()
                    } else {
                        showHomeActivity()
                    }
                } else {
                    if (::progressDialog.isInitialized) {
                        progressDialog.dismiss()
                    }
                    notifyUserAndRetry("Your Phone Number verification failed. Try again!")
                }
            }
    }

    private fun showSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCounterDown != null) {
            mCounterDown!!.cancel()
        }
    }

    override fun onClick(v: View) {
        val verificationBtn: Button = findViewById(R.id.verificationBtn)
        sentcodeEt = findViewById(R.id.sentcodeEt)

        when (v) {
            verificationBtn -> {
                var code = sentcodeEt.text.toString()
                if (code.isNotEmpty() && !mVerificationId.isNullOrBlank()) {
                    progressDialog = createProgressDialog("Please wait...", false)
                    progressDialog.show()
                    //   progressBar.visibility=View.VISIBLE
                    val credential: PhoneAuthCredential =
                        PhoneAuthProvider.getCredential(mVerificationId!!, code.toString())
                    signInWithPhoneAuthCredential(credential)
                }
            }

            resendBtn -> {
                if (mResendToken != null) {
                    resendVerificationCode(phoneNumber.toString(), mResendToken)
                    showTimer(60000)
                    progressDialog = createProgressDialog("Sending a verification code", false)
                    progressDialog.show()
                } else {
                    Toast.makeText(
                        this,
                        "Sorry, You Can't request new code now, Please wait ...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showTimer(milliesInFutute: Long) {
        resendBtn = findViewById(R.id.resendBtn)
        counterTv = findViewById(R.id.counterTv)

        resendBtn.isEnabled = false
        mCounterDown = object : CountDownTimer(milliesInFutute, 1000) {

            override fun onFinish() {
                resendBtn.isEnabled = true
                counterTv.isVisible = false
            }

            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                counterTv.isVisible = true
                counterTv.text = "Seconds remaining: " + millisUntilFinished / 1000
            }
        }.start()
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        mResendToken: PhoneAuthProvider.ForceResendingToken?
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks, // OnVerificationStateChangedCallbacks
            mResendToken
        ) // ForceResendingToken from callbacks
    }


}

fun Context.createProgressDialog(message: String, isCancelable: Boolean): ProgressDialog {
    return ProgressDialog(this).apply {
        setCancelable(isCancelable)
        setCanceledOnTouchOutside(false)
        setMessage(message)
    }
}

