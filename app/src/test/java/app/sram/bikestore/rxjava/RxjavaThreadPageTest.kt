package app.sram.bikestore.rxjava

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before

/**
 * A pure RxJava prototype that is aim to mock the process of fetching all the pages of the Google map place API result.
 *
 * .
 */
class RxjavaThreadPageTest {

    /*
    * Initial token is "", last token is null.
    *
    *    ""  -> list1,tokenA
    * tokenA -> list2,tokenB
    * tokenB -> list3,tokenC
    * tokenC -> list4,tokenD
    * tokenD -> list5,null
    *
    * */

    private lateinit var io: Scheduler
    private lateinit var ui: Scheduler

    @Before
    fun setUp() {
        io = Schedulers.io()
        ui = Schedulers.trampoline()
    }

    @After
    fun tearDown() {
        io.shutdown()
        ui.shutdown()
    }
}
