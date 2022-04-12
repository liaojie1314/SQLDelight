package io.liaojie1314.sqldelight

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.liaojie1314.datastorage.db.Bills
import io.liaojie1314.sqldelight.model.BillTypeVals
import io.liaojie1314.sqldelight.viewmodel.BillViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnrememberedMutableState")
@Composable
fun ComposeDialog(
    navController: NavController,
    context: Context,
    billViewModel: BillViewModel,
    bill: Bills
) {
    val radioOptions = listOf(
        BillTypeVals.BillType.traffic,
        BillTypeVals.BillType.food,
        BillTypeVals.BillType.money
    )
    val cost = remember {
        mutableStateOf(bill.cost)
    }
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(radioOptions[bill.type])
    }
    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "创建账单",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "类型：")
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        radioOptions.forEach { text ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = text == selectedOption,
                                        onClick = { onOptionSelected(text) }
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = text == selectedOption,
                                    onClick = { onOptionSelected(text) })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = when (text) {
                                        BillTypeVals.BillType.traffic -> "出行"
                                        BillTypeVals.BillType.food -> "餐饮"
                                        BillTypeVals.BillType.money -> "理财"
                                        else -> "出行"
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "费用：")
                    OutlinedTextField(
                        singleLine = true,
                        value = cost.value,
                        onValueChange = { cost.value = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(

                        )
                    )
                }
            }
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    shape = RoundedCornerShape(100.dp),
                    border = BorderStroke(1.dp, color = Color(0xFF1BD184)),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color(0xFF1BD184)
                    ),
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        "取消",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        if (cost.value.isEmpty()) {
                            Toast.makeText(context, "请填写费用", Toast.LENGTH_SHORT).show()
                        } else {
                            val data = SimpleDateFormat("HH:mm")
                            val strData = data.format(Date())
                            if (bill.id == -1L) {
                                billViewModel.addBill(
                                    id = null,
                                    type = radioOptions.indexOf(selectedOption),
                                    typename = selectedOption,
                                    cost = cost.value,
                                    time = strData,
                                    favorite = bill.favorite
                                )
                            } else {
                                billViewModel.updateBill(
                                    id = bill.id,
                                    type = radioOptions.indexOf(selectedOption),
                                    typename = selectedOption,
                                    cost = cost.value,
                                    time = strData
                                )
                            }

                            navController.popBackStack()
                        }
                    },
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF1BD184),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "确定",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}