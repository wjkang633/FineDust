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
    GOOD("μ’μ", "π", R.color.blue),

    @SerializedName("2")
    NORMAL("λ³΄ν΅", "π", R.color.green),

    @SerializedName("3")
    BAD("λμ¨", "π", R.color.yellow),

    @SerializedName("4")
    AWFUL("λ§€μ° λμ¨", "π«", R.color.red),

    UNKNOWN("λ―ΈμΈ‘μ ", "π§", R.color.grey);

    override fun toString(): String {
        return "$label $emoji"
    }
}