package vs.test.aviasales.domain.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import vs.test.aviasales.domain.model.Airport
import vs.test.aviasales.domain.model.City
import vs.test.aviasales.domain.repository.SuggestRepository
import vs.test.aviasales.ui.adapter.diff.AirportItemDiff
import vs.test.aviasales.ui.model.DataDiffResult
import vs.test.aviasales.ui.model.select_airport.SelectAirportEvent
import vs.test.aviasales.ui.model.select_airport.SelectAirportState
import vs.test.aviasales.ui.viewmodel.SelectAirportViewModel
import vs.test.aviasales.utils.SingleLiveEvent

class SelectAirportViewModelImpl(
    private val suggestRepository: SuggestRepository
) : SelectAirportViewModel<SelectAirportState, SelectAirportEvent>() {

    private companion object {
        const val TAG = "SelectAirportViewModelImpl"
    }

    private val querySource = BehaviorRelay.create<String>()

    override val state = MutableLiveData<SelectAirportState>()
    override val events = SingleLiveEvent<SelectAirportEvent>()

    init {
        subscribeToQuery()
    }

    override fun onQueryChanged(query: CharSequence) = querySource.accept(query.toString())

    override fun onAirportClicked(item: Airport) {
        safeSubscribe {
            suggestRepository.insertAirport(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext {
                    Timber.tag(TAG).e(it)
                    Completable.complete()
                }
                .subscribe {
                    events.postValue(
                        SelectAirportEvent.OnAirportSelected(item)
                    )
                }
        }
    }

    override fun onCloseClicked() {
        events.postValue(SelectAirportEvent.Close)
    }

    private fun subscribeToQuery() {
        safeSubscribe {
            querySource
                .distinctUntilChanged()
                .doOnSubscribe { state.postValue(SelectAirportState.Loading) }
                .observeOn(Schedulers.io())
                .switchMap(::getCacheOrRequestData)
                .calculateDiff()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val shouldShowStub = querySource.value?.isBlank() == true && it.data.isEmpty()

                    state.postValue(
                        when {
                            shouldShowStub -> SelectAirportState.Stub
                            it.data.isEmpty() -> SelectAirportState.Empty
                            else -> SelectAirportState.Content(it)
                        }
                    )
                }, Timber.tag(TAG)::e)
        }
    }

    private fun getCacheOrRequestData(query: String): Observable<List<Airport>> {
        return when {
            query.isBlank() -> {
                suggestRepository
                    .getHistory()
                    .toObservable()
            }

            else -> {
                suggestRepository
                    .searchSuggest(query)
                    .toObservable()
                    .map(::toAirportList)
            }
        }.onErrorResumeNext { error: Throwable ->
            Timber.tag(TAG).e(error)
            Observable.empty<List<Airport>>()
        }
    }

    private fun toAirportList(list: List<City>): List<Airport> {
        return list.map {
            it.airports.map { airportCode ->
                Airport(
                    code = airportCode,
                    locationFullName = it.fullName,
                    cityName = it.name,
                    coordinates = it.coordinates,
                    countryName = it.country
                )
            }
        }.flatten()
    }

    private fun Observable<List<Airport>>.calculateDiff(): Observable<DataDiffResult<List<Airport>>> {
        return scan(DataDiffResult<List<Airport>>(emptyList())) { previousData, newData ->
            DataDiffResult(newData, DiffUtil.calculateDiff(AirportItemDiff(previousData.data, newData)))
        }.skip(1)
    }
}

