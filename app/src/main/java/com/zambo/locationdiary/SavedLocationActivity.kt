package com.zambo.locationdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zambo.locationdiary.databinding.ActivitySavedLocationBinding

class SavedLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}