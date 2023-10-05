package com.example.blinkchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hbb20.CountryCodePicker

class LoginActivity : AppCompatActivity() {
    private lateinit var ccp: CountryCodePicker
    private lateinit var phoneNumber: String
    private lateinit var countryCode: String
    private lateinit var alertDialogBuilder: MaterialAlertDialogBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val phoneNumberEt: EditText = findViewById(R.id.phoneNumberEt)
        val nextBtn: Button = findViewById(R.id.nextBtn)

        phoneNumberEt.addTextChangedListener {
            nextBtn.isEnabled = !(it.isNullOrBlank() || it.length != 10)
        }
        nextBtn.setOnClickListener {
            checkNumber()
        }
    }


    private fun checkNumber() {
        val phoneNumberEt: EditText = findViewById(R.id.phoneNumberEt)
        ccp = findViewById(R.id.ccp)

        countryCode = ccp.selectedCountryCodeWithPlus
        phoneNumber = countryCode + phoneNumberEt.text.toString()

//        if(validatePhoneNumber(phoneNumberEt.text.toString())){
//            notifyUserBeforeVerify(
//                "We will be verifying the phone number:$phoneNumber\n"+
//                "Is this OK, or would you like to edit the number?"
//            )
//        }else{
//            Toast.makeText(this,"Please enter a valid number to continue!",toas)
//        }
        notifyUser()
    }

    private fun notifyUser() {
        MaterialAlertDialogBuilder(this).apply {
            setMessage(
                "We will be verifying the phone number:$phoneNumber\n" +
                        "Is this OK, or would you like to edit the number?"
            )
            setPositiveButton("Ok") { _, _ ->
                showOtpActivity()
            }
            setNegativeButton("Edit") { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }
        //    private fun validatePhoneNumber(phone: String): Boolean {
//        if(phone.isEmpty()){
//            return false
//        }
//        return true
//    }
//
//    private fun notifyUserBeforeVerify(message: String) {
//        alertDialogBuilder = MaterialAlertDialogBuilder(this).apply {
//            setMessage(message)
//            setPositiveButton("Ok"){_,_->
//                showLoginActivity()
//            }
//            setNegativeButton("Edit"){ dialog,_->
//                dialog.dismiss()
//            }
//        }
//    }
//
//    private fun showLoginActivity() {
//        TODO("Not yet implemented")
//    }

    }
    fun showOtpActivity() {
        startActivity(Intent(this,OtpActivity::class.java).putExtra(PHONE_NUMBER,phoneNumber))
        finish()
    }
}


