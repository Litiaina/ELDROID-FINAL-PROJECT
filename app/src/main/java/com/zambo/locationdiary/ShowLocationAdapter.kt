package com.zambo.locationdiary

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zambo.locationdiary.databinding.ItemLayoutLocationBinding

class ShowLocationAdapter(private val originalList: List<AddLocationData>, private val activity: Activity) : RecyclerView.Adapter<ShowLocationAdapter.ViewHolder>() {

    private var filteredList: List<AddLocationData> = originalList


    class ViewHolder(val binding: ItemLayoutLocationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = filteredList[position]
        holder.binding.titleTextView.text = currentItem.locationName
        holder.binding.subtitleTextView.text = currentItem.address
        holder.binding.parentCardView.setOnClickListener {
            val intent = Intent(activity, HistoryDetailsActivity::class.java)
            intent.putExtra("locationTitle", currentItem.locationName)
            intent.putExtra("locationAddress", currentItem.address)
            intent.putExtra("locationUID", currentItem.locationUID)
            intent.putExtra("locationDescription", currentItem.description)
            activity.startActivity(intent)
        }
        holder.binding.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Confirm Delete")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete") { _, _ ->
                    val mDatabase = FirebaseDatabase.getInstance()
                    val mAuth = FirebaseAuth.getInstance()
                    val historyKey = mDatabase.getReference("history").push().key
                    mDatabase.getReference("history").child(historyKey!!).setValue(
                        HistoryData(
                            mAuth.currentUser?.uid.toString(),
                            historyKey.toString(),
                            currentItem.locationName,
                            currentItem.address,
                            currentItem.description
                        )
                    ).addOnSuccessListener {
                        Toast.makeText(activity, "History added", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(activity, "History failed to upload", Toast.LENGTH_SHORT).show()
                    }
                    mDatabase.getReference("location").orderByChild("locationUID").equalTo(currentItem.locationUID)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (data in snapshot.children) {
                                    val locationKey = data.key
                                    if (locationKey != null) {
                                        mDatabase.getReference("location").child(locationKey).removeValue()
                                            .addOnSuccessListener {
                                                Toast.makeText(activity, "Location deleted", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(activity, "Failed to delete location", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("FirebaseError", "Error: ${error.message}")
                            }
                        })
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }
}