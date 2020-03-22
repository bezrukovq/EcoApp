package com.md.nails.presentation.basemvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment

abstract class BaseMvpFragment : MvpAppCompatFragment(), BaseView {

    abstract val layoutId: Int

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun showMessage(text: Int) {
        val activity = activity
        if (activity != null)
            Toast.makeText(activity, text.toString(), Toast.LENGTH_LONG).show()
    }

    override fun showMessage(text: String) {
        val activity = activity
        if (activity != null)
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

}