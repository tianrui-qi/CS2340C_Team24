package com.example.sprint1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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


@RunWith(AndroidJUnit4.class)
public class TestSprint4 {
    private static final MainViewModel mainViewModel = new MainViewModel();

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.sprint1", appContext.getPackageName());
    }
    @Test
    public void testUserSignUpWithExistingUsername() {
        mainViewModel.userSignUp("sprint4Test", "test", Assert::assertTrue);
    }
    @Test
    public void testUserSignInWithCorrectCredentials() {
        mainViewModel.userSignIn("sprint4Test", "test", Assert::assertTrue);
    }



    // Tianqin Yu
    @Test
    public void testAddTravelWithEmptyStart() {
        mainViewModel.addTravel(
                "", "11/30/2024",
                "Paris", "Hotel Le Meurice",
                "Restaurant Guy Savoy", "Exploration and relaxation",
                Assert :: assertFalse);
    }
    @Test
    public void testAddTravelWithEmptyEnd() {
        mainViewModel.addTravel(
                "11/25/2024", "",
                "Paris", "Hotel Le Meurice",
                "Restaurant Guy Savoy", "Exploration and relaxation",
                Assert :: assertFalse
        );
    }

    @Test
    public void testAddTravelWithInvalidDateRange() {
        mainViewModel.addTravel(
                "11/30/2024", "11/25/2024",
                "Paris", "Hotel Le Meurice",
                "Restaurant Guy Savoy", "Exploration and relaxation",
                Assert :: assertFalse
        );
    }

    @Test
    public void testAddTravelWithEmptyDestination() {
        mainViewModel.addTravel(
                "11/25/2024", "11/30/2024",
                "", "Hotel Le Meurice",
                "Restaurant Guy Savoy", "Exploration and relaxation",
                Assert :: assertFalse
        );
    }

    @Test
    public void testAddTravelWithEmptyAccommodation() {
        mainViewModel.addTravel(
                "11/25/2024", "11/30/2024",
                "Paris", "",
                "Restaurant Guy Savoy", "Exploration and relaxation",
                Assert :: assertFalse
        );
    }

    @Test
    public void testAddTravelWithEmptyDining() {
        mainViewModel.addTravel(
                "11/25/2024", "11/30/2024",
                "Paris", "Hotel Le Meurice",
                "", "Exploration and relaxation",
                Assert :: assertFalse
        );
    }

    // Tianrui Qi
    @Test
    public void testAddTravelWithValidData() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("sprint4Test", "test", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.addTravel(
                    "11/25/2024", "11/30/2024",
                    "Paris", "Hotel Le Meurice",
                    "Restaurant Guy Savoy", "Exploration and relaxation", addResult -> {
                        assertTrue(addResult);
                    latch.countDown();
            });
        });

    }

    @Test
    public void testGetTravelEntrySaved() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("sprint4Test", "test", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.getTravel(result -> {
                assertNotNull(result);
                boolean found = result.values().stream().anyMatch(entry ->
                        "Paris".equals(entry.get("destination")) &&
                                "Hotel Le Meurice".equals(entry.get("accommodation")) &&
                                "Restaurant Guy Savoy".equals(entry.get("dining")) &&
                                "Exploration and relaxation".equals(entry.get("note")) &&
                                "11/25/2024".equals(entry.get("start")) &&
                                "11/30/2024".equals(entry.get("end"))
                );
                assertTrue(found);
                latch.countDown();
            });
        });
    }


    @Test
    public void testGetTravelWhenNoDataExists() {
        mainViewModel.getTravel(travelMap -> {
            assertNotNull(travelMap);
            assertTrue(travelMap.isEmpty());
        });
    }

    @Test
    public void testAddDiningDuplicateEntry() {
        CountDownLatch latch = new CountDownLatch(1);
        mainViewModel.userSignIn("sprint4Test", "test", signInResult -> {
            assertTrue(signInResult);
            mainViewModel.addTravel(
                    "11/25/2024", "11/30/2024",
                    "Paris", "Hotel Le Meurice",
                    "Restaurant Guy Savoy", "Exploration and relaxation", addResult -> {
                assertFalse(addResult);
                latch.countDown();
            });
        });
    }


    @Test
    public void testAddTravelWithNullInputs() {
        mainViewModel.addTravel(
                null, "11/30/2024",
                null, null,
                "Restaurant Guy Savoy", null,
                Assert :: assertFalse
        );
    }

}