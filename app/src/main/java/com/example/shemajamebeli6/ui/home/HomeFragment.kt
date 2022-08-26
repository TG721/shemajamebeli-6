package com.example.shemajamebeli6.ui.home

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shemajamebeli6.databinding.FragmentHomeBinding
import com.example.shemajamebeli6.network.repository.Repository
import com.example.shemajamebeli6.ui.BaseFragment
import com.example.shemajamebeli6.ui.HomeViewModelFactory
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    private lateinit var viewModel: HomeViewModel
    override fun setup() {
        val repository = Repository()
        val viewModelFactory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch {
            binding.userMail.text = viewModel.getEmail("login_email")
        }

    }
    override fun listeners() {
        binding.buttonSignOut.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.clear()
                goToLogin()
            }
        }
    }
    private fun goToLogin() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
    }
}