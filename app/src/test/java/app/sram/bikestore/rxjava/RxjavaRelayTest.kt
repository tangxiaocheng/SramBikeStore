package app.sram.bikestore.rxjava

import com.google.common.truth.Truth.*
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
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
    fun testPageList_CollectAllTheList() {

        val pageSubject: ReplaySubject<String> = ReplaySubject.create<String>(10)
        pageSubject.onNext(START_TOKEN)
        val observable = Observable.defer {
            return@defer pageSubject.concatMap { pageToken ->
                return@concatMap if (pageToken == END_TOKEN) {
                    Observable.empty<Any>().doOnComplete { pageSubject.onComplete() }
                } else {
                    Observable.just(dataSource[pageToken]!!)
                        .doOnNext { pageSubject.onNext(it.pageToken) }
                }
            }
        }

        val test = observable.test()
        assertThat(test.valueCount()).isEqualTo(5)
        assertThat(test.values()[0]).isEqualTo(dataSource[START_TOKEN])


        val resultTest = observable
            .cast(PageResult::class.java)
            .map { it.pageList }
            .flatMapIterable { it }
            .test()


        assertThat(resultTest.valueCount()).isEqualTo(3 * 5)
        assertThat(resultTest.values()).isEqualTo(dataSource.values.flatMap { it.pageList })

        resultTest.values().forEach { print("$it -> ") }


    }

}
