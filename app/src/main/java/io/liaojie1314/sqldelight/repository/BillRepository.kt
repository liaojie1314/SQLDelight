package io.liaojie1314.sqldelight.repository

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.liaojie1314.datastorage.db.BillDataBase
import io.liaojie1314.datastorage.db.Bills
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BillRepository(sqlDriver: SqlDriver) {
    private val dbRef: BillDataBase = BillDataBase(sqlDriver)

    suspend fun setupFavorite(favorite: Long, id: Long) {
        withContext(Dispatchers.IO) {
            dbRef.billTableQueries.setupFavorite(favorite, id)
        }
    }

    fun getAllBill(): Flow<List<Bills>> {
        return dbRef.billTableQueries.getAllBills().asFlow().mapToList()
    }

    suspend fun insertBill(id: Long?, type: Long, typeName: String, cost: String, time: String) {
        withContext(Dispatchers.IO) {
            dbRef.billTableQueries.insertBill(id, type, typeName, cost, time)
        }
    }

    suspend fun updateBillById(id: Long, type: Long, typeName: String, cost: String, time: String) {
        withContext(Dispatchers.IO) {
            dbRef.billTableQueries.updateBillById(type, typeName, cost, time, id)
        }
    }

    suspend fun deleteBillById(id: Long) {
        withContext(Dispatchers.IO) {
            dbRef.billTableQueries.deleteBillById(id)
        }
    }
}