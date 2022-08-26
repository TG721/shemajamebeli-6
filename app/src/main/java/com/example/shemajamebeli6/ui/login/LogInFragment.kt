package com.example.shemajamebeli6.ui.login

import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.shemajamebeli6.R
import com.example.shemajamebeli6.databinding.FragmentLoginBinding
import com.example.shemajamebeli6.network.repository.Repository
import com.example.shemajamebeli6.ui.BaseFragment
import com.example.shemajamebeli6.ui.register.checkEmpty
import com.example.shemajamebeli6.ui.register.isValidEmail
import com.example.shemajamebeli6.utils.FragmentRes
import com.example.tbc_homework15.utils.ResponseState
import kotlinx.coroutines.launch


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
            registerButton.setOnClickListener {
                val actionToRegister= LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                Navigation.findNavController(requireView()).navigate(actionToRegister)
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
                            val actionToLoggedIn = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                            if(binding.cbRemember.isChecked) {
                                viewModel.save("token", it.item.token!!)
                            }
                            viewModel.save("login_email", binding.loginEmailET.text.toString())
                            goToHome()
                            hideProgressBar()
                        }
                        else -> {}
                    }
                }
            }
        }
    }
    private fun fragmentResult(){
        setFragmentResultListener(FragmentRes.REG_DATA_KEY){ key, bundle ->
            binding.apply {
                loginEmailET.setText(bundle.getString(FragmentRes.EMAIL, ""))
                loginPasswordET.setText(bundle.getString(FragmentRes.PASSWORD, ""))
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
            loginEmailET.background.alpha = 76
            loginPasswordET.background.alpha = 76
        }
        fragmentResult()
        viewLifecycleOwner.lifecycleScope.launch {
            //check session
            if (viewModel.checkSession("token") != null) {
                goToHome()
            }

        }
    }
    private fun goToHome(){
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }
}
