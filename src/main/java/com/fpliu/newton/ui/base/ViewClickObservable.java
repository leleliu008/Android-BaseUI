package com.fpliu.newton.ui.base;

import android.os.Looper;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

public final class ViewClickObservable extends Observable<View> {

    private View view;

    public ViewClickObservable(View view) {
        this.view = view;
    }

    @Override
    protected void subscribeActual(Observer<? super View> observer) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        ViewClickObservable.Listener listener = new ViewClickObservable.Listener(view, observer);
        observer.onSubscribe(listener);
        view.setOnClickListener(listener);
    }

    static final class Listener extends MainThreadDisposable implements View.OnClickListener {

        private final View view;
        private final Observer<? super View> observer;

        Listener(View view, Observer<? super View> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void onClick(View v) {
            if (!isDisposed()) {
                observer.onNext(v);
            }
        }

        @Override
        protected void onDispose() {
            view.setOnClickListener(null);
        }
    }
}
