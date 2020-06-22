package com.migferlab.justpizza.features.pizza.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.databinding.PizzaListBinding
import com.migferlab.justpizza.di.viewModel
import com.migferlab.justpizza.features.base.BaseFragment


class PizzaRecipeListFragment : BaseFragment() {

    private val viewModel: PizzaRecipeListViewModel by viewModel()
    private lateinit var binding: PizzaListBinding
    private val args: PizzaRecipeListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.loadPizzaRecipes()
        return PizzaListBinding.inflate(inflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setUp(args)

        val pizzaRecipeListAdapter = PizzaRecipeListAdapter(
            {
                findNavController().navigate(
                    PizzaRecipeListFragmentDirections.actionPizzaListToDetail(it.id, it.name)
                )
            },
            {
                viewModel.favoritePizza(it.id)
            }
        )

        binding.pizzaRecipeListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.pizzaRecipeListRecyclerView.adapter =
            MergeAdapter(pizzaRecipeListAdapter)

        viewModel.getLiveData().observe {
            when (it) {
                is Result.Success -> {
                    pizzaRecipeListAdapter.submitList(it.value.pizzas)
                }
                is Result.Loading -> {
                }
                is Result.Failure -> {
                }
                is Result.Empty -> {
                }
            }
        }
    }
}