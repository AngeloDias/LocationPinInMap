package self.training.locationpininmap

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test
import self.training.locationpininmap.utils.PinLocation

class PinLocationUnitTest {

    companion object {
        private var pinLocation: PinLocation = PinLocation("CIn", 4, -8.0548025, -34.9516363)
    }

    @Test
    fun nomeNotEmpty() {
        assertThat(pinLocation.nome, Matchers.not(Matchers.isEmptyString()))
    }

    @Test
    fun categoriaValidNumber() {
        assertThat(pinLocation.categoria, Matchers.greaterThan(0))
        assertThat(pinLocation.categoria, Matchers.lessThanOrEqualTo(5))
    }

    @Test
    fun validLatitudeRange() {
        assertThat(pinLocation.latitude, Matchers.greaterThanOrEqualTo(-90.0))
        assertThat(pinLocation.latitude, Matchers.lessThanOrEqualTo(90.0))
    }

    @Test
    fun validLongitudeRange() {
        assertThat(pinLocation.longitude, Matchers.greaterThanOrEqualTo(-180.0))
        assertThat(pinLocation.longitude, Matchers.lessThanOrEqualTo(180.0))
    }

}
