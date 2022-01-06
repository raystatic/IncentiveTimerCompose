package com.raystatic.incentivetimer.rewardlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.raystatic.incentivetimer.data.Reward
import com.raystatic.incentivetimer.data.RewardDao
import com.raystatic.incentivetimer.ui.IconKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RewardListViewModel @Inject constructor(
    private val rewardDao: RewardDao
): ViewModel() {

   val rewards = rewardDao.getAllRewards().asLiveData()

}