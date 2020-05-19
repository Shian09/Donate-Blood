package com.example.donateblood

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.addressRegisterArea
import kotlinx.android.synthetic.main.activity_register.addressRegisterDistrict
import kotlinx.android.synthetic.main.activity_register.bloodGroupRegister
import kotlinx.android.synthetic.main.activity_register.contactRegister
import kotlinx.android.synthetic.main.activity_register.emailRegister
import kotlinx.android.synthetic.main.activity_register.lastDonatedRegister
import kotlinx.android.synthetic.main.activity_register.passwordRegister
import kotlinx.android.synthetic.main.activity_register.register_buttonRegister
import kotlinx.android.synthetic.main.activity_register.usernameRegister

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val bloodGroups = arrayOf("Choose a blood group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        bloodGroupRegister.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups)

        bloodGroupRegister.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                register_buttonRegister.isClickable = false
                Toast.makeText(this@Register, "Please choose a bloodgroup", LENGTH_SHORT).show()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(bloodGroups[p2] == "Choose a blood group") {
                    register_buttonRegister.isClickable = false
                 //   Toast.makeText(this@Register, "Please choose a valid bloodgroup", LENGTH_SHORT).show()
                }else {
                    register_buttonRegister.isClickable = true
                   // Toast.makeText(this@Register, bloodGroups[p2] + " is selected", LENGTH_SHORT).show()
                }
            }

        }

        val gender = arrayOf("Choose a gender", "Male", "Female", "Other")

        genderRegister.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, gender)

         genderRegister.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                register_buttonRegister.isClickable = false
                Toast.makeText(this@Register, "Please choose a gender", LENGTH_SHORT).show()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(gender[p2] == "Choose a gender") {
                    register_buttonRegister.isClickable = false
                   // Toast.makeText(this@Register,"Please choose a valid gender" , LENGTH_SHORT).show()
                }else {
                    register_buttonRegister.isClickable = true
                   // Toast.makeText(this@Register, gender[p2] + " is selected", LENGTH_SHORT).show()
                }
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

        addressRegisterDistrict.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, addressDistrict)

        addressRegisterDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                register_buttonRegister.isClickable = false
                Toast.makeText(this@Register, "Please choose a district", LENGTH_SHORT).show()

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(addressDistrict[p2] == "Choose a district") {
                    register_buttonRegister.isClickable = false
                    // Toast.makeText(this@Register,"Please choose a valid district" , LENGTH_SHORT).show()
                }else {
                    register_buttonRegister.isClickable = true
                   // Toast.makeText(this@Register, addressDistrict[p2] + " is selected", LENGTH_SHORT).show()
                }
            }

        }

        register_buttonRegister.setOnClickListener {
            registerUser()
        }

        }

    private fun registerUser(){

        val progressDialog = ProgressDialog(this)
        progressDialog.show()
        //progressDialog.setTitle("Loading")
        progressDialog.setMessage("Plz wait ...")
        progressDialog.setContentView(R.layout.loading_search)
        progressDialog.setCancelable(true)

       // Toast.makeText(this, "Register button has been pressed", LENGTH_SHORT).show()
        val username = usernameRegister.text.toString()
        val email = emailRegister.text.toString().trim()
        val password = passwordRegister.text.toString()
        val contact = contactRegister.text.toString().trim()
        val bloodgroup = bloodGroupRegister.selectedItem.toString().trim()
        val gender = genderRegister.selectedItem.toString().trim()
        val lastdonated = lastDonatedRegister.text.toString().trim()
        val addressArea = addressRegisterArea.text.toString().trim()
        val addressDistrict = addressRegisterDistrict.selectedItem.toString().trim()

        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || contact.isEmpty()|| bloodgroup.isEmpty()|| gender.isEmpty() || lastdonated.isEmpty() || addressArea.isEmpty()|| addressDistrict.isEmpty()){

           progressDialog.dismiss()
            Toast.makeText(this, "Please fill everything", LENGTH_SHORT).show()
            //register_buttonRegister.isClickable = false
        }else{
            register_buttonRegister.isClickable= true
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "User Created.", LENGTH_SHORT).show()

                    val ref = FirebaseFirestore.getInstance().collection("Users")
                    val userId = FirebaseAuth.getInstance().uid
                    val userAttribute = UserClass(username, email, password, contact, bloodgroup, gender, lastdonated, addressArea, addressDistrict)

                    ref.document("$userId").set(userAttribute)
                        .addOnSuccessListener {
                          //  Toast.makeText(this, "User is added to database", LENGTH_SHORT).show()
                            val user_now = FirebaseAuth.getInstance().currentUser
                            user_now?.sendEmailVerification()
                                ?.addOnSuccessListener {
                                         //   Toast.makeText(this, "Email verification successful", LENGTH_SHORT).show()

                                }
                                ?.addOnFailureListener {
                                   // Toast.makeText(this, "Email verification failed. Put authentic email.", LENGTH_SHORT).show()
                                }
                            val intent = Intent(this, HomePage::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()

                        }
                        .addOnFailureListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "User is not added to database ${it.message}", LENGTH_SHORT).show()
                        }

                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "User not created. ${it.message}", LENGTH_SHORT).show()
                }
        }


    }


    }



