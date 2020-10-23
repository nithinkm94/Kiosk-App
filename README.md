# Kiosk-App
HOW TO TURN YOUR ANDROID APPLICATION INTO A KIOSK

LOCKTASK MODE
Our Kiosk application will mostly be based on a class inheriting from DeviceAdminReceiver and ComponentName. First, we have to tell the system that our app is pretending to become device administrator. We can do it by adding a receiver in AndroidManifest.xml containing metadata with name android.app.device_admin:

AndroidManifest.xml - 
Here we can provide the same basic information about our admin like name and description, which will be displayed in device’s settings. Also take note of testOnly=”true”, which will allow us to remove the admin without complications, which will be discussed in next section.

An xml file added with some information about policies which the admin app will use. To do this we can create a new directory in res.

Then create a DeviceAdminReceiver.

From this point, we should install our application, but enabling admin is only possible on devices which don’t have any users added. So before we install app we have to wipe device/factory reset if any Google accounts have been added:

1.Wipe/Factory reset device

2.Do not add Google account on the first start, just skip it

3.Install our application with Android Studio or command line:  adb install path/to/kiosk.apk

4.Set device admin:
 
        adb shell dpm set-device-owner com.example.kiosk/.MyDeviceAdminReceiver

5.If everything has gone well we should be able to see our application in the list of device’s administrators in Settings → Security → Device admin apps.

6 .We can also check if our application is device admin programmatically:



To remove app 

                adb shell dpm remove-active-admin  com.example.kiosk/MyDeviceAdminReceiver

To install App

                adb install -r path/to/kiosk.apk


SOURCE
For more details - https://snow.dog/blog/kiosk-mode-android
