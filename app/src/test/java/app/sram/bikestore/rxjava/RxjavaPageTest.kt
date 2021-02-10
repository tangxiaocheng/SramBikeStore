package app.sram.bikestore.rxjava

import com.google.common.truth.Truth
import io.reactivex.Observable
import org.junit.Test

/**
 * A pure RxJava prototype that is aim to mock the process of fetching all the pages of the Google map place API result.
 *
 * .
 */
class RxjavaPageTest {

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

    internal data class PageResult(val pageList: List<String>, val pageToken: String?)

    private val dataSource = mapOf(
        "" to PageResult(listOf("list1_1", "list1_2", "list1_3"), "tokenA"),
        "tokenA" to PageResult(listOf("list2_1", "list2_2", "list2_3"), "tokenB"),
        "tokenB" to PageResult(listOf("list3_1", "list3_2", "list3_3"), "tokenC"),
        "tokenC" to PageResult(listOf("list4_1", "list4_2", "list4_3"), "tokenD"),
        "tokenD" to PageResult(listOf("list5_1", "list5_2", "list5_3"), "null")
    )

    @Test
    fun testTokenPage() {
        val initToken = ""
        val resList = mutableListOf<String>()
        fetchPageByToken(initToken, resList)
        Truth.assertThat(resList.size).isEqualTo(3 * 5)
    }

    private fun fetchPageByToken(token: String, list: MutableList<String>) {
        Observable.just(token)
            .flatMap { getPage(it, list) }
            .filter { it != "null" }
            .subscribe { fetchPageByToken(it, list) }
    }

    private fun getPage(token: String, list: MutableList<String>): Observable<String> {
        list.addAll(dataSource[token]!!.pageList)
        return Observable.just(dataSource[token]!!.pageToken)
    }
}
