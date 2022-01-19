package com.raystatic.incentivetimer.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.raystatic.incentivetimer.core.ui.IconKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "rewards")
@Parcelize
data class Reward(
    val icon:IconKey,
    val title: String,
    val chanceInPercent:Int,
    val isUnlocked: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
):Parcelable
