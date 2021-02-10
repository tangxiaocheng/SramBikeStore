package app.sram.bikestore.rxjava

import com.google.common.truth.Truth
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.ReplaySubject
import org.junit.After
import org.junit.Before
import org.junit.Test


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

    internal data class PageResult(val pageList: List<String>, val pageToken: String)

    private val START_TOKEN = "startToken"
    private val END_TOKEN = "endToken"

    private val dataSource = mapOf(
        START_TOKEN to PageResult(listOf("list1_1", "list1_2", "list1_3"), "tokenA"),
        "tokenA" to PageResult(listOf("list2_1", "list2_2", "list2_3"), "tokenB"),
        "tokenB" to PageResult(listOf("list3_1", "list3_2", "list3_3"), "tokenC"),
        "tokenC" to PageResult(listOf("list4_1", "list4_2", "list4_3"), "tokenD"),
        "tokenD" to PageResult(listOf("list5_1", "list5_2", "list5_3"), END_TOKEN)
    )


    private lateinit var io: Scheduler
    private lateinit var ui: Scheduler


    @Before
    fun setUp() {
        io = Schedulers.io()
        ui = Schedulers.trampoline()
    }

    @Test
    fun testTokenPage() {


        val defer = Observable.defer {
            val replaySubject = ReplaySubject.create<String>(10)
            replaySubject.concatMap { pageToken: String ->
                if (pageToken == END_TOKEN) {
                    return@concatMap Observable.empty<List<String>>().doOnComplete {
                        replaySubject.onComplete()
                    }
                } else {
                    return@concatMap Observable.just(dataSource[pageToken])
                        .doOnNext { replaySubject.onNext(it!!.pageToken) }
                }
            }
            replaySubject.onNext(START_TOKEN)
            replaySubject
        }
        val test = defer.test()
//        Truth.assertThat(test.valueCount()).isEqualTo(10)
        Truth.assertThat(test.values()).isNotEmpty()
    }


    @After
    fun tearDown() {
        io.shutdown()
        ui.shutdown()
    }
}
