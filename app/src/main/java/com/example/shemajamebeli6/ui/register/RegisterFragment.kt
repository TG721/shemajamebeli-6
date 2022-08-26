package com.example.shemajamebeli6.ui.register

import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.shemajamebeli6.R
import com.example.shemajamebeli6.app.App
import com.example.shemajamebeli6.databinding.FragmentRegisterBinding
import com.example.shemajamebeli6.network.repository.Repository
import com.example.shemajamebeli6.ui.BaseFragment
import com.example.shemajamebeli6.utils.FragmentRes
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

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(
    FragmentRegisterBinding::inflate
) {

    private lateinit var viewModel: RegisterViewModel

    override fun listeners(){
        binding.apply {
            var eyeClicked = 0
            registerPassword.setEndIconOnClickListener {
                if (eyeClicked % 2 == 0) {
                    registerPassword.editText!!.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    registerPassword.endIconDrawable = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.layered_eye_no
                    )
                } else {
                    registerPassword.editText!!.inputType =
                        InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    registerPassword.endIconDrawable = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.layered_eye
                    )
                }
                eyeClicked++

            }
            registerBackButton.setOnClickListener {
                findNavController().popBackStack()
            }
            registerRegisterButton.setOnClickListener {
                when {
                    checkEmpty(registerEmail) || checkEmpty(registerPassword) || checkEmpty(
                        registerPasswordRepeat
                    ) -> {
                    }
                    notGoodPass(registerPassword) -> {}
                    !isValidEmail(registerEmail) -> {}
                    registerPasswordET.text.toString() != registerPasswordRepeatET.text.toString() -> {
                        Snackbar.make(binding.root, "Passwords should match", Snackbar.LENGTH_SHORT)
                            .setTextMaxLines(2)
                            .setBackgroundTint(ContextCompat.getColor(App.appContext, R.color.regular_red))
                            .show()
                    }
                    else -> {
                        viewModel.register(
                            email = registerEmailET.text.toString(),
                            password = registerPasswordET.text.toString()
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
                            setFragmentResult(
                                requestKey = FragmentRes.REG_DATA_KEY,
                                result = bundleOf(
                                    FragmentRes.EMAIL to binding.registerEmailET.text.toString(),
                                    FragmentRes.PASSWORD to binding.registerPasswordET.text.toString()
                                )
                            )
                            hideProgressBar()
                            goToLoginFra()
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
    private fun goToLoginFra() {
        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
    }
    private fun hideProgressBar() {
        binding.registerProgressBar.visibility = View.GONE
    }

    override fun setup() {
        val repository = Repository()
        val viewModelFactory = RegisterViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
        binding.apply {


            registerPasswordET.background.alpha = 76
            registerEmailET.background.alpha = 76
            registerPasswordRepeatET.background.alpha = 76

        }
    }

}
