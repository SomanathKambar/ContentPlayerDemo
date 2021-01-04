package com.somanath.example.contentplayerdemo.home.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.somanath.example.contentplayerdemo.R
import com.somanath.example.contentplayerdemo.databinding.FragmentHomeListBinding
import com.somanath.example.contentplayerdemo.home.view_model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.com.somanath.example.contentplayerdemo.home.interfaces.IOnItemClickHandler

/**
 * A fragment representing a list of Items.
 */
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home_list) {

    private var columnCount = 1
    private val  iMainViewModel =  viewModels<MainViewModel>()
    private lateinit var navController: NavController
    private var _binding: FragmentHomeListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeListBinding.bind(view)
        binding.apply {
            if (view is RecyclerView) {
                with(view) {
                    layoutManager = when {
                        columnCount <= 1 -> LinearLayoutManager(context)
                        else -> GridLayoutManager(context, columnCount)
                    }

                    iMainViewModel.value.getContents().observe(viewLifecycleOwner, {
                        adapter = MyItemRecyclerViewAdapter(
                            object : IOnItemClickHandler {
                                override fun onItemClick(url: String) {
                                    val bundle = Bundle()
                                    bundle.putString("content_url",url)
                                    findNavController().navigate(R.id.playerActivity,bundle)
                                }
                            })

                        adapter.updateData(it.mediaItems) as MyItemRecyclerViewAdapter
                    })
                }
            }
        }
    }
    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}