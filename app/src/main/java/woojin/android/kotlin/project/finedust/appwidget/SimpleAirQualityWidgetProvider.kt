package woojin.android.kotlin.project.finedust.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import androidx.lifecycle.LifecycleService

class SimpleAirQualityWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

    }

    class UpdateWidgetService:LifecycleService(){
        
    }
}