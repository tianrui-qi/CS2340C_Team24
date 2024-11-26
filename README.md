WanderSync is simplifies the process of creating and managing travel itineraries for solo and group travel. 
Through this app, users can contribute to and refine travel plans in real-time. 
The Travel Management app allows users to organize locations, transportation options, accommodations, dining reservations, and personal notes. 
Key features include secure account creation, a user-friendly interface for itinerary management, and real-time collaboration tools for group planning. 
Users can seamlessly integrate travel details such as destinations, transportation schedules, lodging information, and dining preferences. 
The app also offers features for adding and sharing notes, creating shared travel agendas, and synchronizing updates among all group members.  

We will introduce the main features of WanderSync and the design of each 
feature. 
To implement features, we will use Firebase Realtime Database to store
all the data. More specifically, we will have five databases:
[UserDatabase](app/src/main/java/com/example/sprint1/model/UserDatabase.java), 
[DestDatabase](app/src/main/java/com/example/sprint1/model/DestDatabase.java),
[DiniDatabase](app/src/main/java/com/example/sprint1/model/DiniDatabase.java),
[AccoDatabase](app/src/main/java/com/example/sprint1/model/AccoDatabase.java),
and
[TravDatabase](app/src/main/java/com/example/sprint1/model/TravDatabase.java).

## Sign In and Sign Up

After launching the app with a welcome page, users can sign in or sign up. 
For sign up, after a user enters a username and password, the app will create a new user in the [UserDatabase](app/src/main/java/com/example/sprint1/model/UserDatabase.java). 
The key is just the username, and the values are username and password. Note that we will have more values in the future. 
For sign in, the app will check if [UserDatabase](app/src/main/java/com/example/sprint1/model/UserDatabase.java) has the entered username as a key and if the entered password is the same as the password stored under the username. 

```json
{
  "user": {
    "tianrui": {
      "password": "123",
      "username": "tianrui"
    }
  }
}
```

![](assets/figure-1.jpg)

## Add Dining

In the Dining page, users can add a new dining place by clicking the `Add Dining` button and entering the location, website, and date.
After users click the `Submit` button, in [DiniDatabase](app/src/main/java/com/example/sprint1/model/DiniDatabase.java), the app will randomly generate a key for this dining place and store each field as a value.
Then, the app will add this dining key generated to the user's dining list in [UserDatabase](app/src/main/java/com/example/sprint1/model/UserDatabase.java).
After the interaction with the database finishes, the app will refresh the dining page and list all the dining places stored under the user's dining list.
The app marks the date that is later than today as green where others as red and sorts the list by date.

```json
{
  "dining": {
    "-OCd0Lh6Clt2BCKIf2mM": {
      "location": "McDonald's",
      "time": "11/26/2024",
      "website": "https://www.mcdonalds.com"
    },
    "-OCd0nH7yeCiFlE2dUoK": {
      "location": "Taco Bell",
      "time": "11/27/2024",
      "website": "https://www.tacobell.com"
    }
  },
  "user": {
    "tianrui": {
      "dining": [
        "-OCd0Lh6Clt2BCKIf2mM",
        "-OCd0nH7yeCiFlE2dUoK"
      ]
    }
  }
}
```

![](assets/figure-2.jpg)

## Add Accommodation

In the Accommodation page, user can add a new accommodation by clicking the `Add Accommodation` button and entering location, room number, room type, check in date, and check out date.
After clicking the `Submit` button, the app will generate a key for this accommodation and store each field as a value in [AccoDatabase](app/src/main/java/com/example/sprint1/model/AccoDatabase.java).
Then, the app will add this accommodation key generated to the user's accommodation list in [UserDatabase](app/src/main/java/com/example/sprint1/model/UserDatabase.java).
After the interaction with the database finishes, the app will refresh the accommodation page and list all the accommodations stored under the user's accommodation list.
The app marks the date that is later than today as green where others as red and sorts the list by date.

```json
{
  "accommodation": {
    "-OCd55NK_O4EU_VdJR6_": {
      "checkIn": "11/24/2024",
      "checkOut": "11/27/2024",
      "location": "QQQQ",
      "roomNum": "1",
      "roomType": "Two"
    },
    "-OCd5BdyNzdt_ggxQmqo": {
      "checkIn": "11/28/2024",
      "checkOut": "11/30/2024",
      "location": "WWWW",
      "roomNum": "2",
      "roomType": "One"
    }
  },
  "user": {
    "tianrui": {
      "accommodation": [
        "-OCd55NK_O4EU_VdJR6_",
        "-OCd5BdyNzdt_ggxQmqo"
      ]
    }
  }
}
```

![](assets/figure-3.jpg)