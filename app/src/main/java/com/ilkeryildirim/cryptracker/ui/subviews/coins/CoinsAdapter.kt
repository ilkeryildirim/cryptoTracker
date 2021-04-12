package com.ilkeryildirim.cryptracker.ui.subviews.coins

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ilkeryildirim.cryptracker.R
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem
import com.ilkeryildirim.cryptracker.databinding.ItemCoinBinding


class CoinsAdapter(var coins: List<CoinItem>,val itemClick: (CoinItem) -> Unit) : RecyclerView.Adapter<CoinsAdapter.ViewHolder>() {
    private var lastPosition = -1
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCoinBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_coin, parent, false)
        context = parent.context
        return ViewHolder(binding,itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(coins[position])
        setAnimation(holder.itemView, position)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.itemView.clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return coins.size
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
            val animation = AnimationUtils.loadAnimation(context, R.anim.nav_default_enter_anim)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

}


