
# Snoozeloo

Snoozeloo is an alarm clock app designed to provide users with an intuitive, feature-rich experience. The app allows users to create and manage alarms effortlessly while offering customization options such as ringtones, repeat schedules, and snoozing functionality. This document outlines the core features and functionality of Snoozeloo.

## Features

### Alarm List Screen
- Displays a list of all created alarms.
- Shows an empty state when there are no alarms.
- Includes a Floating Action Button (FAB) to create new alarms.
- Allows toggling alarms on or off directly from the list.
- Displays the next occurrence of the alarm in the format `1d 4h 45min`.
- Shows the days of the week the alarm is set to repeat.
- Provides a bedtime suggestion for 8 hours of sleep (e.g., "Go to bed at XX:YYpm to get 8h of sleep").

### Alarm Detail Screen
- Configure individual alarms without affecting others.
- Input fields for alarm time, restricted to valid times (00:00 to 23:59).
- "Save" button enabled only when the input time is valid.
- Automatically schedules alarms for the next day if the time is earlier than the current time.
- Displays the next alarm occurrence in `1d 4h 45min` format.
- Option to set an alarm name through a dialog with a "Save" button.
- Selectable chips to choose the days the alarm should repeat.
- Cards for:
  - Selecting alarm ringtones (navigates to Ringtone Setting Screen).
  - Setting alarm volume (default: 50%).
  - Toggling vibration on or off.

### Alarm Trigger Screen
- Displays when an alarm is triggered.
- "Turn Off" button to dismiss the screen and stop the alarm.
- "Snooze" button to:
  - Stop the alarm and dismiss the screen.
  - Schedule a new alarm for 5 minutes later with the same configuration.
  - Ensure snoozing does not affect the alarm's regular repeating schedule.

### Ringtone Setting Screen
- Lists all default Android ringtones and includes a silent option.
- Allows users to:
  - Mark a ringtone as selected.
  - Play a short preview of the selected ringtone.
  - Stay on the screen after selecting a ringtone (no navigation back).


## Design
- UI designs and colors are detailed in the [Snoozeloo mockups](https://www.figma.com/design/t0TlMqJem7LCjALeyQNip2/Snoozeloo?node-id=62-6482&t=Goatlz8jjY3dyocJ-1).
- Icons are sourced from Material Design or provided SVG files from the mockups.


