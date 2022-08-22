package com.example.shemajamebeli6.ui

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.shemajamebeli6.databinding.FragmentHomeBinding
import com.example.shemajamebeli6.databinding.FragmentLogInBinding
import com.example.shemajamebeli6.network.repository.Repository
import com.example.tbc_homework15.ui.login.LoginViewModel
import com.example.tbc_homework15.ui.login.LoginViewModelFactory
import com.example.tbc_homework15.utils.ResponseState


class LogInFragment : BaseFragment<FragmentLogInBinding>(
    FragmentLogInBinding::inflate
) {

    class LoginFragment : BaseFragment<FragmentLoginBinding>(
        FragmentLoginBinding::inflate
    ) {
        private lateinit var viewModel: LoginViewModel


        override fun listeners(){
            binding.apply {
                var loginEyeClicked = 0
                loginPassword.setEndIconOnClickListener {
                    if (loginEyeClicked % 2 == 0) {
                        loginPassword.editText!!.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        loginPassword.endIconDrawable = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.layered_eye_no
                        )
                    } else {
                        loginPassword.editText!!.inputType =
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        loginPassword.endIconDrawable = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.layered_eye
                        )
                    }
                    loginEyeClicked++

                }
                loginLoginButton.setOnClickListener {
                    when {
                        checkEmpty(loginEmail) || checkEmpty(loginPassword) -> {
                        }
                        !isValidEmail(loginEmail) -> {}
                        else -> {
                            viewModel.logIn(
                                email = loginEmailET.text.toString(),
                                password = loginPasswordET.text.toString()
                            )
                        }
                    }
                }
            }
        }
        override fun observers(){
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.loginResponseState.collect {
                        when (it) {
                            is ResponseState.Loading -> {
                                showProgressBar()
                            }
                            is ResponseState.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    "${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                hideProgressBar()
                            }
                            //if login was successful
                            is ResponseState.Success -> {
                                val actionToLoggedIn = LoginFragmentDirections.actionLoginFragmentToLoggedinFragment()
                                Navigation.findNavController(requireView()).navigate(actionToLoggedIn)
                                hideProgressBar()
                            }
                            else -> {}
                        }
                    }
                }
            }
        }

        private fun showProgressBar() {
            binding.loginProgressBar.visibility = View.VISIBLE
        }

        private fun hideProgressBar() {
            binding.loginProgressBar.visibility = View.INVISIBLE
        }

        override fun setup() {
            val repository = Repository()
            val viewModelFactory = LoginViewModelFactory(repository)
            viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
            binding.apply {

            }
        }


    }
}