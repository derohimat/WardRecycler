package com.wardabbass.redit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * a View model that will carry the data and the state of the Main activity
 */
class MainActivityViewModel : ViewModel() {
    var query: MutableLiveData<String> = MutableLiveData()
    var selectedScreen = 0
}