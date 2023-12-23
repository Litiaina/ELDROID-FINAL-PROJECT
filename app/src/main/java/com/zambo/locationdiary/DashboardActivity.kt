package com.zambo.locationdiary

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zambo.locationdiary.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth
    val dataList = mutableListOf<AddLocationData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)

        mDatabase.getReference("location").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (snapshot in dataSnapshot.children) {
                    snapshot.getValue(AddLocationData::class.java)?.let {
                        if (it.userUID.toString() == mAuth.currentUser?.uid.toString()) {
                            dataList.add(it)
                        }
                    }
                }
                binding.historyRecyclerView.adapter = ShowLocationAdapter(dataList, this@DashboardActivity)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error retrieving data: $error")
            }
        })

        binding.addSavedLocationButton.setOnClickListener {
            startActivity(Intent(this, AddLocationActivity::class.java))
        }

        binding.mapButton.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        binding.historyButton.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        binding.accountButton.setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }
    }
}