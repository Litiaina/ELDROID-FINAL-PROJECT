package com.zambo.locationdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zambo.locationdiary.databinding.ActivityHistoryDetailsBinding

class HistoryDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailsBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        binding.titleTextView.text = intent.getStringExtra("locationTitle")
        binding.subtitleTextView.text = intent.getStringExtra("locationAddress")

        binding.updateButton.setOnClickListener {
            mDatabase.getReference("location").child(intent.getStringExtra("locationUID").toString()).setValue(
                AddLocationData(
                    mAuth.currentUser?.uid.toString(),
                    intent.getStringExtra("locationUID").toString(),
                    binding.newNameEditText.text.toString(),
                    binding.newAddressEditText.text.toString(),
                    intent.getStringExtra("locationDescription").toString()
                )
            ).addOnSuccessListener {
                Toast.makeText(this, "Location updated", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to update location", Toast.LENGTH_SHORT).show()
            }
        }

    }
}