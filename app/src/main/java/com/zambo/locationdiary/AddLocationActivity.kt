package com.zambo.locationdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zambo.locationdiary.databinding.ActivityAddLocationBinding

class AddLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddLocationBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        binding.checkButton.setOnClickListener {
            val locationKey = mDatabase.getReference("location").push().key
            mDatabase.getReference("location").child(locationKey!!).setValue(
                AddLocationData(
                    mAuth.currentUser?.uid.toString(),
                    locationKey.toString(),
                    binding.addLocationNameEditText.text.toString(),
                    binding.addLocationAddressEditText.text.toString(),
                    binding.addLocationDescriptionEditText.text.toString()
                )
            ).addOnSuccessListener {
                Toast.makeText(this, "Location added", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Location failed to be added", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

    }
}