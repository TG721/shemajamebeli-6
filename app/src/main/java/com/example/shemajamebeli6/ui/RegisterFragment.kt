package com.example.shemajamebeli6.ui

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shemajamebeli6.R
import com.example.shemajamebeli6.databinding.FragmentRegisteBinding
import com.example.tbc_homework15.ui.register.RegisterViewModel
import com.example.tbc_homework15.utils.ResponseState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch


fun checkEmpty(Etext: TextInputLayout): Boolean {
    return if (Etext.editText?.text.toString().trim().isEmpty()) {
        Etext.helperText = "Required"
        true
    } else {
        Etext.helperText = ""
        false
    }
}

fun isValidEmail(Etext: TextInputLayout): Boolean {
    val email = Etext.editText?.text.toString().trim()
    return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Etext.helperText = "invalid email"
        false
    } else {
        Etext.helperText = ""
        true
    }
}

fun isValidUsername(Etext: TextInputLayout): Boolean {
    val username = Etext.editText?.text.toString().trim()
    return if (username.length < 10) {
        Etext.helperText = "username should be at least 10 characters"
        false
    } else {
        Etext.helperText = ""
        true
    }

}

fun notGoodPass(Epassword: TextInputLayout): Boolean {
    val password = Epassword.editText?.text.toString()
    when {
        password.length <= 8 -> {
            Epassword.helperText = "Password should be more than 8 characters"
            return true
        }
        password.contains(Regex("^[0-9]+[^a-zA-z]*$")) -> {
            Epassword.helperText = "Password should contain at least one alphabet character"
            return true
        }
        password.contains(Regex("^[a-zA-Z]+[^0-9]*$")) -> {
            Epassword.helperText = "Password should contain at least one numeric character"
            return true
        }
        else -> {
            Epassword.helperText = ""
            return false
        }
    }
}

class RegisterFragment : BaseFragment<FragmentRegisteBinding>(
    FragmentRegisteBinding::inflate
) {
    override fun setup() {

    }
    private lateinit var viewModel: RegisterViewModel

    override fun listeners(){
        binding.apply {

            ibtnRegister.setOnClickListener {
                when {
                    checkEmpty(tilEmail) || checkEmpty(tilPassword) || checkEmpty(
                        tilRepeatPassword
                    ) -> {
                    }
                    notGoodPass(tilPassword) -> {}
                    !isValidEmail(tilEmail) -> {}
                    tilPassword.editText?.text.toString()!=tilRepeatPassword.editText?.text.toString() -> {
                        Snackbar.make(binding.root, "Passwords should match", Snackbar.LENGTH_SHORT)
                            .setTextMaxLines(1)
                            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.regular_red))
                            .show()
                    }
                    else -> {
                        viewModel.register(
                            email = tilEmail.editText?.text.toString(),
                            password = tilPassword.editText?.text.toString()
                        )

                    }
                }
            }
        }
    }
    override fun observers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.registerResponseState.collect{
                    when(it){
                        is ResponseState.Loading -> {
                            showProgressBar()
                        }
                        is ResponseState.Error -> {
                            Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                            hideProgressBar()
                        }
                        is ResponseState.Success -> {
                            Toast.makeText(requireContext(), "registered successfully", Toast.LENGTH_SHORT).show()
                            hideProgressBar()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.registerProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.registerProgressBar.visibility = View.GONE
    }

}
