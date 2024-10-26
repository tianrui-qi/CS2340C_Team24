package com.example.sprint1;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.sprint1.model.MainModel;
import com.example.sprint1.model.MainModel.CallbackBool;
import com.example.sprint1.model.MainModel.CallbackDestination;
import com.example.sprint1.model.MainModel.CallbackVacation;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static MainModel mainModel;
    private boolean callbackResult;
    private HashMap<String, HashMap<String, String>> destinationsResult;
    private HashMap<String, String> vacationResult;

    @Before
    public void setup() {
        mainModel = MainModel.getInstance();
        callbackResult = false;
        destinationsResult = null;
        vacationResult = null;
    }


    private CallbackBool booleanCallback = result -> callbackResult = result;


    private CallbackDestination destinationCallback = result -> destinationsResult = result;


    private CallbackVacation vacationCallback = result -> vacationResult = result;
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.sprint1", appContext.getPackageName());
    }



    @Test
    public void testUserSignUpWithExistingUsername() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);

        mainModel.userSignUp("testUser", "testPassword",  result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testUserSignInWithCorrectCredentials() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("tianrui", "1", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);
    }

    @Test
    public void testUserSignInWithIncorrectPassword() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("tianrui", "2", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testAddDestinationWithValidData() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.addDestination("Paris", "01/01/2023", "01/11/2023", "10", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);
    }

    @Test
    public void testAddDestinationWithAdditionalValidData() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.addDestination("New York", "01/04/2023", "01/06/2023", "2", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);
    }

    @Test
    public void testAddDestinationWithDuplicateLocation() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.addDestination("Atlanta", "01/01/2023", "01/11/2023", "10", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.addDestination("Atlanta", "01/01/2023", "01/11/2023", "10", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(500, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testGetDestinationsWithExistingDestinations() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.addDestination("Paris", "01/01/2023", "01/11/2023", "10", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.getDestinations( result -> {
            destinationsResult = result;
            latch.countDown();
        });

        latch.await(500, TimeUnit.SECONDS);

        assertTrue(destinationsResult.containsKey("Paris"));
    }

    @Test
    public void testGetDestinationsWithNoDestinations() throws InterruptedException {
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.getDestinations( result -> {
            destinationsResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertNull(destinationsResult);
    }

    @Test
    public void testSetVacationWithValidDatesAndDuration() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("testUser", "testPassword",result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.setVacation("01/01/2023", "01/11/2023", "10",result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.getVacation( result -> {
            vacationResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);

        assertEquals("01/01/2023", vacationResult.get("startDate"));
        assertEquals("01/10/2023", vacationResult.get("endDate"));
        assertEquals("10", vacationResult.get("duration"));
    }

    @Test
    public void testSetVacationWithMismatchedDuration() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("testUser", "testPassword",result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.setVacation("01/10/2023", "01/01/2023", "5", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(500, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testCalculateOccupiedDaysWithValidVacationAndDestinations() throws InterruptedException {
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.setVacation("01/01/2023", "01/21/2023", "20", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.addDestination("Paris", "01/01/2023", "01/11/2023", "10", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);


    }

    @Test
    public void testCalculateOccupiedDaysWithNoOverlap() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);

        mainModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.setVacation("01/01/2023", "01/11/2023", "10", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainModel.addDestination("Paris", "01/15/2023", "01/20/2023","5", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);


    }




}