package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RepresentativeViewModel : ViewModel() {
    private val repository = RepresentativeRepository()

    //: Establish live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _status = MutableLiveData<Constants.ApiStatus>()
    val status: LiveData<Constants.ApiStatus>
        get() = _status

    //: Create function to fetch representatives from API from a provided address
    fun getRepresentativesFromNetwork() {
        viewModelScope.launch {
            _status.value = Constants.ApiStatus.LOADING
            repository.getRepresentative("94043")
                .catch {
                    _status.value = Constants.ApiStatus.ERROR
                    Log.d("RepresentativeRepository", "getRepresentative: ${it.message}")
                }
                .collect { representativeResponse ->
                    _status.value = Constants.ApiStatus.DONE
                    _representatives.value = representativeResponse.offices.flatMap { office ->
                        office.getRepresentatives(representativeResponse.officials)
                    }
                }
        }
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //: Create function get address from geo location
    fun setAddress(address: Address) {
        _address.value = address
    }

    //TODO: Create function to get address from individual fields

}
