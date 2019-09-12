package com.fpliu.newton.ui.base

import android.os.Looper
import android.view.View

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class ViewClickObservable(private val view: View) : Observable<View>() {

    override fun subscribeActual(observer: Observer<in View>) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return
        }
        ViewClickObservable.Listener(view, observer).let {
            observer.onSubscribe(it)
            view.setOnClickListener(it)
        }
    }

    internal class Listener(private val view: View, private val observer: Observer<in View>) : MainThreadDisposable(), View.OnClickListener {

        override fun onClick(v: View) {
            if (!isDisposed) {
                observer.onNext(v)
            }
        }

        override fun onDispose() {
            view.setOnClickListener(null)
        }
    }
}
