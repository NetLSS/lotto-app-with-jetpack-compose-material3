package com.lilcode.examples.jetpack_compose_material3.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


fun <T> MutableLiveData<T>.toLiveData() = this as LiveData<T>

fun <T> T.toMutableLiveData() = MutableLiveData(this)