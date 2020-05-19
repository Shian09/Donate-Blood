package com.example.donateblood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_update_profile.*

class updateProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        val bloodGroups = arrayOf("Choose a blood group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        editBloodGroupSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups)

        editBloodGroupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@updateProfile, "Please choose a bloodgroup", Toast.LENGTH_SHORT).show()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                //    Toast.makeText(this@updateProfile, bloodGroups[p2] + " is selected", Toast.LENGTH_SHORT).show()

            }

        }

        val gender = arrayOf("Choose a gender", "Male", "Female", "Other")

        editGenderSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gender)

        editGenderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@updateProfile, "Please choose a gender", Toast.LENGTH_SHORT).show()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                   // Toast.makeText(this@updateProfile, gender[p2] + " is selected", Toast.LENGTH_SHORT).show()

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

        editDistrictSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, addressDistrict)

        editDistrictSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@updateProfile, "Please choose a district", Toast.LENGTH_SHORT).show()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                   // Toast.makeText(this@updateProfile, addressDistrict[p2] + " is selected", Toast.LENGTH_SHORT).show()

            }

        }

        showEditableProfile()

        updateButtonProfile.setOnClickListener {
            updateUserProfile()
        }

        homeButtonUpdateProfile.setOnClickListener{

            startActivity(Intent(this, HomePage::class.java))
        }
    }

    fun showEditableProfile(){

        val userId = FirebaseAuth.getInstance().uid
        val ref = FirebaseFirestore.getInstance().collection("Users").document("$userId")

        ref.addSnapshotListener{snapshot, exception ->
            if(exception!= null)
            {
                Toast.makeText(this, "An exception has occured.", Toast.LENGTH_SHORT).show()
                Log.d("CHECK","No data in database. Exception: ${exception.message}")
            }
            else{
                if(snapshot?.exists()!!){

                    val data = snapshot.toObject(UserClass::class.java)
                    editNameText.setText( data?.username)
                    editBloodGroupText.setText( data?.bloodgroup)
                    editGenderText.setText( data?.gender)
                    editAreaText.setText( data?.addressarea)
                    editDistrictText.setText( data?.addressdistrict)
                    editContactText.setText( data?.contact)
                    editEmailText.setText( data?.email)
                    editLastDonatedText.setText(data?.lastdonated)

                }else{
                    Toast.makeText(this, "Information does not exist on database.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    fun updateUserProfile(){

        val userId = FirebaseAuth.getInstance().uid
        val ref = FirebaseFirestore.getInstance().collection("Users").document("$userId")

        ref.update("username", editNameText.text.toString())

        if(editBloodGroupSpinner.selectedItem.toString().trim() != "Choose a blood group"){
            ref.update("bloodgroup", editBloodGroupSpinner.selectedItem.toString().trim())
        }

        if(editGenderSpinner.selectedItem.toString().trim() != "Choose a gender"){
            ref.update("gender", editGenderSpinner.selectedItem.toString().trim())
        }

        ref.update("addressarea", editAreaText.text.toString().trim())

        if(editDistrictSpinner.selectedItem.toString().trim() != "Choose a district") {
            ref.update("addressdistrict", editDistrictSpinner.selectedItem.toString().trim())
        }

        ref.update("contact", editContactText.text.toString().trim())

        ref.update("lastdonated", editLastDonatedText.text.toString().trim())

        val intent = Intent(this, Profile::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        Toast.makeText(this, "User updated.", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }


}
