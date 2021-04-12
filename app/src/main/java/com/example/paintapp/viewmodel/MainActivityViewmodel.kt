package com.example.paintapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paintapp.utils.ToolsEnum

class MainActivityViewmodel: ViewModel() {

    private var _onToolSelected = MutableLiveData<ToolsEnum>()
    val onToolSelected : LiveData<ToolsEnum> get() = _onToolSelected

    fun onToolSelected(toolsEnum: ToolsEnum){
        _onToolSelected.postValue(toolsEnum)
    }
}