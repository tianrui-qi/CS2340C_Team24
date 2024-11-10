package com.example.sprint1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.sprint1.viewmodel.MainViewModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final MainViewModel mainViewModel = new MainViewModel();

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.sprint1", appContext.getPackageName());
    }

    @Test
    public void testUserSignUpWithExistingUsername() {
        mainViewModel.userSignUp("testUser", "testPassword", Assert::assertFalse);
    }

    @Test
    public void testUserSignInWithCorrectCredentials() {
        mainViewModel.userSignIn("tianrui", "1", Assert::assertTrue);
    }

    @Test
    public void testUserSignInWithIncorrectPassword() {
        mainViewModel.userSignIn("tianrui", "2", Assert::assertFalse);
    }

    @Test
    public void testAddDestinationWithValidData() {
        mainViewModel.userSignIn("testUser", "testPassword", Assert::assertTrue);
        mainViewModel.addDestination("Paris", "01/01/2023", "01/11/2023", Assert::assertTrue);
    }

    @Test
    public void testAddDestinationWithAdditionalValidData() {
        mainViewModel.userSignIn("testUser", "testPassword", Assert::assertTrue);
        mainViewModel.addDestination("New York", "01/04/2023", "01/06/2023", Assert::assertTrue);
    }

    @Test
    public void testAddDestinationWithDuplicateLocation() {
        mainViewModel.userSignIn("testUser", "testPassword", Assert::assertTrue);
        mainViewModel.addDestination("Atlanta", "01/01/2023", "01/11/2023", Assert::assertTrue);
    }

    @Test
    public void testGetDestinationsWithExistingDestinations() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("testUser", "testPassword", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.getDestination(result -> {
                assertNotNull(result);
                boolean found = result.values().stream().anyMatch(destination ->
                        "Paris".equals(destination.get("travelLocation"))
                );
                assertTrue(found);
                latch.countDown();
            });
        });
    }

    @Test
    public void testGetDestinationsWithNoDestinations() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignUp("testUserNoDest", "testPassword", signUpResult -> {
            assertTrue(signUpResult);
            mainViewModel.userSignIn("testUserNoDest", "testPassword", signInResult -> {
                assertTrue( signInResult);
                mainViewModel.getDestination(result -> {
                    assertNotNull(result);
                    assertTrue(result.isEmpty());
                    latch.countDown();
                });
            });
        });
    }

    @Test
    public void testSetVacationWithValidDatesAndDuration() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("testUser", "testPassword", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.setVacation("01/01/2023", "01/10/2023", "10", setResult -> {
                assertTrue(setResult);
                mainViewModel.getVacation(result -> {
                    assertNotNull(result);
                    assertEquals("01/01/2023", result.get("startDate"));
                    assertEquals("01/10/2023", result.get("endDate"));
                    assertEquals("10", result.get("duration"));
                    latch.countDown();
                });
            });
        });
    }

    @Test
    public void testSetVacationWithMismatchedDuration() {
        mainViewModel.userSignIn("testUser", "testPassword", Assert::assertTrue);
        mainViewModel.setVacation("01/10/2023", "01/01/2023", "5", Assert::assertFalse);
    }

    @Test
    public void testCalculateOccupiedDaysWithValidVacationAndDestinations() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("testUser", "testPassword", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.setVacation("01/01/2023", "01/21/2023", "21", setResult -> {
                assertTrue(setResult);
                mainViewModel.addDestination("Paris", "01/01/2023", "01/11/2023", addResult -> {
                    assertTrue(addResult);
                    mainViewModel.calVacation(occupiedDays -> {
                        assertEquals("11", occupiedDays);
                        latch.countDown();
                    });
                });
            });
        });
    }

    @Test
    public void testCalculateOccupiedDaysWithNoOverlap() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("testUser", "testPassword", signInResult -> {
            assertTrue("User sign-in failed", signInResult);
            mainViewModel.setVacation("01/01/2023", "01/30/2023", "30", setResult -> {
                assertTrue("Setting vacation failed", setResult);
                mainViewModel.addDestination("Paris", "02/15/2023", "02/20/2023", addResult -> {
                    assertTrue("Adding destination failed", addResult);
                    mainViewModel.calVacation(occupiedDays -> {
                        assertEquals("0", occupiedDays);
                        latch.countDown();
                    });
                });
            });
        });
    }

    @Test
    public void testLogTravelWithEmptyLocation() {
        mainViewModel.addDestination("", "01/01/2023", "01/10/2023", Assert::assertFalse);
    }

    @Test
    public void testLogTravelEntryStoredInDatabase() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("testUser", "testPassword", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.addDestination("TestLocation", "01/01/2023", "01/05/2023", addResult -> {
                assertTrue(addResult);
                mainViewModel.getDestination(result -> {
                    assertNotNull(result);
                    boolean found = result.values().stream().anyMatch(destination ->
                            "TestLocation".equals(destination.get("travelLocation"))
                    );
                    assertTrue(found);
                    latch.countDown();
                });
            });
        });
    }

    @Test
    public void testAccountCreationWithWhitespaceOnlyInput() {
        mainViewModel.userSignUp("   ", "validPassword", Assert::assertFalse);
    }

    @Test
    public void testAccountCreationWithNullInput() {
        mainViewModel.userSignUp(null, "validPassword", Assert::assertFalse);
    }

    @Test
    public void testAccountCreationWithEmptyStringInput() {
        mainViewModel.userSignUp("", "validPassword", Assert::assertFalse);
    }

    @Test
    public void testAccountLoginWithWhitespaceOnlyInput() {
        mainViewModel.userSignIn("   ", "validPassword", Assert::assertFalse);
    }

    @Test
    public void testAccountLoginWithNullInput() {
        mainViewModel.userSignIn(null, "validPassword", Assert::assertFalse);
    }

    @Test
    public void testAccountLoginWithEmptyStringInput() {
        mainViewModel.userSignIn("", "validPassword", Assert::assertFalse);
    }
}