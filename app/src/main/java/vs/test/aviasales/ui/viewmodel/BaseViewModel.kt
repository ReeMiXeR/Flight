package vs.test.aviasales.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import vs.test.aviasales.utils.SingleLiveEvent

abstract class BaseViewModel<State, Event> : ViewModel() {
    abstract val state: MutableLiveData<State>
    abstract val events: SingleLiveEvent<Event>

    private var compositeDisposable = CompositeDisposable()

    fun safeSubscribe(action: () -> Disposable) {
        compositeDisposable.add(action())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
    }
}