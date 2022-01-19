package com.raystatic.incentivetimer.features.addeditreward

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raystatic.incentivetimer.data.Reward
import com.raystatic.incentivetimer.data.RewardDao
import com.raystatic.incentivetimer.core.ui.IconKey
import com.raystatic.incentivetimer.core.ui.defaultRrewardIcon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRewardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val rewardDao: RewardDao
): ViewModel(), AddEditRewardActions {

    sealed class AddEditRewardEvent{
        object RewardCreated: AddEditRewardEvent()
        object RewardUpdated: AddEditRewardEvent()
        object RewardDeleted: AddEditRewardEvent()
    }

    private val eventChannel = Channel<AddEditRewardEvent>()
    val events = eventChannel.receiveAsFlow()

    private val rewardId = savedStateHandle.get<Long>(ARG_REWARD_ID)
    private var reward:Reward?=null

    val isEditMode = rewardId != NO_REWARD_ID

    private fun populateEmptyInputWithDefaults() {

        if (rewardNameLiveData.value == null){
            rewardNameLiveData.value = ""
        }

        if (chanceInPercentInputLiveData.value == null){
            chanceInPercentInputLiveData.value = 10
        }

        if (rewardIconSelectionLiveData.value == null){
            rewardIconSelectionLiveData.value = defaultRrewardIcon
        }
    }

    private fun populateEmptyInputValuesWithRewardData() {
        val reward = this.reward
        if (reward!=null){
            if (rewardNameLiveData.value == null){
                rewardNameLiveData.value = reward.title
            }
            
            if (chanceInPercentInputLiveData.value == null){
                chanceInPercentInputLiveData.value = reward.chanceInPercent
            }
            
            if (rewardIconSelectionLiveData.value == null){
                rewardIconSelectionLiveData.value = reward.icon
            }
        }
    }

    private val rewardNameLiveData = savedStateHandle.getLiveData<String>("rewardNameLiveData")
    val rewardNameInput:LiveData<String> = rewardNameLiveData

    private val rewardNameInputIsErrorLiveData = savedStateHandle.getLiveData<Boolean>("rewardNameInputIsErrorLiveData", false)
    val rewardNameInputIsError:LiveData<Boolean> = rewardNameInputIsErrorLiveData

    private val chanceInPercentInputLiveData = savedStateHandle.getLiveData<Int>("chanceInPercentLiveData")
    val chanceInPercentInput :LiveData<Int> = chanceInPercentInputLiveData

    private val rewardIconSelectionLiveData = savedStateHandle.getLiveData<IconKey>("rewardIconSelectionLiveData")
    val rewardIconSelection:LiveData<IconKey> = rewardIconSelectionLiveData

    private val showRewardIconSelectionDialogLiveData = savedStateHandle.getLiveData<Boolean>("showRewardIconSelectionDialogLiveData",false)
    val showRewardIconSelectionDialog:LiveData<Boolean> = showRewardIconSelectionDialogLiveData

    private val showDeleteRewardConfirmationDialogLiveData = savedStateHandle.getLiveData<Boolean>("showDeleteRewardConfirmationDialogLiveData", false)
    val showDeleteRewardConfirmationDialog: LiveData<Boolean> = showDeleteRewardConfirmationDialogLiveData

    init {
        if (rewardId != null && rewardId != NO_REWARD_ID){
            viewModelScope.launch {
                reward = rewardDao.getRewardById(rewardId)
                populateEmptyInputValuesWithRewardData()
            }
        }else{
            populateEmptyInputWithDefaults()
        }
    }

    override fun onRewardNameInputChanged(input:String){
        rewardNameLiveData.value = input
    }

    override fun onChanceInPercentInputChanged(input:Int){
        chanceInPercentInputLiveData.value = input
    }

    override fun onRewardIconButtonClicked(){
        showRewardIconSelectionDialogLiveData.value = true
    }

    override fun onRewardIconSelected(iconKey: IconKey){
        rewardIconSelectionLiveData.value = iconKey
    }

    override fun onRewardIconDialogDismissRequest(){
        showRewardIconSelectionDialogLiveData.value = false
    }

    override fun onSaveClicked(){
        val rewardNameInput = rewardNameInput.value
        val chanceInPercentInput = chanceInPercentInput.value
        val rewardIconKeySelection = rewardIconSelection.value

        rewardNameInputIsErrorLiveData.value = false

        viewModelScope.launch {
            if (!rewardNameInput.isNullOrBlank() && chanceInPercentInput!= null && rewardIconKeySelection != null){
                val reward = reward
                if (reward != null){
                    updateReward(reward.copy(title = rewardNameInput,chanceInPercent = chanceInPercentInput,icon = rewardIconKeySelection))
                }else{
                    createReward(
                        Reward(
                            icon =  rewardIconKeySelection,
                            title = rewardNameInput,
                            chanceInPercent = chanceInPercentInput
                        )
                    )
                }
            }else{
                if (rewardNameInput.isNullOrBlank()){
                    rewardNameInputIsErrorLiveData.value = true
                }
            }
        }

    }

    override fun onDeleteClicked() {
        showDeleteRewardConfirmationDialogLiveData.value = true
    }

    override fun onDeleteRewardConfirmed() {
        showDeleteRewardConfirmationDialogLiveData.value = false
        viewModelScope.launch {
            val reward =  reward
            if (reward!=null){
                rewardDao.deleteReward(reward)
                eventChannel.send(AddEditRewardEvent.RewardDeleted)
            }
        }
    }

    override fun onDeleteRewardDialogDismissed() {
        showDeleteRewardConfirmationDialogLiveData.value = false
    }

    private suspend fun createReward(reward: Reward) {
        rewardDao.insertReward(reward = reward)
        eventChannel.send(AddEditRewardEvent.RewardCreated)
    }

    private suspend fun updateReward(reward: Reward) {
        rewardDao.upadateReward(reward = reward)
        eventChannel.send(AddEditRewardEvent.RewardUpdated)
    }

}


const val ARG_REWARD_ID = "rewardId"
const val NO_REWARD_ID = -1L

const val ADD_EDIT_REWARD_RESULT = "ADD_EDIT_REWARD_RESULT"
const val RESULT_REWARD_ADDED = "RESULT_REWARD_ADDED"
const val RESULT_REWARD_UPDATED = "RESULT_REWARD_UPDATED"
const val RESULT_REWARD_DELETED = "RESULT_REWARD_DELETED"