package com.lilcode.examples.jetpack_compose_material3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lilcode.examples.jetpack_compose_material3.lottery.LotteryHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LotteryViewModel @Inject constructor() : ViewModel() {
//    val lotteryNavHostController = NavHostController()

    private val _lottery720liveData = MutableLiveData(listOf<LotteryHelper.Data720>())
    val lottery720liveData: LiveData<List<LotteryHelper.Data720>> = _lottery720liveData

    fun updateLottery720liveData(newData: List<LotteryHelper.Data720>) {
        _lottery720liveData.postValue(newData)
    }

}