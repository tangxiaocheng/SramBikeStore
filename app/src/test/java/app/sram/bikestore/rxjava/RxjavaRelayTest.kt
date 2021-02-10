package app.sram.bikestore.rxjava

import com.google.common.truth.Truth
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.observables.GroupedObservable
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
class RxjavaRelayTest {

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


    private val START_TOKEN = "startToken"
    private val END_TOKEN = "endToken"

    private val pageTokenA = "tokenA"
    private val pageTokenB = "tokenB"
    private val pageTokenC = "tokenC"
    private val pageTokenD = "tokenD"
    private val dataSource = linkedMapOf(
        START_TOKEN to PageResult(listOf("list1_1", "list1_2", "list1_3"), pageTokenA),
        pageTokenA to PageResult(listOf("list2_1", "list2_2", "list2_3"), pageTokenB),
        pageTokenB to PageResult(listOf("list3_1", "list3_2", "list3_3"), pageTokenC),
        pageTokenC to PageResult(listOf("list4_1", "list4_2", "list4_3"), pageTokenD),
        pageTokenD to PageResult(listOf("list5_1", "list5_2", "list5_3"), END_TOKEN)
    )


    @Test
    fun testTokenPage() {


        val observable = Observable.defer {
            val pageSubject = ReplaySubject.create<String>(10)

            val nextPageSubject = pageSubject.concatMap { pageToken: String ->
                if (pageToken == END_TOKEN) {
                    return@concatMap Observable.empty<List<String>>().doOnComplete {
                        pageSubject.onComplete()
                    }
                } else {
                    return@concatMap Observable.just(dataSource[pageToken])
                        .doOnNext { pageSubject.onNext(it!!.pageToken) }
                }
            }
            pageSubject.onNext(START_TOKEN)
            nextPageSubject
        }
        val test = observable.test()
        Truth.assertThat(test.valueCount()).isEqualTo(5)
        Truth.assertThat(test.values()[0]).isEqualTo(dataSource[START_TOKEN])
        Truth.assertThat(test.values()[1]).isEqualTo(dataSource[pageTokenA])
        Truth.assertThat(test.values()[2]).isEqualTo(dataSource[pageTokenB])
        Truth.assertThat(test.values()[3]).isEqualTo(dataSource[pageTokenC])
        Truth.assertThat(test.values()[4]).isEqualTo(dataSource[pageTokenD])



    }

}
