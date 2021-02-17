package app.sram.bikestore.activity

import app.sram.bikestore.data.*
import app.sram.bikestore.di.ui.FragmentScope
import com.uber.autodispose.AutoDispose.autoDisposable
import com.uber.autodispose.ScopeProvider
import javax.inject.Inject
/*
*
* Observe the signal from UI and transform the signal to the command of fetching data through use case.
*
* */
@FragmentScope
class AllPagesPresenter @Inject constructor(
    private val useCase: AllPagesUseCase,
    private val scopeProvider: ScopeProvider,
    private val mainFragmentCallback: MainFragmentCallback
) {
    fun loadData(location: ScramLocation) {
        useCase.execute(location)
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
