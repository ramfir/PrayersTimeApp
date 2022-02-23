# PrayersTimeApp

This is an android app which shows and notifies user about islamic prayers time due to site of ДУМ РФ (https://www.dumrf.ru/)

# Preview

Start page            |  Notification when prayer time comes | Clicking on notification
:-------------------------:|:-------------------------:|:-------------------------:
<img width="281" alt="1" src="https://user-images.githubusercontent.com/52213479/155285359-e5216c29-d6c3-41ed-952e-8277788b51c1.png">  |  <img width="280" alt="2" src="https://user-images.githubusercontent.com/52213479/155285381-1a01d707-d815-4692-b9e9-ce89aebb7ef8.png"> | <img width="281" alt="4" src="https://user-images.githubusercontent.com/52213479/155285405-e3d9ef22-c379-4db9-bdde-779e67cf70d0.png"> 

# App logic overview

The app is developed due to MVP architectural pattern.

PrayersListFragment implements View interface which is responsible for:
- Showing prayers time in RecyclerView
- Highlighting current prayer
- Changing ImageView due to current prayer
- Showing information about prayers time
- Showing Toast when errors occurs
- Showing and hiding ProgressBar

PrayersListPresenter implements Presenter interface which is responsible for:
- Subscription to Observable from Model in io thread
- Requesting View to:
   - Show ProgressBar before starting downloading prayers time
   - Hiding ProgressBar after completion or error
   - Showing Toast when errors occurs
   - Showing prayers time
   - Highlighting current prayer
   - Changing ImageView due to current prayer
- Setting alarms via AlarmManager

PrayersModel implements Model interface which is resposible for:
- Creating Observable
- Requesting https://www.dumrf.ru/ and parsing prayers time
- Emitting prayers time
- Saving prayers time to SharedPreferences

Creating Observable, emitting data, subscription and creating different operators done via RxJava.

Dependencies for components were provided via Dagger Hilt.

# Stack

- Kotlin
- MVP
- Dagger Hilt
- RxJava
- Jsoup
- Navigation Architecture Component
