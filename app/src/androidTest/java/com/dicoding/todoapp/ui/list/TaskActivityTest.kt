package com.dicoding.todoapp.ui.list

import android.widget.DatePicker
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.add.AddTaskActivity
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//TODO 16 : Write UI test to validate when user tap Add Task (+), the AddTaskActivity displayed
@RunWith(AndroidJUnit4::class)
@LargeTest
class TaskActivityTest {

    private val dummyTitle = "kegiatan seru"
    private val dummyDescription = "deskripsi kegiatan yang katanya seru itu"
    private val dummyDueDate = "20/12/2024"

    @get:Rule
    val activity = ActivityScenarioRule(TaskActivity::class.java)

    @Test
    fun addTask_Success(){

        Intents.init()

        onView(withId(R.id.fab)).check(matches(isDisplayed()))
        onView(withId(R.id.fab)).perform(click())

        Intents.intended(IntentMatchers.hasComponent(AddTaskActivity::class.java.name))

        onView(withId(R.id.add_ed_title)).check(matches(isDisplayed()))
        onView(withId(R.id.add_ed_title)).perform(typeText(dummyTitle))
        closeSoftKeyboard()
        onView(withId(R.id.add_ed_description)).perform(typeText(dummyDescription))
        closeSoftKeyboard()

        onView(withId(R.id.ib_datePicker)).check(matches(isDisplayed()))
        onView(withId(R.id.ib_datePicker)).perform(click())

        onView(withClassName(equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2024, 12, 20))  // Atur tanggal di DatePicker

        onView(withText("OK")).perform(click())  // Ini adalah tombol "OK" di DatePicker

        onView(withId(R.id.add_tv_due_date)).check(matches(withText(dummyDueDate)))

        onView(withId(R.id.action_save)).perform(click())

//        Intents.intended(IntentMatchers.hasComponent(TaskActivity::class.java.name))
    }

}