package com.example.ecoapp.feature.preparing

import android.os.Bundle
import android.view.View
import com.example.ecoapp.R
import com.example.ecoapp.feature.main.MainActivity
import com.example.ecoapp.feature.simplelist.SimpleListAdapter
import com.md.nails.presentation.basemvp.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_markings_list.*

class PreparingFragment: BaseMvpFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_markings_list

    val adapter = PreparingListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.show()
        markings_list.adapter = adapter
    }
}