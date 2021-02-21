package app.sram.bikestore

import app.sram.bikestore.data.SramLocation
import app.sram.bikestore.util.calculate
import app.sram.bikestore.util.formatDistance
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DistanceUtilTest {

    private val home = SramLocation(47.584166043000366, -122.15145292817319)
    private val hotel = SramLocation(47.58078640955763, -122.15412440821234)
    private val bikeStore = SramLocation(47.62104165883578, -122.17876199467423)
    private val greggs = SramLocation(47.6115901, -122.2017231)

    @Test
    fun calculate_edge_case() {

        assertThat(formatDistance(SramLocation(0.0, 0.0).calculate(SramLocation(0.0, 0.0)))).isEqualTo("0.0 mi")
        assertThat(formatDistance(SramLocation(0.0, 1.0).calculate(SramLocation(0.0, 0.0)))).isEqualTo("69.2 mi")
        assertThat(formatDistance(SramLocation(0.0, 1.0).calculate(SramLocation(0.0, 1.0)))).isEqualTo("0.0 mi")
    }

    @Test
    fun calculate_real_life() {

        assertThat(formatDistance(home.calculate(hotel))).isEqualTo("0.3 mi")
        assertThat(formatDistance(bikeStore.calculate(home))).isEqualTo("2.8 mi")
        assertThat(formatDistance(home.calculate(bikeStore))).isEqualTo("2.8 mi")
        assertThat(formatDistance(home.calculate(greggs))).isEqualTo("3.0 mi")
    }
}
