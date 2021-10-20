package woojin.android.kotlin.project.finedust.data.models.airquality

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import woojin.android.kotlin.project.finedust.R

enum class Grade(
    val label: String,
    val emoji: String,
    @ColorRes val colorResId: Int
) {
    @SerializedName("1")
    GOOD("좋음", "😆", R.color.blue),

    @SerializedName("2")
    NORMAL("보통", "🙂", R.color.green),

    @SerializedName("3")
    BAD("나쁨", "😞", R.color.yellow),

    @SerializedName("4")
    AWFUL("매우 나쁨", "😫", R.color.red),

    UNKNOWN("미측정", "🧐", R.color.grey);

    override fun toString(): String {
        return "$label $emoji"
    }
}