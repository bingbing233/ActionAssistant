package com.example.actionassistant

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.Navigation
import com.example.actionassistant.base.BaseActivity
import com.example.actionassistant.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val TAG = javaClass.simpleName
    private val navController by lazy {
        Navigation.findNavController(this,R.id.fragment_container)
    }

    private val viewModel:MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initData()
        binding.btnHome.setOnClickListener {
            navController.navigate(R.id.mainFragment)
        }
        binding.btnMore.setOnClickListener {
            navController.navigate(R.id.addFragment)

        }
    }

}

