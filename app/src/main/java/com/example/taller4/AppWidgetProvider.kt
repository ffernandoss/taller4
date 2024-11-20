package com.example.taller4

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.google.firebase.firestore.FirebaseFirestore

class CarWidgetProvider : AppWidgetProvider() {

    companion object {
        private var showingModels = true
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    private fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val db = FirebaseFirestore.getInstance()
        db.collection("coches").get().addOnSuccessListener { result ->
            val carInfo = if (showingModels) {
                result.map { it.getString("modelo") ?: "" }.joinToString("\n")
            } else {
                result.map { it.getString("marca") ?: "" }.joinToString("\n")
            }
            for (appWidgetId in appWidgetIds) {
                val views = RemoteViews(context.packageName, R.layout.car_widget)
                views.setTextViewText(R.id.carNamesTextView, carInfo)

                val intent = Intent(context, CarWidgetProvider::class.java).apply {
                    action = "com.example.taller4.ACTION_TOGGLE_CAR_INFO"
                }
                val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "com.example.taller4.ACTION_TOGGLE_CAR_INFO") {
            showingModels = !showingModels
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, CarWidgetProvider::class.java))
            updateWidgets(context, appWidgetManager, appWidgetIds)
        }
    }
}