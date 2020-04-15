package com.example.ecoapp.feature.webmapview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.example.ecoapp.R
import com.example.ecoapp.feature.main.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class BlankFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar?.hide()
        return null
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showMap()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).hideMap()
    }

}
