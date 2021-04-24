package ru.ifmo.mushrooms

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class MushroomsInstrumentalTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)


    @Before
    fun before() {
        val dir = File(activityRule.activity.cacheDir, "placesInfo")
        dir.deleteRecursively()
    }


    @Test
    fun testMenuToDetect() {
        onView(withId(R.id.placesOnMapMenu))
            .perform(click())

        onView(withText(R.string.detect_item))
            .perform(click())

        onView(withId(R.id.detectMushroomMenu))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testMenuToCatalog() {
        onView(withId(R.id.placesOnMapMenu))
            .perform(click())

        onView(withText(R.string.catalog_item))
            .perform(click())

        onView(withId(R.id.mushroomsCatalogMenu))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testMenuToPlaces() {
        onView(withId(R.id.placesOnMapMenu))
            .perform(click())

        onView(withText(R.string.places_item))
            .perform(click())

        onView(withId(R.id.placesOnMapMenu))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testToListView() {
        onView(withId(R.id.toListButton))
            .perform(click())

        onView(withId(R.id.placesListMenu)).check(matches(isDisplayed()))
    }


    @Test
    fun testAddPlace() {
        onView(withId(R.id.addPlaceButton))
            .perform(click())

        val placeName = "abc123"

        onView(withId(R.id.placeName))
            .perform(clearText(), typeText(placeName), closeSoftKeyboard())

        onView(withId(R.id.saveButton))
            .perform(click())

        onView(withId(R.id.toListButton))
            .perform(click())

        onView(withText(placeName)).check(matches(isDisplayed()))
    }

    @Test
    fun testDetectMushroom() {
        onView(withId(R.id.placesOnMapMenu))
            .perform(click())

        onView(withText(R.string.detect_item))
            .perform(click())

        for (i in 0..5) {
            onView(withText(R.string.next_item)).perform(click())
            onView(withText(R.string.back_item)).perform(click())
            onView(withText(R.string.next_item)).perform(click())
        }

        onView(withText(R.string.result)).check(matches(isDisplayed()))
    }

    @Test
    fun testCatalog() {
        onView(withId(R.id.placesOnMapMenu))
            .perform(click())

        onView(withText(R.string.catalog_item))
            .perform(click())

        onView(withId(R.id.toNotEdibleCatalog))
            .perform(click())

        onView(withText("Мухомор красный")).perform(click())

        onView(withId(R.id.mushroomInfoMenu)).check(matches(isDisplayed()))

        onView(withId(R.id.imageSlider)).perform(swipeLeft())
        onView(withId(R.id.imageSlider)).perform(swipeLeft())

        onView(withId(R.id.imageSlider)).perform(swipeRight())
        onView(withId(R.id.imageSlider)).perform(swipeRight())
        onView(withId(R.id.imageSlider)).perform(swipeRight())

        onView(withId(R.id.mushroomInfoMenu)).perform(click())
        onView(withText(R.string.places_item))
            .perform(click())

        onView(withId(R.id.placesOnMapMenu)).check(matches(isDisplayed()))
    }
}
