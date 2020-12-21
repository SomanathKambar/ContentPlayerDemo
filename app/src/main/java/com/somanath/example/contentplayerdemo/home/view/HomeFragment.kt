package com.somanath.example.contentplayerdemo.home.view

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.somanath.example.contentplayerdemo.R
import com.somanath.example.contentplayerdemo.home.view_model.MainViewModel
import java.com.somanath.example.contentplayerdemo.home.interfaces.IMainViewModel
import java.com.somanath.example.contentplayerdemo.home.interfaces.IOnItemClickHandler

/**
 * A fragment representing a list of Items.
 */
class HomeFragment : Fragment() {

    private var columnCount = 1
    private lateinit var  iMainViewModel: IMainViewModel
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_list, container, false)
        setHasOptionsMenu(true)
        iMainViewModel = ViewModelProvider.AndroidViewModelFactory(activity?.application!!).create(MainViewModel::class.java)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                iMainViewModel.getContents().observe(viewLifecycleOwner, {
                    adapter = MyItemRecyclerViewAdapter(it.mediaItems,
                        object : IOnItemClickHandler {
                            override fun onItemClick(url: String) {
                                val bundle = Bundle()
                                bundle.putString("content_url",url)
                                findNavController().navigate(R.id.playerActivity,bundle)
                            }
                        })
                })
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}