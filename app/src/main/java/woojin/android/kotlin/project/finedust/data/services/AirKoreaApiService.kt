package woojin.android.kotlin.project.finedust.data.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import woojin.android.kotlin.project.finedust.BuildConfig
import woojin.android.kotlin.project.finedust.data.models.airquality.AirQuality
import woojin.android.kotlin.project.finedust.data.models.airquality.AirQualityResponse
import woojin.android.kotlin.project.finedust.data.models.monitoringstation.MonitoringStationsResponse

interface AirKoreaApiService {
    @GET("/B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList" +
            "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}" +
            "&returnType=json")
    suspend fun getNearbyMonitoringStation(
        @Query("tmX") tmX: Double,
        @Query("tmY") tmY: Double
    ): Response<MonitoringStationsResponse>

    @GET("/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty" +
            "?serviceKey=${BuildConfig.AIR_KOREA_SERVICE_KEY}" +
            "&returnType=json" +
            "&dataTerm=DAILY" +
            "&ver=1.3")
    suspend fun getRealtimeAirQualities(
        @Query("stationName") stationName: String
    ):Response<AirQualityResponse>
}