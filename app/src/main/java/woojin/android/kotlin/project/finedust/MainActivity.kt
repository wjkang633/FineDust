package woojin.android.kotlin.project.finedust

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import woojin.android.kotlin.project.finedust.data.Repository
import woojin.android.kotlin.project.finedust.data.models.airquality.AirQuality
import woojin.android.kotlin.project.finedust.data.models.airquality.Grade
import woojin.android.kotlin.project.finedust.data.models.monitoringstation.MonitoringStation
import woojin.android.kotlin.project.finedust.databinding.ActivityMainBinding
import java.lang.Exception
import java.security.spec.ECField

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var cancellationTokenSource: CancellationTokenSource? = null

    private val scope = MainScope()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindViews()
        initVariables()
        //권한 요청
        requestLocationPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
        scope.cancel()
    }

    private fun bindViews(){
        binding.refreshLayout.setOnRefreshListener {
            fetchAirQualityData()
        }
    }

    private fun initVariables() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_ACCESS_LOCATION_PERMISSIONS
        )
    }

    private fun requestBackgroundLocationPermissions() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS
        )
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val locationPermissionGranted =
            requestCode == REQUEST_ACCESS_LOCATION_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            val backgroundLocationPermissionGranted =
                    requestCode == REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS && grantResults[0] == PackageManager.PERMISSION_GRANTED

            if (!backgroundLocationPermissionGranted){
                //요청
                requestBackgroundLocationPermissions()
            }
            else
            {
                fetchAirQualityData()
            }
        }
        else
        {
            if (!locationPermissionGranted) {
                finish()
            } else {
                //fetchData
                fetchAirQualityData()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchAirQualityData() {
        cancellationTokenSource = CancellationTokenSource()

        fusedLocationProviderClient
            .getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource!!.token
            )
            .addOnSuccessListener { location ->
                //코루틴 시작
                scope.launch {
                    binding.errorDescriptionTextView.visibility = View.GONE
                    try {
                        val monitoringStation =
                            Repository.getNearbyMonitoringStation(
                                location.latitude,
                                location.longitude
                            )

                        val airQuality =
                            Repository.getLatestAirQualityData(monitoringStation!!.stationName!!)

                        displayAirQualityData(monitoringStation, airQuality!!)
                    } catch (exception: Exception) {
                        binding.errorDescriptionTextView.visibility = View.VISIBLE
                        binding.contentsLayout.alpha = 0F
                    } finally {
                        binding.progressBar.visibility = View.GONE
                        binding.refreshLayout.isRefreshing = false
                    }
                }
            }
    }

    fun displayAirQualityData(monitoringStation: MonitoringStation, airQuality: AirQuality) {
        binding.contentsLayout
            .animate()
            .alpha(1F)
            .start()

        binding.measuringStationNameTextView.text = monitoringStation.stationName
        binding.measuringStationAddressTextView.text = monitoringStation.addr

        //종합 지수
        (airQuality.khaiGrade ?: Grade.UNKNOWN)?.let { grade ->
            binding.root.setBackgroundResource(grade.colorResId)
            binding.totalGradeLabelTextView.text = grade.label
            binding.totalGradeEmojiTextView.text = grade.emoji
        }


        //미세, 초미세 등
        with(airQuality) {
            binding.fineDustInfoTextView.text =
                "미세먼지: $pm10Value ㎍/㎥ ${(pm10Grade ?: Grade.UNKNOWN).emoji}."
            binding.ultraFineDustInfoTextView.text =
                "초미세먼지: $pm25Value ㎍/㎥ ${(pm25Grade ?: Grade.UNKNOWN).emoji}."

            with(binding.so2Item) {
                labelTextView.text = "아황산가스"
                gradeTextView.text = (so2Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$so2Value ppm"
            }
            with(binding.coItem) {
                labelTextView.text = "일산화탄소"
                gradeTextView.text = (coGrade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$coValue ppm"
            }
            with(binding.o3Item) {
                labelTextView.text = "오존"
                gradeTextView.text = (o3Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$o3Value ppm"
            }
            with(binding.no2Item) {
                labelTextView.text = "이산화질소"
                gradeTextView.text = (no2Grade ?: Grade.UNKNOWN).toString()
                valueTextView.text = "$no2Value ppm"
            }
        }
    }

    companion object {
        const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
        const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101
    }
}