package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.BR
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RepresentativeViewModel : ViewModel(), Observable {
    private val repository = RepresentativeRepository()
    private val propertyChangeRegistry = PropertyChangeRegistry()

    //: Establish live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives


    private val _status = MutableLiveData<Constants.ApiStatus>()
    val status: LiveData<Constants.ApiStatus>
        get() = _status

    //: Create function to fetch representatives from API from a provided address
    fun getRepresentativesFromNetwork() {
        Log.d(
            "ReprensentativeViewModel",
            "getRepresentativesFromNetwork: ${address.line1} ${address.line2}"
        )
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
//    fun setAddress(address: Address) {
//        // _address.value = address
//    }

    //: Create function to get address from individual fields
    @Bindable
    var address = Address("", "", "", "", "")
        set(value) {
            if (value != field) {
                field = value
                propertyChangeRegistry.notifyChange(this, BR.address)
            }
        }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.remove(callback)
    }

    private var _inputMsg = MutableLiveData<String?>()
    val inputMsg: LiveData<String?>
        get() = _inputMsg

    private fun isValidateInputs(): Boolean {
        when {
            address.line1.isEmpty() -> {
                _inputMsg.value = "Enter Line1"
                return false
            }
            address.line2.isNullOrEmpty() -> {
                _inputMsg.value = "Enter Lin2"
                return false
            }
            address.city.isEmpty() -> {
                _inputMsg.value = "Enter City"
                return false
            }
            address.state.isEmpty() -> {
                _inputMsg.value = "Enter State"
                return false
            }
            address.zip.isEmpty() -> {
                _inputMsg.value = "Enter Zip"
                return false
            }
            else -> return true
        }
    }


}
