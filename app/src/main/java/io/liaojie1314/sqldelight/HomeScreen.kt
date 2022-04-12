package io.liaojie1314.sqldelight

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.liaojie1314.datastorage.db.Bills
import io.liaojie1314.sqldelight.viewmodel.BillViewModel

@ExperimentalFoundationApi
@Composable
fun HomeScreen(navController: NavController, billViewModel: BillViewModel) {
    val bills = billViewModel.bills.collectAsState(initial = emptyList()).value
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(bills) { bills ->
                BillItem(navController, billViewModel, bills)
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun BillItem(navController: NavController, billViewModel: BillViewModel, bills: Bills) {
    Card(
        modifier = Modifier
            .height(80.dp)
            .combinedClickable(
                onClick = {
                    navController.navigate(
                        "dialog?id=${bills.id},type=${bills.type},typename=${bills.typename},cost=${bills.cost}"
                    )
                },
                onLongClick = {
                    navController.navigate("deldialog/${bills.id}")
                },
                onDoubleClick = {}
            ),
        shape = RoundedCornerShape(10.dp),
        elevation = 2.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = when (bills.type) {
                    0L -> painterResource(id = R.mipmap.ic_type_01)
                    1L -> painterResource(id = R.mipmap.ic_type_03)
                    2L -> painterResource(id = R.mipmap.ic_type_02)
                    else -> painterResource(id = R.mipmap.ic_type_01)
                },
                contentDescription = "",
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = bills.typename, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = bills.time, color = Color.Gray)
            }
            if (bills.type == 2L) {
                Text(
                    text = "+${bills.cost}$",
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFF44B8F2)
                )
            } else {
                Text(
                    text = "-${bills.cost}$",
                    style = MaterialTheme.typography.h6,
                    color = Color(0xFFFE343D)
                )
            }
        }
    }
}