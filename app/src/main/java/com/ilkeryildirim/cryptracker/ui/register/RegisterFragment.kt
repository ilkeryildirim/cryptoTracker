package com.ilkeryildirim.cryptracker.ui.register

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
import com.ilkeryildirim.cryptracker.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.Error


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerFragmentUIState()
    }

    private fun observerFragmentUIState() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is RegisterFragmentUIState.Initial -> {
                    }
                    is RegisterFragmentUIState.RegisterSuccess -> {
                        navigate(R.id.action_registerFragment_to_coinsFragment,null)
                    }
                    is Error -> {
                        showError(state.message.toString())
                        //or show error page
                    }
                    is RegisterFragmentUIState.Navigation->{
                        navigate(state.destinationId,state.bundle)
                    }
                    is RegisterFragmentUIState.Loading -> {
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
