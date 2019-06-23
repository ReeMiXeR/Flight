package vs.test.aviasales

import android.app.Application
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import vs.test.aviasales.di.modules.appModule
import vs.test.aviasales.di.modules.mapModule
import vs.test.aviasales.di.modules.routeModule
import vs.test.aviasales.di.modules.selectAirportModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, selectAirportModule, routeModule, mapModule))
        }
    }
}