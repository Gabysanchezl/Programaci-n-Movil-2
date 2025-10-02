/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApiFilter
import kotlinx.coroutines.launch

enum class MarsApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<MarsApiStatus>
        get() = _status

    // Internally, we use a MutableLiveData, to update the MarsProperty with new values
    // The external LiveData interface to the property is immutable, so only this class can modify

    private val _property = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _property

    // Internally, we use a MutableLiveData to handle navigation to the selected property
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()

    // The external immutable LiveData for the navigation property
    val navigateToSelectedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty




    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

//    MarsApi.retrofitService.getProperties().enqueue( object: Callback<String> {
//        override fun onFailure(call: Call<String>, t: Throwable) {
//            _response.value = "Failure: " + t.message
//        }
//
//        override fun onResponse(call: Call<String>, response: Response<String>) {
//            _response.value = response.body()
//        }
//    })

    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
        viewModelScope.launch {

            _status.value = MarsApiStatus.LOADING
            try {
                _property.value = MarsApi.retrofitService.getProperties(filter.value)
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _property.value = ArrayList()
            }
        }
    }


    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }


    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }
    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

}
