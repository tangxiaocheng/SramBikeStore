package app.sram.bikestore

import app.sram.bikestore.data.*
import app.sram.bikestore.activity.BikeStoreListUseCase
import app.sram.bikestore.util.modelToEntity
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

val TEST_PARAM = Param(ScramLocation(47.584166043000366, -122.15145292817319))

@RunWith(RobolectricTestRunner::class)
class BikeStoreListUseCaseTest {

    private val repoUs: BikeRepo = mockk()
    private val testScheduler = TestScheduler()

    @Test
    fun entityToModelTest() {
        // given
        every { repoUs.list(TEST_PARAM) } returns Single.just(input())
        val useCase = BikeStoreListUseCase(repoUs, testScheduler, testScheduler)
        val test: TestObserver<ResultModel<List<BikeStoreItem>>> = useCase.execute(TEST_PARAM).test()
        test.assertNoErrors()
        test.assertValueCount(0)

        // when
        testScheduler.triggerActions()

        // then
        test.assertValueCount(1)
        Truth.assertThat(test.values().first()).isInstanceOf(Success::class.java)
        val data: List<BikeStoreItem> = (test.values().first() as Success).data
        Truth.assertThat(data.size).isEqualTo(4)
        Truth.assertThat(data.first()).isInstanceOf(BikeStoreItem::class.java)
        Truth.assertThat(data.first().bikeStoreEntity.name).isEqualTo("Nearest WheelLab")
        Truth.assertThat(data.first().distance).isWithin(0.01f).of(2367.8901f)
    }

    @Test
    fun entityToModelTestFail() {
        // given
        every { repoUs.list(TEST_PARAM) } returns Single.just(inputOfFail())
        val useCase = BikeStoreListUseCase(repoUs, testScheduler, testScheduler)
        val test: TestObserver<ResultModel<List<BikeStoreItem>>> = useCase.execute(TEST_PARAM).test()
        test.assertNoErrors()
        test.assertValueCount(0)

        // when
        testScheduler.triggerActions()

        // then
        test.assertValueCount(1)
        Truth.assertThat(test.values().first()).isInstanceOf(Failure::class.java)
        val errorModel = (test.values().first() as Failure).errorModel
        Truth.assertThat(errorModel.resultCode).isEqualTo(404)
    }

    @Test
    fun entityToModelTestFailOfWrongAPI_Status() {
        // given
        every { repoUs.list(TEST_PARAM) } returns Single.just(inputOfFailWrongApiStatusCode())
        val useCase = BikeStoreListUseCase(repoUs, testScheduler, testScheduler)
        val test: TestObserver<ResultModel<List<BikeStoreItem>>> = useCase.execute(TEST_PARAM).test()
        test.assertNoErrors()
        test.assertValueCount(0)

        // when
        testScheduler.triggerActions()

        // then
        test.assertValueCount(1)
        Truth.assertThat(test.values().first()).isInstanceOf(Failure::class.java)
        val errorModel = (test.values().first() as Failure).errorModel
        Truth.assertThat(errorModel.resultCode).isEqualTo(200)
        Truth.assertThat(errorModel.apiStatusCode).isEqualTo("REQUEST_DENIED")
    }

    private fun input(): ResultModel<BikeStoreData> {
        return Success(
            BikeStoreData(
                parseJsonToList(
                    readFileToString("200.json", this),
                    MapApiResponse::class.java
                ).results.map { modelToEntity(it) }
            )
        )
    }

    private fun inputOfFail(): ResultModel<BikeStoreData> {
        return Failure(ErrorModel("", "data not found", 404))
    }

    private fun inputOfFailWrongApiStatusCode(): ResultModel<BikeStoreData> {
        return Failure(ErrorModel("REQUEST_DENIED", "The provided API key is invalid.", 200))
    }
}
