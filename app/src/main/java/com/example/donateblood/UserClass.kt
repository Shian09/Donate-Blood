package com.example.donateblood

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserClass(val username: String, val email: String, val password: String, val contact: String, val bloodgroup: String, val gender: String, val lastdonated: String, val addressarea: String, val addressdistrict: String): Parcelable {
    constructor():this("","","","","","","","","")
}