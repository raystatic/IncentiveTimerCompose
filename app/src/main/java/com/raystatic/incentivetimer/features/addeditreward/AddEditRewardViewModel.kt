package com.raystatic.incentivetimer.features.addeditreward

import androidx.lifecycle.*
import com.raystatic.incentivetimer.data.Reward
import com.raystatic.incentivetimer.data.RewardDao
import com.raystatic.incentivetimer.core.ui.IconKey
import com.raystatic.incentivetimer.core.ui.defaultRrewardIcon
import com.raystatic.incentivetimer.core.ui.screenSpecs.AddEditRewardScreenSpec
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

    private val rewardId = AddEditRewardScreenSpec.getRewardIdFromSavedStateHandle(savedStateHandle = savedStateHandle)
    private val rewardLiveData = savedStateHandle.getLiveData<Reward>(KEY_REWARD_LIVE_DATA)

    val isEditMode = AddEditRewardScreenSpec.isEditMode(rewardId = rewardId)

    val rewardNameInput:LiveData<String> = rewardLiveData.map {
        it.title
    }

    private val rewardNameInputIsErrorLiveData = savedStateHandle.getLiveData<Boolean>("rewardNameInputIsErrorLiveData", false)
    val rewardNameInputIsError:LiveData<Boolean> = rewardNameInputIsErrorLiveData

    val chanceInPercentInput :LiveData<Int> = rewardLiveData.map {
        it.chanceInPercent
    }
 
    val rewardIconSelection:LiveData<IconKey> = rewardLiveData.map {
        it.icon
    }

    private val showRewardIconSelectionDialogLiveData = savedStateHandle.getLiveData<Boolean>("showRewardIconSelectionDialogLiveData",false)
    val showRewardIconSelectionDialog:LiveData<Boolean> = showRewardIconSelectionDialogLiveData

    private val showDeleteRewardConfirmationDialogLiveData = savedStateHandle.getLiveData<Boolean>("showDeleteRewardConfirmationDialogLiveData", false)
    val showDeleteRewardConfirmationDialog: LiveData<Boolean> = showDeleteRewardConfirmationDialogLiveData

    init {

        if (!savedStateHandle.contains(KEY_REWARD_LIVE_DATA )){
            if (rewardId != null && isEditMode){
                viewModelScope.launch {
                    rewardLiveData.value = rewardDao.getRewardById(rewardId)
                }
            }else{
                rewardLiveData.value = Reward(icon = defaultRrewardIcon,title = "",chanceInPercent = 10)
            }
        }
    }

    override fun onRewardNameInputChanged(input:String){
        rewardLiveData.value = rewardLiveData.value?.copy(title = input)
    }

    override fun onChanceInPercentInputChanged(input:Int){
        rewardLiveData.value = rewardLiveData.value?.copy(chanceInPercent = input)
    }

    override fun onRewardIconButtonClicked(){
        showRewardIconSelectionDialogLiveData.value = true
    }

    override fun onRewardIconSelected(iconKey: IconKey){
        rewardLiveData.value = rewardLiveData.value?.copy(icon = iconKey)
    }

    override fun onRewardIconDialogDismissRequest(){
        showRewardIconSelectionDialogLiveData.value = false
    }

    override fun onSaveClicked(){
        val reward = rewardLiveData.value ?: return
        rewardNameInputIsErrorLiveData.value = false

        viewModelScope.launch {
            if (reward.title.isNotBlank()){
                if (isEditMode){
                    updateReward(reward)
                }else{
                    createReward(reward)
                }
            }else{
                rewardNameInputIsErrorLiveData.value = true
            }
        }

    }

    override fun onDeleteClicked() {
        showDeleteRewardConfirmationDialogLiveData.value = true
    }

    override fun onDeleteRewardConfirmed() {
        showDeleteRewardConfirmationDialogLiveData.value = false
        viewModelScope.launch {
            val reward =  rewardLiveData.value
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


const val KEY_REWARD_LIVE_DATA = "KEY_REWARD_LIVE_DATA"

const val ARG_REWARD_ID = "rewardId"
const val NO_REWARD_ID = -1L

const val ADD_EDIT_REWARD_RESULT = "ADD_EDIT_REWARD_RESULT"
const val RESULT_REWARD_ADDED = "RESULT_REWARD_ADDED"
const val RESULT_REWARD_UPDATED = "RESULT_REWARD_UPDATED"
const val RESULT_REWARD_DELETED = "RESULT_REWARD_DELETED"