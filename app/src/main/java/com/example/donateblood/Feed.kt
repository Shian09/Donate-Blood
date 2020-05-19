package com.example.donateblood

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.search_cardview_design.*

class Feed : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        showFeed()

        homeButtonFeed.setOnClickListener{
            startActivity(Intent(this, HomePage::class.java))
        }

        refreshButtonFeed.setOnClickListener {
            val progressDialog = ProgressDialog(this)
            progressDialog.show()
            //progressDialog.setTitle("Loading")
            progressDialog.setMessage("Plz wait ...")
            progressDialog.setContentView(R.layout.loading_search)
            progressDialog.setCancelable(true)
            showFeed()
            progressDialog.dismiss()
        }
    }

    fun showFeed(){



        val db = FirebaseFirestore.getInstance().collection("Users")
        val adapter =  GroupAdapter<GroupieViewHolder>()

        db.get()
            .addOnSuccessListener {
                for(query in it !!){
                    if(query.getString("requestdescription") != null){
                        val data = query.toObject(UserClass::class.java)

                        if(data != null){
                            adapter.add(FeedAdapter(data, query.getString("requestdescription")))
                        }

                    }
                }

                recyclerFeed.adapter = adapter

                adapter.setOnItemClickListener { item, view ->
                    // Toast.makeText(this,"Adapter clicked", LENGTH_SHORT).show()
                    val data = item as FeedAdapter
                    val dialog = AlertDialog.Builder(this)
                    val dialogview = layoutInflater.inflate(R.layout.contact_dialog_search, null)
                    dialog.setView(dialogview)
                    dialog.setCancelable(true)
                    val contactDialog = dialog.create()
                    contactDialog.show()
                    contactDialog.contactDonorButtonRequest.setOnClickListener {
                        contact(data.searchedData.contact.toString().trim())
                    }
                }

            }.addOnFailureListener {
                Toast.makeText(this, "Query problem.${it.message}", LENGTH_SHORT).show()
            }


    }

    fun contact(phoneNumber: String) {

        val phone = "tel:" + phoneNumber

        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phone))
        startActivity(intent)
    }
}
