package com.ilkeryildirim.cryptracker.ui.coindetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ilkeryildirim.cryptracker.R
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem
import com.ilkeryildirim.cryptracker.databinding.FragmentCoinDetailBinding
import com.ilkeryildirim.cryptracker.ui.coindetail.CoinDetailFragmentUIState.*
import com.ilkeryildirim.cryptracker.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.Error


@AndroidEntryPoint
class CoinDetailFragment : Fragment() {

    private val viewModel: CoinDetailViewModel by viewModels()

    private var _binding: FragmentCoinDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<CoinItem>(Constants.KEY_COIN_ITEM)?.let {
            getCoinDetail(it.id!!)
        }
        observeFragmentViewState()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.isFavourite.observe(viewLifecycleOwner) {isFav->
                println("is fav ? : $isFav")
                if(isFav){
                    binding.imgChangeIsFav.setImageResource(R.drawable.baseline_star_black_24dp)
                }else{
                    binding.imgChangeIsFav.setImageResource(R.drawable.baseline_star_border_black_24dp)
                }
            }
        }
    }

    private fun observeFragmentViewState() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is Initial ->{
                        hideContent()
                    }
                    is Success -> {
                        println(state.coinDetail)
                        showContent()
                    }
                    is Error -> {
                        state.message?.let { showError(it) }
                        //or show error page
                    }
                    is Loading -> {
                        hideContent()
                    }
                    else -> Unit
                }
            }
        }
    }


    private fun getCoinDetail(id:String) {
        lifecycleScope.launch {
            viewModel.getCoinDetail(id)
        }
    }

    private fun showContent(){
        binding.lytContent.animate().alpha(1.0f)
    }
    private fun hideContent(){
        binding.lytContent.animate().alpha(0.0f)
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
