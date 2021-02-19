package app.sram.bikestore

import app.sram.bikestore.data.*
import app.sram.bikestore.data.mapper.JsonToEntityMapper
import app.sram.bikestore.util.modelToEntity
import com.google.common.truth.Truth.assertThat
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class BikeRemoteDataSourceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var url: HttpUrl
    private lateinit var imp: BikeRemoteDataSource

    @Before
    fun setUp() {

        mockWebServer = MockWebServer()
        mockWebServer.start()
        url = mockWebServer.url("/")
        imp = BikeRemoteDataSource(RestApiHelper.getRestApi(url), JsonToEntityMapper())
    }

    @Test
    fun shouldGetSuccessResultModelAndUpdatePageToken() {
        // given
        val mockBody: String = readFileToString("200_min.json", this)
        // when
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockBody))

        // Then
        val resultModel: ResultModel<BikeStoreData> = imp.fetch(TEST_PARAM)
        assertThat(resultModel).isInstanceOf(Success::class.java)
        assertThat(resultModel).isEqualTo(successExpected())

        assertThat((resultModel as Success).data.list[0].location).isEqualTo(ScramLocation(47.6203442, -122.3297537))
    }

    @Test
    fun shouldGet404FromSever() {
        // given
        val mockBody = " 404 not find"
        // when
        mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(mockBody))

        // Then
        val resultModel: ResultModel<BikeStoreData> = imp.fetch(TEST_PARAM)
        assertThat(resultModel).isInstanceOf(Failure::class.java)
        assertThat((resultModel as Failure).errorModel.resultCode).isEqualTo(404)
    }

    @Test
    fun shouldGetRequestDeniedStatusFromMapApi() {
        // given
        val mockBody = readFileToString("200_REQUEST_DENIED.json", this)
        // when
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(mockBody))
        val resultModel: ResultModel<BikeStoreData> = imp.fetch(TEST_PARAM)
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
