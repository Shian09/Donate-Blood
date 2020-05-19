package com.example.donateblood

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.search_cardview_design.view.*


class SearchAdapter(val searchedData: UserClass): Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return  R.layout.search_cardview_design
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {


        viewHolder.itemView.nameTextRequest.setText("Username: " + searchedData.username)
        viewHolder.itemView.bloodGroupTextRequest.setText("Blood Group: " + searchedData.bloodgroup)
        viewHolder.itemView.genderTextRequest.setText("Gender: " + searchedData.gender)
        viewHolder.itemView.areaTextRequest.setText("Area: " + searchedData.addressarea)
        viewHolder.itemView.districtTextRequest.setText("District: " + searchedData.addressdistrict)
        viewHolder.itemView.contactTextRequest.setText("Contact: " + searchedData.contact)
        viewHolder.itemView.emailTextRequest.setText("E-mail: " + searchedData.email)
        viewHolder.itemView.lastDonatedTextRequest.setText("Last Donated: " + searchedData.lastdonated)
    }

}