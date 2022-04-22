package io.liaojie1314.sqldelight.repository

import androidx.paging.PagingSource
import com.squareup.sqldelight.EnumColumnAdapter
import com.squareup.sqldelight.android.paging3.QueryPagingSource
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.liaojie1314.datastorage.db.BillDataBase
import io.liaojie1314.datastorage.db.Bills
import io.liaojie1314.sqldelight.model.BillTypeVals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BillRepository(sqlDriver: SqlDriver) {
    private val dbRef: BillDataBase = BillDataBase(sqlDriver,
    billsAdapter = Bills.Adapter(
        typenameAdapter = EnumColumnAdapter()
    ))

    fun allBills():PagingSource<Long,Bills>{
        return QueryPagingSource(
            countQuery = dbRef.billTableQueries.billcount(),
            transacter = dbRef.billTableQueries,
            dispatcher = Dispatchers.IO,
            queryProvider = dbRef.billTableQueries::allBills
        )
    }

    suspend fun setupFavorite(favorite: Boolean, id: Long) {
        withContext(Dispatchers.IO) {
            dbRef.billTableQueries.setupFavorite(favorite, id)
        }
    }

    fun getAllBill(): Flow<List<Bills>> {
        return dbRef.billTableQueries.getAllBills().asFlow().mapToList()
    }

    suspend fun insertBill(id: Long?, type: Int, typeName: BillTypeVals.BillType, cost: String, time: String,favorite: Boolean) {
        withContext(Dispatchers.IO) {
            dbRef.billTableQueries.insertBill(id, type, typeName, cost, time,favorite)
        }
    }

    suspend fun updateBillById(id: Long, type: Int, typeName: BillTypeVals.BillType, cost: String, time: String) {
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