package app.sram.bikestore.di.thread

import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * This executor is to handle heavy work like io reading, network request, computation, etc. Manging
 * threading usually is a challenging work. By putting all complicated code here will enable us to
 * focus on business logic in use cases.
 *
 * Inspired from
 *
 * https://github.com/bufferapp/android-clean-architecture-boilerplate/blob/8d2b43e20b8c3cbcd9a93187c4c627a82b07c417/data/src/main/java/org/buffer/android/boilerplate/data/executor/JobExecutor.kt
 *
 */
class JobExecutor @Inject constructor() : Executor {
    var threadPoolExecutor: ThreadPoolExecutor
    override fun execute(runnable: Runnable) {
        threadPoolExecutor.execute(runnable)
    }

    internal class JobThreadFactory : ThreadFactory {
        private var counter = 0
        override fun newThread(runnable: Runnable): Thread {
            return Thread(runnable, THREAD_NAME + counter++)
        }

        companion object {
            const val THREAD_NAME = "android_"
        }
    }

    companion object {
        const val INITIAL_POOL_SIZE = 3
        const val MAX_POOL_SIZE = 6
        const val KEEP_ALIVE_TIME: Long = 10
        val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS
    }

    init {
        val workQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
        threadPoolExecutor = ThreadPoolExecutor(
            INITIAL_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            workQueue,
            JobThreadFactory()
        )
    }
}
