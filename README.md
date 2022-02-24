# PrayersTimeApp

This is an android app which shows and notifies user about islamic prayers time due to site of ДУМ РФ (https://www.dumrf.ru/)

# Preview

Start page            |  Notification when prayer time comes | Clicking on notification
:-------------------------:|:-------------------------:|:-------------------------:
<img width="290" alt="1" src="https://user-images.githubusercontent.com/52213479/155491389-9cadcb0c-63f0-47cc-8bdc-69d646ebc2b1.png">  |  <img width="276" alt="2" src="https://user-images.githubusercontent.com/52213479/155490920-cb5ef453-ed5a-494f-916d-8b20ac8caf7b.png"> | <img width="290" alt="3" src="https://user-images.githubusercontent.com/52213479/155491534-84192c86-65ec-4f9d-b842-e59fdb752503.png">

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
