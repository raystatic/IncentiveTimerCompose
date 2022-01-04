package com.raystatic.incentivetimer.rewardlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raystatic.incentivetimer.data.Reward
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RewardListViewModel @Inject constructor(): ViewModel() {

    private val _dummyRecords = MutableLiveData<List<Reward>>()
    val dummyRecords: LiveData<List<Reward>> get() = _dummyRecords

    init {
        val dummyRecords = mutableListOf<Reward>()
        repeat(100){index->
            dummyRecords+= Reward(icon = Icons.Default.Star,title = "Item ${index + 1}", chanceInPercent = index+1)
        }
        _dummyRecords.value = dummyRecords
    }

}