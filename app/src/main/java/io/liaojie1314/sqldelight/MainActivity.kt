package io.liaojie1314.sqldelight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.liaojie1314.datastorage.db.BillDataBase
import io.liaojie1314.datastorage.db.Bills
import io.liaojie1314.sqldelight.model.BillTypeVals
import io.liaojie1314.sqldelight.repository.BillRepository
import io.liaojie1314.sqldelight.ui.theme.SQLDelightTheme
import io.liaojie1314.sqldelight.viewmodel.BillViewModel
import io.liaojie1314.sqldelight.viewmodel.BillViewModelFactory


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SQLDelightTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    val androidSqliteDriver = AndroidSqliteDriver(
                        BillDataBase.Schema,
                        context.applicationContext,
                        "bill.db"
                    )
                    val repository = BillRepository(androidSqliteDriver)
                    val billViewModel: BillViewModel = viewModel(
                        factory = BillViewModelFactory(repository)
                    )
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(text = "记账App") },
                                navigationIcon = {
                                    Icon(
                                        painter = painterResource(id = R.mipmap.ic_logo),
                                        contentDescription = "",
                                        tint = Color.Unspecified
                                    )
                                },
                                actions = {
                                    IconButton(onClick = {
                                        navController.navigate("dialog")
                                    }) {
                                        Icon(
                                            Icons.Default.Add,
                                            contentDescription = "",
                                            tint = Color.White
                                        )
                                    }
                                }
                            )
                        }
                    ) {
                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") {
                                HomeScreen(navController, billViewModel)
                            }
                            dialog(
                                "deldialog/{id}",
                                arguments = listOf(
                                    navArgument("id") {
                                        type = NavType.LongType
                                    }
                                )
                            ) {
                                val id = it.arguments?.getLong("id") ?: -1L
                                DelComposeDialog(navController, id, billViewModel)
                            }
                            dialog(
                                "dialog?id={id},type={type},typename={typename},cost={cost}",
                                arguments = listOf(
                                    navArgument("id") {
                                        type = NavType.LongType
                                        defaultValue = -1L
                                    },
                                    navArgument("type") {
                                        type = NavType.IntType
                                        defaultValue = 0
                                    },
                                    navArgument("typename") {
                                        type = NavType.StringType
                                        defaultValue = "traffic"
                                    },
                                    navArgument("cost") {
                                        type = NavType.StringType
                                        defaultValue = ""
                                    }
                                )
                            ) {
                                val id = it.arguments?.getLong("id") ?: -1L
                                val type = it.arguments?.getInt("type") ?: 0
                                val typename = it.arguments?.getString("typename") ?: "traffic"
                                val cost = it.arguments?.getString("cost") ?: ""

                                val bill = Bills(
                                    id = id,
                                    type = type,
                                    typename = when (typename) {
                                        "traffic" -> BillTypeVals.BillType.traffic
                                        "food" -> BillTypeVals.BillType.food
                                        "money" -> BillTypeVals.BillType.money
                                        else -> BillTypeVals.BillType.traffic
                                    },
                                    cost = cost,
                                    time = "",
                                    favorite = false
                                )
                                ComposeDialog(navController, context, billViewModel, bill)
                            }
                        }
                    }
                }
            }
        }
    }
}