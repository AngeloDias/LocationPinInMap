package self.training.locationpininmap

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MapsActivityInstrumentedTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule<MapsActivity>(MapsActivity::class.java)

    @Test
    fun clickOnActionBar() {
        var viewId = 0

        onView(withId(R.id.action_filter)).check { view, _ ->
            viewId = view.id
        }.perform(click())

        onView(withId(viewId)).perform(pressBack())
    }

}
