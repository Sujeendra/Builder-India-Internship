# Builder-India-Internship

How this works---------------------------->Video Link
https://drive.google.com/file/d/1zVvgpupISpbF6vLUN3tlUkE8H7AJRv62/view?usp=sharing
<------------------------Working----------------------------->
(1) TO AUTOMATICALLY CALL THE MOBILE NUMBERS THRU MOBILE SIMCARD ONE BY ONE AND 
(2) WHEN THE CALL IS ANSWERED THEN IT SHOULD PLAY A VOICE MESSAGE WHICH THE RECIPIENT OF CALL WILL BE ABLE TO LISTEN, FOR EXAMPLE AUTOMATIC VOICE CALL MESSAGE FOR OTP VERIFICATION WE GET ON OR PHONE FROM GMAIL OR VARIOUS OTHER APPS.
(3) AFTER CALL IS ENDED THEN CONDITIONING WITH SAME PROCESS BY CALLING NEXT NUMBER IN EXCEL SHEET.
Note:   Problem:  Both the users sholud have this app.
        Reason:   Because of Incompatibility between different dialer framework.
        If the call is connected between different dialer framework then everything works except the verification code is not           properly audible.

Links Referred
  [1]: https://developer.android.com/reference/android/content/Intent.html#ACTION_DIAL
  [2]: https://android.googlesource.com/platform/frameworks/base/+/master/telecomm/java/android/telecom/DefaultDialerManager.java#144
  [3]: https://android.googlesource.com/platform/packages/apps/Dialer/+/nougat-release/AndroidManifest.xml#79
  [4]: docs/dialog.png
  [11]: docs/settings.png
  
  [5]: https://developer.android.com/reference/android/telecom/InCallService.html
  [6]: https://developer.android.com/reference/android/telecom/InCallService.html#onCallAdded(android.telecom.Call)
  [7]: https://developer.android.com/reference/android/telecom/InCallService.html#onCallRemoved(android.telecom.Call)
  [8]: https://developer.android.com/reference/android/telecom/Call.html#answer(int)
  [9]: https://developer.android.com/reference/android/telecom/VideoProfile.html#STATE_AUDIO_ONLY
  [10]: https://developer.android.com/reference/android/telecom/Call.Callback.html
  [12]: docs/call.gif
 
