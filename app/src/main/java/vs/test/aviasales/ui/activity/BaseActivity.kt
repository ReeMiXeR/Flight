package vs.test.aviasales.ui.activity

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


abstract class BaseActivity : AppCompatActivity() {

    private var uiDisposables = CompositeDisposable()

    fun safeSubscribe(action: () -> Disposable) {
        uiDisposables.add(action())
    }

    override fun onDestroy() {
        super.onDestroy()
        uiDisposables.dispose()
        uiDisposables = CompositeDisposable()
    }
}
