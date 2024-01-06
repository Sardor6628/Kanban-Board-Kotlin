package com.example.tempstructure.ui.home

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.tempstructure.app.BaseActivity
import com.example.tempstructure.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity(){

    private lateinit var binding: ActivityHomeBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}