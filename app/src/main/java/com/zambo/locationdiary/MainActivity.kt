package com.zambo.locationdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.zambo.locationdiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            if (binding.editTextEmail.text.isBlank()) {
                Toast.makeText(this, "Email must not be blank", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.editTextPassword.text.isBlank()) {
                    Toast.makeText(this, "Password must not be blank", Toast.LENGTH_SHORT).show()
                } else {
                    mAuth.signInWithEmailAndPassword(
                        binding.editTextEmail.text.toString(),
                        binding.editTextPassword.text.toString()
                    ).addOnSuccessListener {
                        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

    }
}