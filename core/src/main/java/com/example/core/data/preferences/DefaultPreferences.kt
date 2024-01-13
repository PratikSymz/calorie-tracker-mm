package com.example.core.data.preferences

import android.content.SharedPreferences
import com.example.core.domain.models.ActivityLevel
import com.example.core.domain.models.Gender
import com.example.core.domain.models.GoalType
import com.example.core.domain.models.UserInfo
import com.example.core.domain.preferences.Preferences

class DefaultPreferences(
    private val sharePref: SharedPreferences
) : Preferences {
    override fun saveGender(gender: Gender) {
        sharePref.edit()
            .putString(Preferences.KEY_GENDER, gender.name)
            .apply()
    }

    override fun saveAge(age: Int) {
        sharePref.edit()
            .putInt(Preferences.KEY_AGE, age)
            .apply()
    }

    override fun saveWeight(weight: Float) {
        sharePref.edit()
            .putFloat(Preferences.KEY_WEIGHT, weight)
            .apply()
    }

    override fun saveHeight(height: Int) {
        sharePref.edit()
            .putInt(Preferences.KEY_HEIGHT, height)
            .apply()
    }

    override fun saveActivityLevel(activityLevel: ActivityLevel) {
        sharePref.edit()
            .putString(Preferences.KEY_ACTIVITY_LEVEL, activityLevel.name)
            .apply()
    }

    override fun saveGoalType(goalType: GoalType) {
        sharePref.edit()
            .putString(Preferences.KEY_GOAL_TYPE, goalType.name)
            .apply()
    }

    override fun saveCarbRatio(ratio: Float) {
        sharePref.edit()
            .putFloat(Preferences.KEY_CARB_RATIO, ratio)
            .apply()
    }

    override fun saveProteinRatio(ratio: Float) {
        sharePref.edit()
            .putFloat(Preferences.KEY_PROTEIN_RATIO, ratio)
            .apply()
    }

    override fun saveFatRatio(ratio: Float) {
        sharePref.edit()
            .putFloat(Preferences.KEY_FAT_RATIO, ratio)
            .apply()
    }

    override fun loadUserInfo(): UserInfo {
        // Load all user info from the shared preferences
        val genderString = sharePref.getString(Preferences.KEY_GENDER, null)
        val age = sharePref.getInt(Preferences.KEY_AGE, -1)
        val weight = sharePref.getFloat(Preferences.KEY_WEIGHT, -1f)
        val height = sharePref.getInt(Preferences.KEY_HEIGHT, -1)
        val activityLevelString = sharePref.getString(Preferences.KEY_ACTIVITY_LEVEL, null)
        val goalTypeString = sharePref.getString(Preferences.KEY_GOAL_TYPE, null)
        val carbRatio = sharePref.getFloat(Preferences.KEY_CARB_RATIO, -1f)
        val proteinRatio = sharePref.getFloat(Preferences.KEY_PROTEIN_RATIO, -1f)
        val fatRatio = sharePref.getFloat(Preferences.KEY_FAT_RATIO, -1f)

        return UserInfo(
            gender = Gender.fromString(genderString ?: "male"),
            age = age,
            weight = weight,
            height = height,
            activityLevel = ActivityLevel.fromString(activityLevelString ?: "medium"),
            goalType = GoalType.fromString(goalTypeString ?: "keep_weight"),
            carbRatio = carbRatio,
            proteinRatio = proteinRatio,
            fatRatio = fatRatio
        )
    }

    override fun saveShouldShowOnboarding(shouldShow: Boolean) {
        sharePref.edit()
            .putBoolean(Preferences.KEY_SHOULD_SHOW_ONBOARDING, shouldShow)
            .apply()
    }

    override fun loadShouldShowOnboarding(): Boolean {
        return sharePref.getBoolean(
            Preferences.KEY_SHOULD_SHOW_ONBOARDING,
            true
        )
    }
}