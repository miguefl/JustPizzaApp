package com.migferlab.justpizza.features.pizza.detail

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.view.setMargins
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.R
import com.migferlab.justpizza.core.DOT
import com.migferlab.justpizza.core.SPACE
import com.migferlab.justpizza.databinding.PizzaDetailBinding
import com.migferlab.justpizza.databinding.TagChipBinding
import com.migferlab.justpizza.di.viewModel
import com.migferlab.justpizza.features.base.BaseFragment


class PizzaRecipeDetailFragment : BaseFragment() {

    private val args: PizzaRecipeDetailFragmentArgs by navArgs()

    private lateinit var binding: PizzaDetailBinding
    private val viewModel: PizzaRecipeDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PizzaDetailBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setUp(args)
        setToolbarTitle(args.pizzaRecipeTittle)

        viewModel.getLiveData().observe {

            when (it) {
                is Result.Success -> renderPizza(it.value)
                is Result.Empty, is Result.Loading, is Result.Failure -> {
                }
            }
        }

        binding.pizzaDetailItemFavoriteButton.setOnClickListener {
            viewModel.favoritePizza()
        }
    }

    private fun renderPizza(detailViewState: PizzaRecipeDetailViewState) {

        Glide.with(this).load(
            Firebase.storage.getReferenceFromUrl(detailViewState.imageUrl)
        ).into(binding.pizzaDetailItemImage)
        setToolbarTitle(detailViewState.name)
        binding.pizzaDetailItemName.text = detailViewState.name

        val favImageId =
            if (detailViewState.fav) R.drawable.ic_filled_fav_24 else R.drawable.ic_empty_fav_24
        binding.pizzaDetailItemFavoriteButton.setImageResource(favImageId)

        renderIngredientsChips(detailViewState)
        renderDifficultyText(detailViewState)
        renderTimeText(detailViewState)

        binding.pizzaDetailSteps.removeAllViews()
        detailViewState.recipeSteps.forEachIndexed { index, step ->
            renderPizzaRecipeStep(index, step)
        }

        binding.pizzaDetailRecipePeople.text = detailViewState.people.toString()
    }

    private fun renderIngredientsChips(detailViewState: PizzaRecipeDetailViewState) {
        binding.tagDetailChipGroup.removeAllViews()
        detailViewState.ingredients.forEach {
            val chip = TagChipBinding.inflate(layoutInflater).root
            chip.text = if (it.unit == PizzaRecipeDetailViewState.UnitDetailView.CUSTOM)
                getString(
                    R.string.ingredientChipWithoutQtyFormat,
                    it.name,
                    getString(it.unit.unit)
                ) else
                getString(
                    R.string.ingredientChipFormat,
                    it.name,
                    it.quantity,
                    getString(it.unit.unit)
                )
            binding.tagDetailChipGroup.addView(chip)
        }
    }

    private fun renderDifficultyText(detailViewState: PizzaRecipeDetailViewState) {
        val difficulty = SpannableStringBuilder().bold { append(getString(R.string.difficulty)) }
            .append(SPACE)
            .append(getString(detailViewState.difficulty.difficulty))
        binding.pizzaDetailRecipeTime.text = difficulty
    }

    private fun renderTimeText(detailViewState: PizzaRecipeDetailViewState) {
        val time = SpannableStringBuilder().bold { append(getString(R.string.time)) }
            .append(SPACE)
            .append(detailViewState.time.toString())
            .append(SPACE)
            .append(getString(R.string.minUnit))
        binding.pizzaDetailRecipeDifficulty.text = time
    }

    private fun renderPizzaRecipeStep(index: Int, step: String) {
        val textStep = TextView(this.context)

        textStep.text =
            SpannableStringBuilder().bold { append((index + 1).toString()).append(DOT) }
                .append(SPACE)
                .append(step)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(16)

        textStep.layoutParams = params

        binding.pizzaDetailSteps.addView(textStep)
    }
}