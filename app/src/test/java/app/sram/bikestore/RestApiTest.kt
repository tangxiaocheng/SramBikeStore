package app.sram.bikestore

import app.sram.bikestore.data.RestApi
import app.sram.bikestore.di.room.DataBaseDao
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class RestApiTest {
    private val dao: DataBaseDao = mockk()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var url: HttpUrl
    private lateinit var imp: RestApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        url = mockWebServer.url("/")
        imp = RestApiHelper.getRestApi(url)
    }

    @Test
    fun shouldSentToCorrectAPI() {

        // given
        val mockBody = readFileToString("200.json", this)
        every { dao.getBikeStoreList() } returns emptyList()

        // when
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockBody))
        val testObserver = imp.getBikeStoreListByLocation(TEST_PARAM.location.toQueryString()).test()
        val request = mockWebServer.takeRequest()

        // then
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        assertThat(request.path).contains(
            "&radius=50000&type=bicycle_store&key"
        )
        assertThat(request.method).isEqualTo("GET")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
