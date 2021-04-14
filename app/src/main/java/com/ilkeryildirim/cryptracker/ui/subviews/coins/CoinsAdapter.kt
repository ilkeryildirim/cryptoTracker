package com.ilkeryildirim.cryptracker.ui.subviews.coins

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilkeryildirim.cryptracker.R
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem
import com.ilkeryildirim.cryptracker.databinding.ItemCoinBinding
import java.util.*
import kotlin.collections.ArrayList


class CoinsAdapter(var coins: List<CoinItem>,val itemClick: (CoinItem) -> Unit) : RecyclerView.Adapter<CoinsAdapter.ViewHolder>(),
    Filterable {
    private var lastPosition = -1
    private lateinit var context: Context
    var filteredCoins = ArrayList<CoinItem>()
    init {
        filteredCoins = coins as ArrayList<CoinItem>
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredCoins = if (charSearch.isEmpty()) {
                    coins as ArrayList<CoinItem>
                } else {
                    val resultList = ArrayList<CoinItem>()
                    for (row in coins) {
                        if (row.name!!.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredCoins
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCoins = results?.values as ArrayList<CoinItem>
                notifyDataSetChanged()
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCoinBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_coin, parent, false)
        context = parent.context
        return ViewHolder(binding,itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredCoins[position])
        setAnimation(holder.itemView, position)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.itemView.clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return filteredCoins.size
    }

    class ViewHolder(private val binding: ItemCoinBinding,private val itemClick: (CoinItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = CoinsItemViewModel()
        fun bind(coin: CoinItem) {
            binding.viewModel = viewModel
            viewModel.bind(coin)
            binding.root.setOnClickListener { itemClick(coin) }
            if (coin.priceChange24h.toString().contains("-")) {
                binding.tvCoinPriceChange24hr.setTextColor(ContextCompat.getColor(binding.root.context, R.color.crimson))
            } else {
                binding.tvCoinPriceChange24hr.setTextColor(ContextCompat.getColor(binding.root.context, R.color.greenLight))
            }

        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}


