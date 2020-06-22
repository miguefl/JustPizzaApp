package com.migferlab.justpizza.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hoopcarpool.fluxy.Result
import com.migferlab.justpizza.databinding.HomeBinding
import com.migferlab.justpizza.di.viewModel
import com.migferlab.justpizza.features.base.BaseFragment
import kotlinx.android.synthetic.main.home.*

class HomeFragment() : BaseFragment() {

    private lateinit var binding: HomeBinding
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = HomeBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pizzaRecipeListButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToPizzaList())
        }

        binding.favsButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToFavsList(favs = true))
        }

        binding.pizzaRandomButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToRandom(random = true))
        }

        binding.pizzaMap.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToRestaurants())
        }

        binding.signOutButton.setOnClickListener {
            viewModel.logOut()
        }


        viewModel.getLiveData().observe {
            when (it) {
                is Result.Success ->{
                }
                is Result.Loading -> {
                }
                is Result.Empty -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeSignOut()
                    )
                }
                is Result.Failure -> {
                }
            }
        }
    }

}