package es.uc3m.android.farmspot;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;


import org.junit.Rule;
import org.junit.Test;

@LargeTest
public class CombinedActivityTests {

    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public ActivityScenarioRule<SearchActivity> searchActivityRule =
            new ActivityScenarioRule<>(SearchActivity.class);

    @Test
    public void testNavigationToSearchActivity() {
        // Click on the search button in the bottom navigation bar
        Espresso.onView(withId(R.id.navigation_search))
                .perform(ViewActions.click());

        // Verify that SearchActivity is opened
        Espresso.onView(ViewMatchers.withId(R.id.navigation_search))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigationToAddActivity() {
        // Click on the add button in the bottom navigation bar
        Espresso.onView(withId(R.id.navigation_add))
                .perform(ViewActions.click());

        // Verify that AddActivity is opened
        Espresso.onView(ViewMatchers.withId(R.id.AddProduct))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigationToFavoritesActivity() {
        // Click on the favorites button in the bottom navigation bar
        Espresso.onView(withId(R.id.navigation_purchases))
                .perform(ViewActions.click());

        // Verify that FavoritesActivity is opened
        Espresso.onView(withId(R.id.navigation_purchases)
                .clone(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testNavigationToProfileActivity() {
        // Click on the profile button in the bottom navigation bar
        Espresso.onView(withId(R.id.navigation_profile))
                .perform(ViewActions.click());

        // Verify that ProfileActivity is opened
        Espresso.onView(ViewMatchers.withId(R.id.navigation_profile))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
