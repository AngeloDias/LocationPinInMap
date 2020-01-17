package self.training.locationpininmap

import androidx.fragment.app.FragmentManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import self.training.locationpininmap.MapsActivity.Companion.fragmentPinsDialogTag


@RunWith(AndroidJUnit4::class)
class MapsActivityInstrumentedTest {
    private lateinit var supportFrag: FragmentManager

    @get: Rule
    val intentsTestRule = IntentsTestRule(MapsActivity::class.java)

    @get: Rule
    val activityTestRule = ActivityTestRule<MapsActivity>(MapsActivity::class.java)

    @Before
    fun setUPFragment() {
        supportFrag = activityTestRule.activity.supportFragmentManager

        supportFrag.beginTransaction()
    }

    @Test
    fun testing(){
        Espresso.onView(ViewMatchers.withTagValue(Matchers.`is`(fragmentPinsDialogTag)))
    }

}