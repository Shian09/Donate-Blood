package com.example.donateblood

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import java.util.jar.Manifest

class Profile : AppCompatActivity() {


    /*this whole companion object means private static final int REQUEST_CALL = 1*/
    private companion object{
        const val REQUEST_CALL = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        showProfile()

        editButtonProfile.setOnClickListener {
            edit()
        }

        delete_buttonProfile.setOnClickListener {
            delete_user()
        }

        homeButtonProfile.setOnClickListener{

            startActivity(Intent(this, HomePage::class.java))
        }
    }

    fun delete_user()
    {
        val ref = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().uid.toString())
        var usernow = FirebaseAuth.getInstance().currentUser
        usernow?.delete()
            ?.addOnSuccessListener {
                ref.delete()
                    .addOnSuccessListener {

                        // Toast.makeText(this, "User id deleted.", LENGTH_SHORT).show()
                        val intent = Intent(this, Login::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        Toast.makeText(this, "User deleted.", LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()

                    }.addOnFailureListener {
                        Toast.makeText(this, "User id not deleted.", LENGTH_SHORT).show()
                    }
            }
            ?.addOnFailureListener {
                Toast.makeText(this, "User not deleted.", LENGTH_SHORT).show()
            }

    }

    fun showProfile(){

        val userId = FirebaseAuth.getInstance().uid
        val ref = FirebaseFirestore.getInstance().collection("Users").document("$userId")

        ref.addSnapshotListener{snapshot, exception ->
            if(exception!= null)
            {
                Toast.makeText(this, "An exception has occured. ${exception.message}", LENGTH_SHORT).show()
                Log.d("CHECK","No data in database. Exception: ${exception.message}")
            }
            else{
                if(snapshot?.exists()!!){

                    val data = snapshot.toObject(UserClass::class.java)
                    nameText.setText("Name: " + data?.username)
                    bloodGroupText.setText("Blood Group: " + data?.bloodgroup)
                    genderText.setText("Gender: " + data?.gender)
                    areaText.setText("Area: " + data?.addressarea)
                    districtText.setText("District: " + data?.addressdistrict)
                    contactText.setText("Contact: " + data?.contact)
                    emailText.setText("E-mail: " + data?.email)
                    lastDonatedText.setText("Last Donated Blood: " + data?.lastdonated)

                }else{
                    Toast.makeText(this, "Information is deleted or does not exist.", LENGTH_SHORT).show()
                }
            }

        }
    }

    fun edit(){

        startActivity(Intent(this,updateProfile::class.java))

    }


}
