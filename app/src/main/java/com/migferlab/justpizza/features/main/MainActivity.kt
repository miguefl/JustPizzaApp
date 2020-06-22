package com.migferlab.justpizza.features.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.migferlab.justpizza.R
import com.migferlab.justpizza.databinding.ActivityMainBinding
import com.migferlab.justpizza.di.viewModel
import com.migferlab.justpizza.features.login.LoginViewModel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di


class MainActivity : AppCompatActivity(), DIAware {

    override val di: DI by di()
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.navHostFragment)

        binding.toolBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.homeFragment) {
                binding.toolBar.visibility = View.GONE
            } else {
                binding.toolBar.visibility = View.VISIBLE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val sign = 23
        if (requestCode == sign) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                viewModel.googleSignIn(task.result!!)
        }
    }

    fun setToolbarTitle(title: String) {
        findViewById<Toolbar>(R.id.toolBar).title = title
    }
}
