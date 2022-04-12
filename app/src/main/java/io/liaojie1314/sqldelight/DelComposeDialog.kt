package io.liaojie1314.sqldelight

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import io.liaojie1314.sqldelight.viewmodel.BillViewModel

@Composable
fun DelComposeDialog(navController: NavController, id: Long, billViewModel: BillViewModel) {
    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = { Text(text = "提示") },
        text = { Text(text = "要删除此信息吗？") },
        confirmButton = {
            TextButton(onClick = {
                billViewModel.deleteBillById(id)
                navController.popBackStack()
            }) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = { navController.popBackStack() }) {
                Text(text = "No")
            }
        }

    )
}