
package com.linx.stress_free_app;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.widget.Toast;

@RunWith(AndroidJUnit4.class)
public class BedTimeFragmentTest {

    @Rule
    public ActivityScenarioRule<MoodSurveyActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void typewriterText2_isCorrect() {
        onView(withId(R.id.typewriter_text2)).check(matches(withText("Please enter what time you go to bed")));
    }

    @Test
    public void submitTime_displaysToast() {
        onView(withId(R.id.editHoursText)).perform(typeText("10.5"));
        onView(withId(R.id.sumbitTime)).perform(click());

        activityRule.getScenario().onActivity(activity -> {
            Toast latestToast = ShadowToast.getLatestToast();
            assertThat(latestToast, Matchers.notNullValue());
            assertThat(ShadowToast.getTextOfLatestToast(), is("10.5"));
        });
    }
}
