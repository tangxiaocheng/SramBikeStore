package app.sram.bikestore

import app.sram.bikestore.data.*
import app.sram.bikestore.util.modelToEntity
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers

class BikeRemoteDataSourceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var url: HttpUrl
    private lateinit var imp: BikeRemoteDataSource
    lateinit var pageTokenRepo: PageTokenRepo

    @Before
    fun setUp() {

        // given
        pageTokenRepo = mock {
            on { put(ArgumentMatchers.anyString()) } doReturn true
            on { get() } doReturn null
        }

        mockWebServer = MockWebServer()
        mockWebServer.start()
        url = mockWebServer.url("/")
        imp = BikeRemoteDataSource(RestApiHelper.getRestApi(url), pageTokenRepo)
    }

    @Test
    fun shouldGetSuccessResultModelAndUpdatePageToken() {
        // given
        val mockBody: String = readFileToString("200_min.json", this)
        // when
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockBody))
        val testObserver = imp.fetch(TEST_PARAM).test()

        // Then
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val resultModel: ResultModel<BikeStoreData> = testObserver.values()[0]
        assertThat(resultModel).isInstanceOf(Success::class.java)
        assertThat(resultModel).isEqualTo(successExpected())

        assertThat((resultModel as Success).data.list[0].location).isEqualTo(ScramLocation(47.6203442, -122.3297537))
        // page token updated
        verify(pageTokenRepo).put("mockPageToken")
    }

    @Test
    fun shouldGet404FromSever() {
        // given
        val mockBody = " 404 not find"
        // when
        mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(mockBody))
        val testObserver = imp.fetch(TEST_PARAM).test()

        // Then
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val resultModel: ResultModel<BikeStoreData> = testObserver.values()[0]
        assertThat(resultModel).isInstanceOf(Failure::class.java)
        assertThat((resultModel as Failure).errorModel.resultCode).isEqualTo(404)
    }

    @Test
    fun shouldGetRequestDeniedStatusFromMapApi() {
        // given
        val mockBody = readFileToString("200_REQUEST_DENIED.json", this)
        // when
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockBody))
        val testObserver = imp.fetch(TEST_PARAM).test()

        // Then
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
        val resultModel: ResultModel<BikeStoreData> = testObserver.values()[0]
        assertThat(resultModel).isInstanceOf(Failure::class.java)
        val failure = resultModel as Failure
        assertThat(failure.errorModel.resultCode).isEqualTo(200)
        assertThat(failure.errorModel.apiStatusCode).isEqualTo("REQUEST_DENIED")
    }

    private fun successExpected(): Success<BikeStoreData> {
        return Success(
            BikeStoreData(
                parseJsonToList(
                    readFileToString("200_min.json", this),
                    MapApiResponse::class.java
                ).results.map { modelToEntity(it) }
            )
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
