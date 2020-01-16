package self.training.locationpininmap

import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import self.training.locationpininmap.MapsActivity.Companion.fragmentPinsDialogTag

@RunWith(AndroidJUnit4::class)
class MapsActivityInstrumentedTest {
    @get: Rule
    val intentsTestRule = IntentsTestRule(MapsActivity::class.java)

    @Test
    fun testing(){
        Espresso.onView(ViewMatchers.withTagValue(Matchers.`is`(fragmentPinsDialogTag)))
    }

}