package pl.qbasso.omisechallenge.presenter

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


abstract class BasePresenter<V : Any> {

    lateinit var view: V
    private val disposables = CompositeDisposable()

    fun attach(view: V) {
        this.view = view
    }

    fun detach() {
        disposables.clear()
    }

    protected fun disposableOnDetach(d: Disposable) {
        if (!disposables.isDisposed) {
            disposables.add(d)
        }
    }

    fun io(): Scheduler = Schedulers.io()

    fun main(): Scheduler = AndroidSchedulers.mainThread()

}
