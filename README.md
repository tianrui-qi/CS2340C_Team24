WanderSync is simplifies the process of creating and managing travel itineraries 
for solo and group travel. Through this project, users can contribute to and 
refine travel plans in real-time. The Travel Management app allows users to 
organize locations, transportation options, accommodations, dining reservations, 
and personal notes.
Key features include secure account creation, a user-friendly interface for 
itinerary management, and real-time collaboration tools for group planning. 
Users can seamlessly integrate travel details such as destinations, 
transportation schedules, lodging information, and dining preferences. 
The app also offers features for adding and sharing notes, creating shared 
travel agendas, and synchronizing updates among all group members.  

We will introduce the main features of WanderSync and the design of each feature 
with five database we established:
- [UserDatabase](app/src/main/java/com/example/sprint1/model/UserDatabase.java)
- [DestDatabase](app/src/main/java/com/example/sprint1/model/DestDatabase.java)
- [DiniDatabase](app/src/main/java/com/example/sprint1/model/DiniDatabase.java)
- [AccoDatabase](app/src/main/java/com/example/sprint1/model/AccoDatabase.java)
- [TravDatabase](app/src/main/java/com/example/sprint1/model/TravDatabase.java)

## Sign In and Sign Up

After launching the app with a welcome page, users can sign in or sign up. 
For sign up, after user each username and password, the app will create a new
user in the UserDatabase. The key is just the username and the values are 
username and password. Note taht we will have more values in the future. 
For sign in, the app will check if userdatabase has the entered username as key 
and if the entered password is same as the password stored under the username. 

```
{
  "user": {
    "tianrui": {
      "password": "123",
      "username": "tianrui"
    }
  }
}
```

![1-1](assets/figure-1.jpg)