package com.example.donateblood

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_request.*
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.search_cardview_design.*
import com.example.donateblood.UserClass as ComExampleDonatebloodUserClass

class Request : AppCompatActivity() {

    //private const val REQUEST_CALL = 1

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        val bloodGroups = arrayOf("Choose a blood group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        bloodGroupRequest.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups)

        bloodGroupRequest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@Request, "Please choose a blood group", Toast.LENGTH_SHORT).show()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

             //   Toast.makeText(this@Request, bloodGroups[p2] + " is selected", Toast.LENGTH_SHORT).show()

            }

        }

        val addressDistrict = arrayOf("Choose a district",
            "Bagerhat",
            "Bandarban",
            "Barguna",
            "Barisal",
            "Bhola",
            "Bogra",
            "Brahmanbaria",
            "Chandpur",
            "Chittagong",
            "Chuadanga",
            "Comilla",
            "Cox's Bazar",
            "Dhaka",
            "Dinajpur",
            "Faridpur",
            "Feni",
            "Gaibandha",
            "Gazipur",
            "Gopalganj",
            "Habiganj",
            "Jaipurhat",
            "Jamalpur",
            "Jessore",
            "Jhalakati",
            "Jhenaidah",
            "Khagrachari",
            "Khulna",
            "Kishoreganj",
            "Kurigram",
            "Kushtia",
            "Lakshmipur",
            "Lalmonirhat",
            "Madaripur",
            "Magura",
            "Manikganj",
            "Meherpur",
            "Moulvibazar",
            "Munshiganj",
            "Mymensingh",
            "Naogaon",
            "Narail",
            "Narayanganj",
            "Narsingdi",
            "Natore",
            "Nawabganj",
            "Netrakona",
            "Nilphamari",
            "Noakhali",
            "Pabna",
            "Panchagarh",
            "Parbattya Chattagram",
            "Patuakhali",
            "Pirojpur",
            "Rajbari",
            "Rajshahi",
            "Rangpur",
            "Satkhira",
            "Shariatpur",
            "Sherpur",
            "Sirajganj",
            "Sunamganj",
            "Sylhet",
            "Tangail",
            "Thakurgaon"
        )

        districtRequest.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, addressDistrict)

        districtRequest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@Request, "Please choose a district", Toast.LENGTH_SHORT).show()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //Toast.makeText(this@Request, addressDistrict[p2] + " is selected", Toast.LENGTH_SHORT).show()

            }

        }

        searchButtonRequest.setOnClickListener {
            search()
        }

        homeButtonRequest.setOnClickListener{

            startActivity(Intent(this, HomePage::class.java))
        }
    }

    fun contact(phoneNumber: String){

                val phone = "tel:" + phoneNumber

                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phone))
                startActivity(intent)

                // this was for making direct phone call including the fuction outside on RequestPermissionResult

                /*   if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                      ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL)
                  }else if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                      val phone = "tel:" + phoneNumber
                      val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phone))
                      startActivity(intent)
                  } */

        /* fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
         if(requestCode == REQUEST_CALL){
             if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                 contact()
             }else{
                 Toast.makeText(this,"Permission for phone call is denied.", LENGTH_SHORT).show()
             }
         }
     } */

    }

    fun search(){

        val progressDialog = ProgressDialog(this)
        progressDialog.show()
        //progressDialog.setTitle("Loading")
        progressDialog.setMessage("Plz wait ...")
        progressDialog.setContentView(R.layout.loading_search)
        progressDialog.setCancelable(true)

        val ref = FirebaseFirestore.getInstance().collection("Users")

        val requestedBloodGroup = bloodGroupRequest.selectedItem.toString().trim()
        val requestedDistrict = districtRequest.selectedItem.toString().trim()

        if(requestedBloodGroup.isEmpty() && requestedDistrict.isEmpty() || requestedBloodGroup == "Choose a blood group" && requestedDistrict == "Choose a district"){

            progressDialog.dismiss()
            Toast.makeText(this, "Please choose a value", LENGTH_SHORT).show()

        }else{
            if(requestedBloodGroup == "Choose a blood group"){
                ref.whereEqualTo("addressdistrict", requestedDistrict)
                    .get()
                    .addOnSuccessListener {

                        val adapter =  GroupAdapter<GroupieViewHolder>()

                        for( query in it !!){
                            val data = query.toObject(ComExampleDonatebloodUserClass::class.java)

                            if(data != null){
                                if(query.id.toString() != FirebaseAuth.getInstance().uid.toString()){
                                    adapter.add(SearchAdapter(data))
                                }

                            }

                        }

                        searchRecyclerRequest.adapter = adapter

                        progressDialog.dismiss()

                        adapter.setOnItemClickListener { item, view ->
                           // Toast.makeText(this,"Adapter clicked", LENGTH_SHORT).show()
                            val data = item as SearchAdapter
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

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Search failed", LENGTH_SHORT).show()
                    }
            }else if(requestedDistrict == "Choose a district"){
                ref.whereEqualTo("bloodgroup", requestedBloodGroup)
                    .get()
                    .addOnSuccessListener {

                        val adapter =  GroupAdapter<GroupieViewHolder>()

                        for( query in it !!){
                            val data = query.toObject(ComExampleDonatebloodUserClass::class.java)

                            if(data != null){
                                if(query.id.toString() != FirebaseAuth.getInstance().uid.toString()){
                                    adapter.add(SearchAdapter(data))
                                }
                            }

                        }

                        searchRecyclerRequest.adapter = adapter

                        progressDialog.dismiss()

                        adapter.setOnItemClickListener { item, view ->
                           // Toast.makeText(this,"Adapter clicked", LENGTH_SHORT).show()
                            val data = item as SearchAdapter
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

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Search failed", LENGTH_SHORT).show()
                    }
            }else{
                ref.whereEqualTo("bloodgroup", requestedBloodGroup)
                    .whereEqualTo("addressdistrict", requestedDistrict)
                    .get()
                    .addOnSuccessListener {

                        val adapter =  GroupAdapter<GroupieViewHolder>()

                        for( query in it !!){
                            val data = query.toObject(ComExampleDonatebloodUserClass::class.java)

                            if(data != null){
                                if(query.id.toString() != FirebaseAuth.getInstance().uid.toString()){
                                    adapter.add(SearchAdapter(data))
                                }
                            }

                        }

                        searchRecyclerRequest.adapter = adapter

                        progressDialog.dismiss()

                        adapter.setOnItemClickListener { item, view ->
                            //Log.d("CHECK","Adapter clicked")
                           // Toast.makeText(this,"Adapter clicked", LENGTH_SHORT).show()
                            val data = item as SearchAdapter
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

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Search failed", LENGTH_SHORT).show()
                    }
            }

        }

    }
}
