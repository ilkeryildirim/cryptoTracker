package com.ilkeryildirim.cryptracker.ui.coins


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
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
class CoinsFragment() : Fragment() {

    private val viewModel: CoinsViewModel by viewModels()

    private var _binding: FragmentCoinsBinding? = null
    private val binding get() = _binding!!
    var coinsAdapter: CoinsAdapter? = null
    private var isFilteringByFavourites = false
    private var coinList = arrayListOf<CoinItem>()
    private var onlyFavouriteCoins = arrayListOf<CoinItem>()

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
        initSearchViewQueryListener()
    }

    private fun observeViewModel() {

        lifecycleScope.launchWhenCreated {
            viewModel.favouriteCoins.observe(viewLifecycleOwner) {favouriteCoins->
                onlyFavouriteCoins.clear()
                favouriteCoins.forEach {favId->
                    onlyFavouriteCoins.addAll(coinList.filter { it.id == favId})
                }
            }
            viewModel.isFavouritesFiltering.observe(viewLifecycleOwner) {
                isFilteringByFavourites = it
                if(it) {
                    initCoinsAdapter(onlyFavouriteCoins)
                    binding.imgShowFavourites.setImageResource(R.drawable.baseline_star_black_24dp)
                }else{
                    initCoinsAdapter(coinList)
                    binding.imgShowFavourites.setImageResource(R.drawable.baseline_star_border_black_24dp)
                }
            }
        }
    }

    private fun initSearchViewQueryListener(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {return false}
            override fun onQueryTextChange(newText: String?): Boolean {
                coinsAdapter?.filter?.filter(newText)
                return false
            }
        })
    }

    private fun observeFragmentViewState() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is Initial -> {
                        hideContent()
                    }
                    is Success -> {
                        coinList=state.coinList
                        state.coinList.let(::initCoinsAdapter)
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
        //missing diff util
        binding.rvCoins.apply {
            coinsAdapter?.let { coinsAdapter ->
             coinsAdapter.coins = list
            }.run {
                coinsAdapter = CoinsAdapter(list) { onCoinItemClick(it) }
            }
            adapter = coinsAdapter
            coinsAdapter?.notifyDataSetChanged()
          binding.searchView.query.let {
              if(it.isNullOrEmpty().not()) coinsAdapter!!.filter.filter(it)
          }
        }
    }


    private fun onCoinItemClick(coinItem: CoinItem) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.KEY_COIN_ITEM, coinItem)
        navigate(R.id.action_coinsFragment_to_coinDetailFragment, bundle)
    }

    private fun getCoinList() {
        lifecycleScope.launchWhenCreated {
            viewModel.getCoinList()
        }
    }

    private fun showContent() {
        binding.lytContent.animate().alpha(1.0f)
    }

    private fun hideContent() {
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
