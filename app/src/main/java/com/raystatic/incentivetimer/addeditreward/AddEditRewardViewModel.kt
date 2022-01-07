package com.raystatic.incentivetimer.addeditreward

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raystatic.incentivetimer.data.Reward
import com.raystatic.incentivetimer.data.RewardDao
import com.raystatic.incentivetimer.ui.IconKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRewardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val rewardDao: RewardDao
): ViewModel() {

    sealed class AddEditRewardEvent{
        object RewardCreated: AddEditRewardEvent()
    }

    private val eventChannel = Channel<AddEditRewardEvent>()
    val events = eventChannel.receiveAsFlow()

    private val rewardId = savedStateHandle.get<Long>(ARG_REWARD_ID)

    val isEditMode = rewardId != null

    private val rewardNameLiveData = savedStateHandle.getLiveData<String>("rewardNameLiveData","")
    val rewardNameInput:LiveData<String> = rewardNameLiveData

    private val chanceInPercentInputLiveData = savedStateHandle.getLiveData<Int>("chanceInPercentLiveData",10)
    val chanceInPercentInput :LiveData<Int> = chanceInPercentInputLiveData

    fun onRewardNameInputChanged(input:String){
        rewardNameLiveData.value = input
    }

    fun onChanceInPercentInputChanged(input:Int){
        chanceInPercentInputLiveData.value = input
    }

    fun onSaveClicked(){
        val rewardNameInput = rewardNameInput.value
        val chanceInPercentInput = chanceInPercentInput.value

        viewModelScope.launch {
            if (rewardNameInput!=null && rewardNameInput.isNotBlank() && chanceInPercentInput!= null){
                if (rewardId != null){
                    updateReward()
                }else{
                    createReward(
                        Reward(
                            icon =  IconKeys.CAKE,
                            title = rewardNameInput,
                            chanceInPercent = chanceInPercentInput
                        )
                    )
                }
            }else{
                // Todo: Show Error
            }
        }

    }

    private suspend fun createReward(reward: Reward) {
        rewardDao.insertReward(reward = reward)
        eventChannel.send(AddEditRewardEvent.RewardCreated)
    }

    private suspend fun updateReward() {
        TODO("Not yet implemented")
    }

}


const val ARG_REWARD_ID = "ARG_REWARD_ID"