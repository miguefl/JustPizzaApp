package com.migferlab.justpizza.features.pizza.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.migferlab.justpizza.R
import com.migferlab.justpizza.databinding.PizzaListItemBinding
import com.migferlab.justpizza.features.pizza.list.PizzaRecipeListViewState.PizzaListItem

class PizzaRecipeListAdapter(
    private val onItemClickListener: (PizzaListItem) -> Unit,
    private val onItemFavClickListener: (PizzaListItem) -> Unit
) : ListAdapter<PizzaListItem, PizzaRecipeListAdapter.ItemViewHolder>(
    PizzaDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        PizzaListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .run { ItemViewHolder(this) }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(private val binding: PizzaListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pizza: PizzaListItem) {
            with(pizza) {
                binding.pizzaListItemName.text = name
                val favImageId =
                    if (fav) R.drawable.ic_filled_fav_24 else R.drawable.ic_empty_fav_24
                binding.pizzaListItemFavoriteButton.setImageResource(favImageId)
                binding.pizzaRecipeItemCard.setOnClickListener {
                    onItemClickListener(this)
                }
                Glide.with(binding.root.context)
                    .load(Firebase.storage.getReferenceFromUrl(imageUrl))
                    .into(binding.pizzaListItemImage)
            }

            binding.pizzaListItemFavoriteButton.setOnClickListener {
                onItemFavClickListener(pizza);
            }

            pizza.tags.forEachIndexed { index, tag ->
                (binding.chipGroupTags[index] as Chip).text = tag
            }
        }

    }

    class PizzaDiffCallback : DiffUtil.ItemCallback<PizzaListItem>() {
        override fun areItemsTheSame(oldItem: PizzaListItem, newItem: PizzaListItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PizzaListItem, newItem: PizzaListItem) =
            oldItem == newItem
    }

}