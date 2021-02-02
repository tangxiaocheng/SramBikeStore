package app.sram.bikestore.activity

import android.content.Context
import app.sram.bikestore.di.thread.IOScheduler
import app.sram.bikestore.di.thread.UIScheduler
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.uber.autodispose.AutoDispose.autoDisposable
import com.uber.autodispose.ScopeProvider
import io.reactivex.Scheduler
import javax.inject.Inject

/**
 * A network helper that could be injected at any scopeProvider.
 */
class NetworkStatusHelper @Inject constructor(
    private val context: Context,
    @IOScheduler private val ioScheduler: Scheduler,
    @UIScheduler private val mainScheduler: Scheduler
) {

    fun observe(scopeProvider: ScopeProvider, callback: (Boolean) -> Unit) {
        ReactiveNetwork
            .observeNetworkConnectivity(context)
            .subscribeOn(ioScheduler)
            .map { it.available() }
            .observeOn(mainScheduler)
            .`as`(autoDisposable(scopeProvider))
            .subscribe(callback)
    }
}
