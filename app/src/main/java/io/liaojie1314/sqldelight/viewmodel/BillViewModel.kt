package io.liaojie1314.sqldelight.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.liaojie1314.sqldelight.repository.BillRepository
import kotlinx.coroutines.launch

class BillViewModel(private val repository:BillRepository):ViewModel() {
    val bills=repository.getAllBill()

    fun setupFavority(favorite:Long,id:Long){
        viewModelScope.launch {
            repository.setupFavorite(favorite, id)
        }
    }
    //add Bill
    fun addBill(id: Long?, type: Long, typename: String, cost: String, time: String) {
        viewModelScope.launch {
            repository.insertBill(id, type, typename, cost, time)
        }
    }
    //update Bill
    fun updateBill(id: Long,type: Long,typename: String,cost: String,time: String){
        viewModelScope.launch {
            repository.updateBillById(id, type, typename, cost, time)
        }
    }
    //delete Bill
    fun deleteBillById(id: Long){
        viewModelScope.launch {
            repository.deleteBillById(id)
        }
    }
}

class BillViewModelFactory(
    private val repository: BillRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(BillViewModel::class.java)) {
            return BillViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}