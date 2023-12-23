package com.zambo.locationdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zambo.locationdiary.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        binding.btnRegister.setOnClickListener {
            if (binding.editTextSignupName.text.isBlank()) {
                Toast.makeText(this, "Name must not be blank", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.editTextSignupEmail.text.isBlank()) {
                    Toast.makeText(this, "Email must not be blank", Toast.LENGTH_SHORT).show()
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextSignupEmail.text).matches()) {
                        Toast.makeText(this, "Email must be valid", Toast.LENGTH_SHORT).show()
                    } else {
                        if (binding.editTextSignupPassword.text.isBlank()) {
                            Toast.makeText(this, "Password must not be blank", Toast.LENGTH_SHORT).show()
                        } else {
                            if (binding.editTextSignupConfirm.text.toString().length < 6) {
                                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                            } else {
                                if (binding.editTextSignupPassword.text.toString() == binding.editTextSignupConfirm.text.toString()) {
                                    mAuth.createUserWithEmailAndPassword(
                                        binding.editTextSignupEmail.text.toString(),
                                        binding.editTextSignupPassword.text.toString()
                                    ).addOnSuccessListener {
                                        mDatabase.getReference("user").child(mAuth.currentUser?.uid.toString()).setValue(
                                            UserData(
                                                mAuth.currentUser?.uid.toString(),
                                                binding.editTextSignupName.text.toString(),
                                                binding.editTextSignupEmail.text.toString(),
                                                ""
                                            )
                                        ).addOnSuccessListener {
                                            Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, DashboardActivity::class.java))
                                            finish()
                                        }.addOnFailureListener {
                                            Toast.makeText(this, "User data failed to save", Toast.LENGTH_SHORT).show()
                                        }
                                    }.addOnFailureListener {
                                        Toast.makeText(this, "Register failed", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this, "Password must match", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.loginTextView.setOnClickListener {
            finish()
        }

    }
}