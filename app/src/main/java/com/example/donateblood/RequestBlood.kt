package com.example.donateblood

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_request_blood.*
import java.lang.Exception

class RequestBlood : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_blood)

        showMyRequestOnStartup()

        homeButtonRequestBlood.setOnClickListener{
            startActivity(Intent(this, HomePage::class.java))
        }

        requestButtonRequestBlood.setOnClickListener {
            request()
        }

        deleteRequestButtonRequestBlood.setOnClickListener {
            deleteRequest()
        }
    }

    fun showMyRequestOnStartup(){

        val progressDialog = ProgressDialog(this)
        progressDialog.show()
        //progressDialog.setTitle("Loading")
        progressDialog.setMessage("Plz wait ...")
        progressDialog.setContentView(R.layout.loading_search)
        progressDialog.setCancelable(true)

        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().uid.toString()).get()
            .addOnSuccessListener {

                val reqDescription = it.getString("requestdescription")

                showMyRequestText.setText(reqDescription)

            }.addOnFailureListener {
                Toast.makeText(this, "Your request is failed to retrieve.${it.message}", LENGTH_SHORT).show()
            }

        progressDialog.dismiss()
    }

    fun request(){

        val progressDialog = ProgressDialog(this)
        progressDialog.show()
        //progressDialog.setTitle("Loading")
        progressDialog.setMessage("Plz wait ...")
        progressDialog.setContentView(R.layout.loading_search)
        progressDialog.setCancelable(true)

        val reqDesc = requestBloodText.text.toString()
        if(!reqDesc.isEmpty()){
            val db = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().uid.toString())

            val reqDescField: HashMap<String,String> = hashMapOf<String, String>()

            reqDescField.put("requestdescription", reqDesc)

            db.set(reqDescField, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Your request has been posted", LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Your request is failed to post.${it.message}", LENGTH_SHORT).show()
                }

            db.get()
                .addOnSuccessListener {

                    val reqDescription = it.getString("requestdescription")

                    showMyRequestText.setText(reqDescription)
                    progressDialog.dismiss()

                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Your request is failed to retrieve. ${it.message}", LENGTH_SHORT).show()
                }

        }else{
            progressDialog.dismiss()
            Toast.makeText(this, "Please write in the description", LENGTH_SHORT).show()
        }


    }

    fun deleteRequest(){

        val db = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().uid.toString())

        val progressDialog = ProgressDialog(this)
        progressDialog.show()
        //progressDialog.setTitle("Loading")
        progressDialog.setMessage("Plz wait ...")
        progressDialog.setContentView(R.layout.loading_search)
        progressDialog.setCancelable(true)

        db.update("requestdescription", FieldValue.delete())
            .addOnSuccessListener {

                showMyRequestText.setText(null)
                progressDialog.dismiss()
                Toast.makeText(this, "Deleted your request", LENGTH_SHORT).show()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Deletion is failed. ${it.message}", LENGTH_SHORT).show()
            }


    }
}
