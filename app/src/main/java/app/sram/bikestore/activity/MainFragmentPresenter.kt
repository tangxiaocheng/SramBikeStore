package app.sram.bikestore.activity

import app.sram.bikestore.data.*
import app.sram.bikestore.di.ui.FragmentScope
import com.uber.autodispose.AutoDispose.autoDisposable
import com.uber.autodispose.ScopeProvider
import javax.inject.Inject
/*
*
*
*
* */
@FragmentScope
class MainFragmentPresenter @Inject constructor(
    private val pageTokenRepo: PageTokenRepo,
    private val useCase: BikeStoreListUseCase,
    private val scopeProvider: ScopeProvider,
    private val mainFragmentCallback: MainFragmentCallback
) {
    fun loadData(location: ScramLocation) {
        mainFragmentCallback.loadingMoreSignal()
            .map { Token(pageTokenRepo.get()) }
            .startWith(Token())
            .distinct()
            .map { Param(location, it) }
            .switchMapSingle { useCase.execute(it) }
            .`as`(autoDisposable(scopeProvider))
            .subscribe(
                { onResultModelReady(it) },
                mainFragmentCallback::onNetworkError
            )
    }

    private fun onResultModelReady(
        resultModel: ResultModel<List<BikeStoreItem>>
    ) {
        when (resultModel) {
            is Success -> mainFragmentCallback.onResultReady(resultModel.data)
            is Failure -> mainFragmentCallback.onServerError(resultModel.errorModel)
        }
    }
}
