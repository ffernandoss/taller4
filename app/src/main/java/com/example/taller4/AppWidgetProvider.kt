// File: app/src/main/java/com/example/taller4/CarWidgetProvider.kt
package com.example.taller4

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.RemoteViews
import com.google.firebase.firestore.FirebaseFirestore

class CarWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val handler = Handler(Looper.getMainLooper())
        val updateRunnable = object : Runnable {
            override fun run() {
                updateWidgets(context, appWidgetManager, appWidgetIds)
                handler.postDelayed(this, 3000) // Re-run every 3 seconds
            }
        }
        handler.post(updateRunnable)
    }

    private fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val db = FirebaseFirestore.getInstance()
        db.collection("coches").get().addOnSuccessListener { result ->
            val carNames = result.map { it.getString("marca") ?: "" }.joinToString("\n")
            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId, carNames)
            }
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, carNames: String) {
        val views = RemoteViews(context.packageName, R.layout.car_widget)
        views.setTextViewText(R.id.carNamesTextView, carNames)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}