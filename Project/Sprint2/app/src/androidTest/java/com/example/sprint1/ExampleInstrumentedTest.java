package com.example.sprint1;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.example.sprint1.model.MainModel.CallbackBool;
import com.example.sprint1.model.MainModel.CallbackDestination;
import com.example.sprint1.model.MainModel.CallbackVacation;
import com.example.sprint1.viewmodel.MainViewModel;

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
    private static MainViewModel mainViewModel;
    private boolean callbackResult;
    private HashMap<String, HashMap<String, String>> destinationsResult;
    private HashMap<String, String> vacationResult;

    @Before
    public void setup() {
        mainViewModel = new MainViewModel();
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

        mainViewModel.userSignUp("testUser", "testPassword", result -> {
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
        mainViewModel.userSignIn("tianrui", "1", result -> {
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
        mainViewModel.userSignIn("tianrui", "2", result -> {
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
        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainViewModel.addDestination("Paris", "01/01/2023", "01/11/2023",  result -> {
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
        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainViewModel.addDestination("New York", "01/04/2023", "01/06/2023",  result -> {
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
        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);
        // add back if database was cleared
//        CountDownLatch latch2 = new CountDownLatch(1);
//        mainViewModel.addDestination("Atlanta", "01/01/2023", "01/11/2023",  result -> {
//            callbackResult = result;
//            latch2.countDown();
//        });
//
//        latch2.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);
        CountDownLatch latch3 = new CountDownLatch(1);
        mainViewModel.addDestination("Atlanta", "01/01/2023", "01/11/2023",  result -> {
            callbackResult = result;
            latch3.countDown();
        });

        latch3.await(500, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testGetDestinationsWithExistingDestinations() throws InterruptedException{

        callbackResult = false;

        CountDownLatch latch2 = new CountDownLatch(1);
        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch2.countDown();
        });

        latch2.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);


        CountDownLatch getDestinationlatch = new CountDownLatch(1);
        mainViewModel.getDestinations(result -> {
            destinationsResult = result;
            getDestinationlatch.countDown();
        });

        getDestinationlatch.await(5, TimeUnit.SECONDS);

        assertTrue(destinationsResult.containsKey("Paris"));
    }

    @Test
    public void testGetDestinationsWithNoDestinations() throws InterruptedException {
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainViewModel.getDestinations(result -> {
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
        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);
        CountDownLatch setVacationLatch = new CountDownLatch(1);
        mainViewModel.setVacation("01/01/2023", "01/10/2023", "10", result -> {
            callbackResult = result;
            setVacationLatch.countDown();
        });
        setVacationLatch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);
        CountDownLatch getVacationLatch = new CountDownLatch(1);
        mainViewModel.getVacation(result -> {
            vacationResult = result;
            getVacationLatch.countDown();
        });

        getVacationLatch.await(5, TimeUnit.SECONDS);

        assertEquals("01/01/2023", vacationResult.get("startDate"));
        assertEquals("01/10/2023", vacationResult.get("endDate"));
        assertEquals("10", vacationResult.get("duration"));
    }

    @Test
    public void testSetVacationWithMismatchedDuration() throws InterruptedException{
        callbackResult = false;

        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainViewModel.setVacation("01/10/2023", "01/01/2023", "5", result -> {
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
        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainViewModel.setVacation("01/01/2023", "01/21/2023", "21", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainViewModel.addDestination("Paris", "01/01/2023", "01/11/2023",  result -> {
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

        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainViewModel.setVacation("01/01/2023", "01/30/2023", "30", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);

        mainViewModel.addDestination("Paris", "01/15/2023", "01/20/2023", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);


    }

    @Test
    public void testLogTravelWithEmptyLocation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        mainViewModel.addDestination("", "01/01/2023", "01/10/2023", result -> {
            callbackResult = result;
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testLogTravelEntryStoredInDatabase() throws InterruptedException {

        CountDownLatch signInLatch = new CountDownLatch(1);
        mainViewModel.userSignIn("testUser", "testPassword", result -> {
            callbackResult = result;
            signInLatch.countDown();
        });
        signInLatch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);


        CountDownLatch addDestinationLatch = new CountDownLatch(1);
        mainViewModel.addDestination("TestLocation", "01/01/2023", "01/10/2023", result -> {
            callbackResult = result;
            addDestinationLatch.countDown();
        });
        addDestinationLatch.await(5, TimeUnit.SECONDS);
        assertTrue(callbackResult);


        CountDownLatch getDestinationLatch = new CountDownLatch(1);
        mainViewModel.getDestinations(result -> {
            destinationsResult = result;
            getDestinationLatch.countDown();
        });
        getDestinationLatch.await(5, TimeUnit.SECONDS);

        assertNotNull(destinationsResult);
        assertTrue(destinationsResult.containsKey("TestLocation"));
    }

    @Test
    public void testAccountCreationWithWhitespaceOnlyInput() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignUp("   ", "validPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testAccountCreationWithNullInput() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignUp(null, "validPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testAccountCreationWithEmptyStringInput() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignUp("", "validPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testAccountLoginWithWhitespaceOnlyInput() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("   ", "validPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testAccountLoginWithNullInput() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn(null, "validPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }

    @Test
    public void testAccountLoginWithEmptyStringInput() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("", "validPassword", result -> {
            callbackResult = result;
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
        assertFalse(callbackResult);
    }




}