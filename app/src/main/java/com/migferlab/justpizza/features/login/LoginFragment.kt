package com.migferlab.justpizza.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.R
import com.migferlab.justpizza.databinding.LoginLayoutBinding
import com.migferlab.justpizza.di.viewModel
import com.migferlab.justpizza.features.base.BaseFragment


class LoginFragment : BaseFragment() {

    private lateinit var binding: LoginLayoutBinding
    private val viewModel: LoginViewModel by viewModel()
    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 23

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = LoginLayoutBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val errorToast = Toast.makeText(view.context, "Error de acceso", Toast.LENGTH_LONG)

        val email = binding.emailLogin.text
        val password = binding.passwordLogin.text

        binding.signin.setOnClickListener {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.signUp(
                    email.toString(), password.toString()
                )
            } else {
                errorToast.show()
            }

        }

        binding.login.setOnClickListener {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.logIn(
                    email.toString(), password.toString()
                )
            } else {
                errorToast.show()
            }
        }

        binding.googleSignIn.setOnClickListener {
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            requireActivity().startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        viewModel.getLiveData().observe {
            when (it) {
                is Result.Success -> if (it.value.uid.isNotEmpty()) {
                    findNavController().navigate(
                        LoginFragmentDirections.actionLoginToHome()
                    )
                }
                is Result.Loading -> {
                }
                is Result.Empty -> {
                }
                is Result.Failure -> errorToast.show()
            }
        }

    }
}