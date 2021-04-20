package bg.dabulgaria.tibroish.presentation.base

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

interface IDisposableHandler {

    fun dispose()
    fun add(disposable: Disposable)
}

class DisposableHandler @Inject constructor() : IDisposableHandler {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun dispose() {
        compositeDisposable.clear()
    }

    override fun add(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
}