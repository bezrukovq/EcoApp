package com.md.nails.presentation.basemvp

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>() {

    val compositeDisposable = CompositeDisposable()

    override fun destroyView(view: T) {
        super.destroyView(view)
        compositeDisposable.clear()
    }

    protected fun Disposable.disposeWhenDestroy() {
        compositeDisposable.add(this)
    }

}