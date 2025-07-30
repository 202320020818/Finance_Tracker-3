package com.example.coincove.data

import android.content.Context
import android.content.SharedPreferences
import com.example.coincove.model.Budget
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManager(context: Context) {

    // Initialize SharedPreferences with a private XML file to store persistent data
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )

    // Gson is used to convert custom objects like Budget to JSON and back
    private val gson = Gson()

    companion object {
        private const val PREFERENCES_NAME = "finance_tracker_prefs" // Name of the SharedPreferences file
        private const val KEY_CURRENCY = "currency"                   // Key for storing currency type
        private const val KEY_BUDGET = "budget"                       // Key for storing serialized budget object
        private const val KEY_NOTIFICATION_ENABLED = "notification_enabled" // Key for storing notification preference
        private const val KEY_REMINDER_ENABLED = "reminder_enabled"         // Key for storing reminder preference
    }

    // Save the selected currency type ("LKR")
    fun setCurrency(currency: String) {
        sharedPreferences.edit().putString(KEY_CURRENCY, currency).apply()
    }

    // Retrieve the stored currency type, defaulting to "USD" if none exists
    fun getCurrency(): String {
        return sharedPreferences.getString(KEY_CURRENCY, "USD") ?: "USD"
    }

    // Save the user's budget by converting the Budget object to a JSON string
    fun setBudget(budget: Budget) {
        val budgetJson = gson.toJson(budget.toMap()) // Convert budget to Map and then to JSON
        sharedPreferences.edit().putString(KEY_BUDGET, budgetJson).apply()
    }

    // Retrieve the saved budget from SharedPreferences and convert it back to a Budget object
    fun getBudget(): Budget {
        val budgetJson = sharedPreferences.getString(KEY_BUDGET, null)
        return if (budgetJson != null) {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val budgetMap: Map<String, Any> = gson.fromJson(budgetJson, type)
            Budget.fromMap(budgetMap) // Reconstruct Budget object from the map
        } else {
            Budget(0.0) // Default budget if none exists
        }
    }

    // Save the user's preference for enabling or disabling notifications
    fun setNotificationEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply()
    }

    // Retrieve the stored notification setting, defaulting to true
    fun isNotificationEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, true)
    }

    // Save the user's preference for enabling or disabling reminders
    fun setReminderEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_REMINDER_ENABLED, enabled).apply()
    }

    // Retrieve the stored reminder setting, defaulting to false
    fun isReminderEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_REMINDER_ENABLED, false)
    }
}
