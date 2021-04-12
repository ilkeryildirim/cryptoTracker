package com.ilkeryildirim.cryptracker.ui.coins

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
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem
import com.ilkeryildirim.cryptracker.databinding.FragmentCoinsBinding
import com.ilkeryildirim.cryptracker.ui.coins.DiscoverFragmentUIState.*
import com.ilkeryildirim.cryptracker.ui.subviews.coins.CoinsAdapter
import com.ilkeryildirim.cryptracker.utils.Constants


import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class CoinsFragment : Fragment() {

    private val viewModel: CoinsViewModel by viewModels()

    private var _binding: FragmentCoinsBinding? = null
    private val binding get() = _binding!!
    var coinsAdapter: CoinsAdapter? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCoinList()
        observeFragmentViewState()
        observeViewModel()
    }

    private fun observeViewModel() {}

    private fun observeFragmentViewState() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is Initial ->{
                        hideContent()
                    }
                    is Success -> {
                        state.discoverData.let(::initCoinsAdapter)
                        showContent()
                    }
                    is Error -> {
                        showError(state.message)
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

    private fun initCoinsAdapter(list: ArrayList<CoinItem>) {
        binding.rvCoins.apply {
            coinsAdapter?.let { coinsAdapter ->
                coinsAdapter.coins = list
                coinsAdapter.notifyDataSetChanged()
            }.run {
                coinsAdapter = CoinsAdapter(list) {onCoinItemClick(it)}
            }
            adapter = coinsAdapter
        }
    }


    private fun onCoinItemClick(coinItem: CoinItem){
        val bundle = Bundle()
        bundle.putParcelable(Constants.KEY_COIN_ITEM,coinItem)
        navigate(R.id.action_coinsFragment_to_coinDetailFragment,bundle)
    }
    private fun getCoinList() {
        lifecycleScope.launchWhenCreated {
            viewModel.getCoinList()
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
