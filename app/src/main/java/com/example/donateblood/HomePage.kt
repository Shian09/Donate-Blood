package com.example.donateblood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val user_id = FirebaseAuth.getInstance().uid

        if(user_id==null){
            val intent = Intent(this, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        requestId.setOnClickListener{
            startActivity(Intent(this, Request::class.java))
        }

        profileId.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        requestHomePage.setOnClickListener {
            startActivity(Intent(this, RequestBlood::class.java))
        }

        feedId.setOnClickListener {
            startActivity(Intent(this, Feed::class.java))
        }

        factsId.setOnClickListener {
            startActivity(Intent(this, Facts::class.java))
        }

        aboutId.setOnClickListener {
            startActivity(Intent(this, About::class.java))
        }

        signOut_buttonHomePage.setOnClickListener {
            signOut()
        }
    }

    private fun signOut()
    {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        Toast.makeText(this, "User has been logged out",LENGTH_SHORT).show()
        finish()
    }
}
