package vs.test.aviasales.di.modules

import org.koin.dsl.module
import vs.test.aviasales.data.network.Api
import vs.test.aviasales.data.network.ApiImpl
import vs.test.aviasales.data.storage.PersistentStorage
import vs.test.aviasales.data.storage.PersistentStorageImpl

val appModule = module {
    single<Api> { ApiImpl() }
    single<PersistentStorage> { PersistentStorageImpl(get()) }
}