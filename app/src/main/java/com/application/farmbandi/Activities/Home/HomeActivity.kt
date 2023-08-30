package com.application.farmbandi.Activities.Home

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.application.farmbandi.Activities.Profile.ProfileActivity
import com.application.farmbandi.AppInterfaces.CallBackListener
import com.application.farmbandi.AppInterfaces.CurrentLocationListener
import com.application.farmbandi.AppInterfaces.DialogClickListener
import com.application.farmbandi.FragmentAdapters.JobsFragmentAdapter
import com.application.farmbandi.PopUpDialogs.InternetConnectivity
import com.application.farmbandi.R
import com.application.farmbandi.Services.LocationService
import com.application.farmbandi.SharedMemory.MyLocalMemory
import com.application.farmbandi.Utils.ConnectionLiveData
import com.application.farmbandi.Utils.MyApp
import com.application.farmbandi.Utils.UtilsFunction
import com.application.farmbandi.Utils.UtilsFunction.Companion.checkLocationPermission
import com.application.farmbandi.databinding.ActivityHomeBinding
import com.application.farmbandi.model.DeliveryBoy
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener


class HomeActivity : AppCompatActivity(), View.OnClickListener, DialogClickListener,
    CurrentLocationListener {
    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private val repository by lazy {
        HomeRepository(this)
    }


    private val networkErrorDialog by lazy {
        InternetConnectivity(this, isVisibleRetryBtn = false)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]
    }
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ -> dexter.check() }


    private val locationBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            repository.availabilityStatus(pressedTab.position)
        }
    }


    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                UtilsFunction.getLastLocation(this, this)
            } else {
                UtilsFunction.infoToast(
                    this,
                    getString(R.string.you_will_have_to_give_permission_for_fetching_current_location)
                )
            }
        }


    lateinit var dexter: DexterBuilder
    lateinit var pressedTab: TabLayout.Tab


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        intializer()
    }

    private fun intializer()
    {
        val connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this, Observer {
            if(it)
            {
               networkErrorDialog.dismiss()
            }else
            {
               networkErrorDialog.show()
            }
        })
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(locationBroadcastReceiver, IntentFilter("update_location"))

        binding.driverData =
            MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS, DeliveryBoy())
        binding.imgMenu.setOnClickListener(this)
        binding.vpJobs.apply {
            adapter = JobsFragmentAdapter(this@HomeActivity)
            offscreenPageLimit = 1
        }

        TabLayoutMediator(binding.tabJob, binding.vpJobs) { tab, position ->
            val tabNames = listOf(
                getString(R.string.upcoming_jobs),
                getString(R.string.ongoing_jobs),
                getString(R.string.completed)
            )
            tab.text = tabNames[position]
        }.attach()


        binding.tabOnlineOffline.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(enableLocationServices())
                {
                    pressedTab = tab!!
                    if (!checkLocationPermission(this@HomeActivity)) {
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                    } else if (pressedTab.position != MyLocalMemory.getObject(
                            MyApp.MySingleton.DRIVER_DETAILS,
                            DeliveryBoy()
                        ).isOnline
                    ) {
                        repository.availabilityStatus(pressedTab.position)
                    } else {
                        goOnlineAndOffline(
                            MyLocalMemory.getObject(
                                MyApp.MySingleton.DRIVER_DETAILS,
                                DeliveryBoy()
                            ).isOnline, false
                        )
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        val tab: TabLayout.Tab = binding.tabOnlineOffline.getTabAt(
            MyLocalMemory.getObject(
                MyApp.MySingleton.DRIVER_DETAILS,
                DeliveryBoy()
            ).isOnline
        )!!
        tab.select()
        setObserver()
    }

    private fun setObserver() {
        viewModel.availabilityStatusResponse.observe(this) { it ->
            if (it.status) {
                if (!checkLocationPermission(context = this)) {
                    requestPermission(this)
                    dexter.check()

                } else {
                    goOnlineAndOffline(it.availability_status)
                }
            } else {
                goOnlineAndOffline(
                    MyLocalMemory.getObject(
                        MyApp.MySingleton.DRIVER_DETAILS,
                        DeliveryBoy()
                    ).isOnline, false
                )
                UtilsFunction.errorToast(this, it.message)
            }
        }

    }

    private fun goOnlineAndOffline(status: Int, showToast: Boolean = true)
    {
        val deliveryBoy = MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS, DeliveryBoy())
        if (status == 1) {
            pressedTab.view.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_green_selected_tab, null)
            if (!LocationService.isServiceRunning) {
                ContextCompat.startForegroundService(
                    this@HomeActivity,
                    Intent(this@HomeActivity, LocationService::class.java)
                )
            }
            if (showToast) {
                UtilsFunction.successToast(this@HomeActivity, "Your are online")
            }
            deliveryBoy.isOnline = status
            MyLocalMemory.putObject(MyApp.MySingleton.DRIVER_DETAILS, deliveryBoy)

        } else {
            stopService(Intent(this@HomeActivity, LocationService::class.java))
            MyApp.MySingleton.getLocTrackingFirebaseDb().removeValue().addOnSuccessListener {
                binding.tabOnlineOffline.getTabAt(1)?.view?.background = ResourcesCompat.getDrawable(resources, R.color.transparent, null)
                pressedTab?.view?.background = ResourcesCompat.getDrawable(resources, R.drawable.tab_color_selector, null)
                if (showToast)
                {
                    UtilsFunction.infoToast(this@HomeActivity, "You are offline")
                }
                deliveryBoy.isOnline = status
                MyLocalMemory.putObject(MyApp.MySingleton.DRIVER_DETAILS, deliveryBoy)
            }.addOnFailureListener {
                UtilsFunction.errorToast(
                    this@HomeActivity,
                    it.message.toString()
                )
            }
        }

    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imgMenu -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requestNotificationPermission()
        if (checkLocationPermission(this) && !LocationService.isServiceRunning && MyLocalMemory.getObject(MyApp.MySingleton.DRIVER_DETAILS, DeliveryBoy()).isOnline == 1)
        {
            if(enableLocationServices())
            {
                ContextCompat.startForegroundService(this@HomeActivity, Intent(this@HomeActivity, LocationService::class.java))
            }
        }
    }


    private fun requestNotificationPermission()
    {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    }
                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: com.karumi.dexter.listener.PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }
                }).check()

        }
    }


    private fun requestPermission(context: Context) {
        dexter = Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    report.let {
                        if (report.areAllPermissionsGranted()) {
                            goOnlineAndOffline(pressedTab.position)
                            Toast.makeText(context, "Permissions Granted", Toast.LENGTH_SHORT)
                                .show()

                        } else {

                            AlertDialog.Builder(context).apply {
                                setMessage("Please allow the required permissions")
                                    .setCancelable(false)
                                    .setPositiveButton("Settings") { dialog, _ ->
                                        val reqIntent =
                                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                                .apply {
                                                    val uri =
                                                        Uri.fromParts("package", packageName, null)
                                                    data = uri
                                                }
                                        dialog.cancel()
                                        resultLauncher.launch(reqIntent)
                                    }
                                // setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                            }.create().show()


                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onClick(clickCode: Int, data: Any?, callBack: CallBackListener?) {

    }

    override fun onGetCurrentLocation(string: String) {

    }

    override fun onError(string: String) {
        TODO("Not yet implemented")
    }

    private fun enableLocationServices():Boolean
    {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
       return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}