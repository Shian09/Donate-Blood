package com.example.donateblood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userID = FirebaseAuth.getInstance().uid


        Handler().postDelayed(
        {
            if(userID != null){
                startActivity(Intent(this, HomePage::class.java))
                finish()
            }else {
                startActivity(Intent(this, Login::class.java))
                finish()
            }
        }, 1000)

    }
}
