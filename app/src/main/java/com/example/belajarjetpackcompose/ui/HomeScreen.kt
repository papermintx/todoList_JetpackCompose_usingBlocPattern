package com.example.belajarjetpackcompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belajarjetpackcompose.bloc.todo.viewmodel.Event
import com.example.belajarjetpackcompose.bloc.todo.viewmodel.State
import com.example.belajarjetpackcompose.bloc.todo.viewmodel.TodoViewModel
import com.example.belajarjetpackcompose.models.UserModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: TodoViewModel) {
    val state by viewModel.todoState.observeAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("To-Do List") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (state) {
                is State.Loading -> Text("Loading...")
                is State.Empty -> Text("No data available")
                is State.Error -> Text("Error: ${(state as State.Error).message}")
                is State.Success -> ListDataView(state = (state as State.Success).listData, viewModel = viewModel)
                else -> {
                    Text("Unknown state")
                }

            }

        }

        if (showDialog) {
            MyDialog(
                user = null,
                onDismiss = { showDialog = false },
                onConfirm = {
                    viewModel.onEvent(Event.AddData(it))
                    showDialog = false
                }
            )

        }
    }

}

@Composable
fun MyDialog(
//    modifier: Modifier = Modifier,
    user: UserModel?,
    onDismiss: () -> Unit,
    onConfirm: (UserModel) -> Unit
) {

    var name by remember { mutableStateOf(user?.name ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var numberPhone by remember { mutableStateOf(user?.numberPhone?.toString() ?: "") }
    var isMarried by remember { mutableStateOf(user?.isMarried ?: false) }

    if (user != null) {
        name = user.name
        email = user.email
        numberPhone = user.numberPhone.toString()
        isMarried = user.isMarried
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            if (user == null) Text("Add New User") else Text("Edit User")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
                OutlinedTextField(
                    value = numberPhone,
                    onValueChange = { numberPhone = it },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Checkbox(
                        checked = isMarried,
                        onCheckedChange = { isMarried = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Married")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (name.isNotBlank() && email.isNotBlank() && numberPhone.isNotBlank()) {
                    val newUser = UserModel(
                        name = name,
                        email = email,
                        numberPhone = numberPhone.toLong(),
                        isMarried = isMarried
                    )
                    onConfirm(newUser)
                    name = ""
                    email = ""
                    numberPhone = ""
                    isMarried = false
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

}

@Composable
fun ListDataView(modifier: Modifier = Modifier, state: List<UserModel>, viewModel: TodoViewModel ) {
    var showDialog by remember { mutableStateOf(false) }
    if(state.isEmpty()){
        Text("No data available")
    }
    LazyColumn {
        items(state.size) { index ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.DarkGray)
                    .padding(16.dp) ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(
                    text = "${index + 1}",
                    Modifier
                        .padding(16.dp)
                    ,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 25.sp

                )
                Column (
                    modifier = Modifier.weight(1f)

                ){

                    Text(
                        text = state[index].name,
                    )
                    Text(text = state[index].email)
                    Text(text = state[index].numberPhone.toString())
                    Text(text = state[index].isMarried.toString())
                }
                if (showDialog) {
                    MyDialog(
                        user = state[index],
                        onDismiss = { showDialog = false },
                        onConfirm = {
                            viewModel.onEvent(Event.EditData(it))
                            showDialog = false
                        }
                    )
                }

                IconButton(onClick = {
                    showDialog = true
                }) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = {
                    viewModel.onEvent(Event.RemoveData(state[index]))
                }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }

}