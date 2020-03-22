package com.md.nails.presentation.basemvp

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface BaseView : MvpView{
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(text: String)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(text: Int)
}