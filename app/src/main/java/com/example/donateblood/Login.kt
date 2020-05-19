package com.example.donateblood

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_update_profile.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signIn_buttonLogin.setOnClickListener {
            signIn()
        }

        register_buttonLogin.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

    private fun signIn() {

        val progressDialog = ProgressDialog(this)
        progressDialog.show()
        //progressDialog.setTitle("Loading")
        progressDialog.setMessage("Plz wait ...")
        progressDialog.setContentView(R.layout.loading_search)
        progressDialog.setCancelable(true)

        val email = emailLogin.text.toString().trim()
        val password = passwordLogin.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            progressDialog.dismiss()
            Toast.makeText(this, "Please fill up everything", LENGTH_SHORT).show()
        } else {

            val ref = FirebaseFirestore.getInstance().collection("Users")
            var emailFound:Int = 0

            ref.addSnapshotListener{snapshot, exception ->
                if(exception!= null)
                {
                    progressDialog.dismiss()
                   // Toast.makeText(this, "An exception has occured.", Toast.LENGTH_SHORT).show()
                    Log.d("CHECK","No data in database. Exception: ${exception.message}")
                }
                else{
                    for( info in snapshot!!) {

                        val data = info.toObject(UserClass::class.java)

                        if(data.email.toString().trim() == email){
                            emailFound = 1
                            break
                        }

                    }

                    if (emailFound == 1){

                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener {
                                    progressDialog.dismiss()
                                    Toast.makeText(
                                        this,
                                        "Congratulations! User Logged in.",
                                        LENGTH_SHORT
                                    ).show()
                                    val intent = Intent(this, HomePage::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener {
                                    progressDialog.dismiss()
                                    Toast.makeText(
                                        this,
                                        "User failed to Log in ${it.message}",
                                        LENGTH_SHORT
                                    ).show()
                                }

                        } else {
                        progressDialog.dismiss()
                            Toast.makeText(
                                this, "User is not registered",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

            /*
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Congratulations! User Logged in.",
                        LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, HomePage::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "User failed to Log in ${it.message}",
                        LENGTH_SHORT
                    ).show()
                } */
                }
            }

        }

