package com.raystatic.incentivetimer.data

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rewards")
data class Reward(
    val icon:String,
    val title: String,
    val chanceInPercent:Int,
    val isUnlocked: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)
