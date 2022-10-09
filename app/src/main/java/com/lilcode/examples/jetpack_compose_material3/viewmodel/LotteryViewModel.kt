package com.lilcode.examples.jetpack_compose_material3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lilcode.examples.jetpack_compose_material3.common.toLiveData
import com.lilcode.examples.jetpack_compose_material3.common.toMutableLiveData
import com.lilcode.examples.jetpack_compose_material3.lottery.LotteryHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LotteryViewModel @Inject constructor() : ViewModel() {
//    val lotteryNavHostController = NavHostController()

    private val _lottery720liveData = emptyList<LotteryHelper.Data720>().toMutableLiveData()
    val lottery720liveData = _lottery720liveData.toLiveData()

    private val _lottery645liveData = emptyList<LotteryHelper.Data645>().toMutableLiveData()
    val lottery645liveData = _lottery645liveData.toLiveData()

    fun updateLottery720liveData(newData: List<LotteryHelper.Data720>) {
        _lottery720liveData.postValue(newData)
    }

    fun updateLottery645liveData(newData: List<LotteryHelper.Data645>) {
        _lottery645liveData.postValue(newData)
    }

}