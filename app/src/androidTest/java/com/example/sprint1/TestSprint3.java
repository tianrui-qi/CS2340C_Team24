package com.example.sprint1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.sprint1.viewmodel.MainViewModel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;


@RunWith(AndroidJUnit4.class)
public class TestSprint3 {
    private static final MainViewModel mainViewModel = new MainViewModel();

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.sprint1", appContext.getPackageName());
    }
    @Test
    public void testUserSignUpWithExistingUsername() {
        mainViewModel.userSignUp("sprint3Test", "test", Assert::assertTrue);
    }
    @Test
    public void testUserSignInWithCorrectCredentials() {
        mainViewModel.userSignIn("sprint3Test", "test", Assert::assertTrue);
    }



    // Tianqin Yu
    @Test
    public void testAddDiningWithEmptyLocation() {
        mainViewModel.addDining("", "test.com", "11/10/2023", Assert::assertFalse);
    }

    // Tianqin Yu
    @Test
    public void testAddDiningWithEmptyWebsite() {
        mainViewModel.addDining("Atlanta", "", "11/10/2023", Assert::assertFalse);
    }

    // Tianrui Qi
    @Test
    public void testAddDiningWithEmptyTime() {
        mainViewModel.addDining("Atlanta", "test.com", "", Assert::assertFalse);
    }

    // Tianrui Qi
    @Test
    public void testAddValidDining(){
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("sprint3Test", "test", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.addDining("Atlanta", "test.com", "11/10/2023", addResult -> {
                assertTrue(addResult);
                latch.countDown();
            });
        });

    }

    // Yuwen Ding
    @Test
    public void testGetDiningEntrySaved() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("sprint3Test", "test", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.getDining(result -> {
                assertNotNull(result);
                boolean found = result.values().stream().anyMatch(dining ->
                        "Atlanta".equals(dining.get("location")) &&
                                "test.com".equals(dining.get("website")) &&
                                "11/10/2023".equals(dining.get("time"))
                );
                assertTrue(found);
                latch.countDown();
            });
        });
    }

    // Yuwen Ding
    @Test
    public void testAddDiningDuplicateEntry() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("sprint3Test", "test", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.addDining("Atlanta", "test.com", "11/10/2023", addResult -> {
                assertFalse(addResult);
                latch.countDown();
            });
        });
    }

    /* Test Cases for Accommodation Reservations */

    // Yuxuan Li
    @Test
    public void testAddAccommodationWithEmptyLocation() {
        mainViewModel.addAccommodation("", "101", "Single", "11/10/2023", "11/15/2023", Assert::assertFalse);
    }

    // Yuxuan Li
    @Test
    public void testAddAccommodationWithEmptyCheckInDate() {
        mainViewModel.addAccommodation("Hotel", "101", "Single", "", "11/15/2023", Assert::assertFalse);
    }

    // Zishuo Wang
    @Test
    public void testAddValidAccommodation() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("sprint3Test", "test", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.addAccommodation("Hotel", "101", "Single", "11/10/2023", "11/15/2023", addResult -> {
                assertTrue(addResult);
                latch.countDown();
            });
        });


    }

    // Zishuo Wang
    @Test
    public void testGetAccommodationEntrySaved() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("sprint3Test", "test", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.getAccommodation(result -> {
                assertNotNull(result);
                boolean found = result.values().stream().anyMatch(accommodation ->
                        "Hotel".equals(accommodation.get("location")) &&
                                "101".equals(accommodation.get("roomNum")) &&
                                "Single".equals(accommodation.get("roomType")) &&
                                "11/10/2023".equals(accommodation.get("checkIn")) &&
                                "11/15/2023".equals(accommodation.get("checkOut"))
                );
                assertTrue(found);
                latch.countDown();
            });
        });
    }

    // Yuang Zhang
    @Test
    public void testAddAccommodationWithInvalidDateRange() {
        mainViewModel.addAccommodation("Hotel", "101", "Single", "11/15/2023", "11/10/2023", Assert::assertFalse);
    }

    // Yuang Zhang
    @Test
    public void testAddAccommodationDuplicateEntry() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("sprint3Test", "test", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.addAccommodation("Hotel", "101", "Single", "11/10/2023", "11/15/2023", addResult -> {
                assertFalse(addResult);
                latch.countDown();
            });
        });
    }

}