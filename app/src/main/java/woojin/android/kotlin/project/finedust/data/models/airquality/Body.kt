package woojin.android.kotlin.project.finedust.data.models.airquality


import com.google.gson.annotations.SerializedName

data class Body(
    @SerializedName("items")
    val airQualities: List<AirQuality>?,
    @SerializedName("numOfRows")
    val numOfRows: Int?,
    @SerializedName("pageNo")
    val pageNo: Int?,
    @SerializedName("totalCount")
    val totalCount: Int?
)