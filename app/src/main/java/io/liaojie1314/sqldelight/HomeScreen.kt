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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import io.liaojie1314.datastorage.db.Bills
import io.liaojie1314.sqldelight.viewmodel.BillViewModel

@ExperimentalFoundationApi
@Composable
fun HomeScreen(navController: NavController, billViewModel: BillViewModel) {
    val bills = billViewModel.bills.collectAsState(initial = emptyList()).value
    val billlistItem = billViewModel.billlist.collectAsLazyPagingItems()
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
//            items(
//                items = billlistItem,
//                key = { item: Bills ->
//                    item.id
//                }
//            ) { bills ->
//                bills?.let {
//                    BillItem(navController, billViewModel, it)
//                }
//            }
        }

//        billlistItem.apply {
//            when {
//                loadState.refresh is LoadState.Loading -> {
//                    LoadUI()
//                }
//                loadState.append is LoadState.Loading -> {
//                    LoadUI()
//                }
//                loadState.prepend is LoadState.Loading -> {
//                    LoadUI()
//                }
//            }
//        }
    }
}

@Composable
fun LoadUI() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            color = Color.Red,
            strokeWidth = 2.dp,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Loading...")
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
                        "dialog?id=${bills.id},type=${bills.type},typename=${bills.typename.name},cost=${bills.cost}"
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
                    0 -> painterResource(id = R.mipmap.ic_type_01)
                    1 -> painterResource(id = R.mipmap.ic_type_03)
                    2 -> painterResource(id = R.mipmap.ic_type_02)
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
                Text(
                    text = when (bills.typename.name) {
                        "traffic" -> "出行"
                        "food" -> "餐饮"
                        "money" -> "理财"
                        else -> "出行"
                    }, style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = bills.time, color = Color.Gray)
            }
            if (bills.type == 2) {
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
            IconButton(onClick = {
                val favorite = if (bills.favorite ?: false) false else true
                billViewModel.setupFavority(favorite, bills.id)
            }) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "",
                    tint = if (bills.favorite ?: false) Color.Red else Color.LightGray
                )
            }
        }
    }
}