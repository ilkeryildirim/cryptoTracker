package com.ilkeryildirim.cryptracker.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ilkeryildirim.cryptracker.R
import com.ilkeryildirim.cryptracker.databinding.FragmentLoginBinding
import com.ilkeryildirim.cryptracker.ui.login.LoginFragmentUIState.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.Error
import kotlin.toString


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFragmentUIState()
        observeViewModel()
    }

    private fun observeViewModel() {}

    private fun observeFragmentUIState() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is Initial -> {
                    }
                    is LoginSuccess -> {
                        navigate(R.id.action_loginFragment_to_coinsFragment,null)
                    }
                    is Error -> {
                        showError(state.message.toString())
                        //or show error page
                    }
                    is Navigation->{
                        navigate(state.destinationId,state.bundle)
                    }
                    is Loading -> {
                    }
                    else -> Unit
                }
            }
        }
    }


    private fun showError(error: String) {
        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
    }

    private fun navigate(destinationId: Int, bundle: Bundle?) {
        findNavController().navigate(
                destinationId,
                bundle
        )
    }

}
