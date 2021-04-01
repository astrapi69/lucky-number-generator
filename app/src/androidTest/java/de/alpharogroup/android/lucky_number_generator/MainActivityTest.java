package de.alpharogroup.android.lucky_number_generator;

import android.widget.Button;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void ensureButtonIsPresent() throws Exception {
        ActivityScenario scenario = rule.getScenario();
        scenario.onActivity(activity -> {

            Button viewById = activity.findViewById(R.id.btn_lottery_6_of_49);
            assertThat(viewById, notNullValue());
            assertThat(viewById, instanceOf(Button.class));
        });

    }
}