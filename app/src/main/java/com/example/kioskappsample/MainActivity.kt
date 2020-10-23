package com.example.kioskappsample

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi

/*we can enable our app to be in fullscreen mode:*/
private val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_FULLSCREEN
        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

class MainActivity : AppCompatActivity() {

    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*/*we can enable our app to be in fullscreen mode:*/*/
        window.decorView.systemUiVisibility = flags

        mAdminComponentName = MyDeviceAdminReceiver.getComponentName(this)
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        if (mDevicePolicyManager.isDeviceOwnerApp(packageName)) {
            // You are the owner!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setKioskPolicies()
            }
        } else {
            // Please contact your system administrator
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setKioskPolicies() {
        /*enable our package to enter lock task*/
        mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, arrayOf(packageName))
        startLockTask()

        /*set our app as the default application:*/
        val intentFilter = IntentFilter(Intent.ACTION_MAIN)
        intentFilter.addCategory(Intent.CATEGORY_HOME)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        mDevicePolicyManager.addPersistentPreferredActivity(mAdminComponentName,
            intentFilter, ComponentName(packageName, MainActivity::class.java.name))

        /* when the device boots, our application will start immediately without the lock screen appearing*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDevicePolicyManager.setKeyguardDisabled(mAdminComponentName, true)
        }

//        /*keep our application awake*/
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}