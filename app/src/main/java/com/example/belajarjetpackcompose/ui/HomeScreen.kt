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
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var numberPhone by remember { mutableStateOf("") }
    var isMarried by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var editUser by remember { mutableStateOf<UserModel?>(null) }

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
                is State.Success -> {
                    LazyColumn {
                        val users = (state as State.Success).listData
                        items(users.size) { index ->
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
                                        text = users[index].name,
                                    )
                                    Text(text = users[index].email)
                                    Text(text = users[index].numberPhone.toString())
                                    Text(text = users[index].isMarried.toString())
                                }


                                IconButton(onClick = {
                                    editUser = users[index]
                                }) {
                                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                                }
                                IconButton(onClick = {
                                    viewModel.onEvent(Event.RemoveData(users[index]))
                                }) {
                                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                                }
                            }
                        }
                    }
                }

                else -> {
                    Text("Unknown state")
                }



            }

            Spacer(modifier = Modifier.height(16.dp))

            if (editUser != null) {
                val userToEdit = editUser!!
                Button(onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && numberPhone.isNotBlank()) {
                        viewModel.onEvent(Event.EditData(userToEdit.copy(
                            name = name,
                            email = email,
                            numberPhone = numberPhone.toLong(),
                            isMarried = isMarried
                        )))
                        name = ""
                        email = ""
                        numberPhone = ""
                        isMarried = false
                        editUser = null
                    }
                }) {
                    Text("Save Changes")
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add New User") },
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
                            viewModel.onEvent(Event.AddData(newUser))
                            name = ""
                            email = ""
                            numberPhone = ""
                            isMarried = false
                            showDialog = false
                        }
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

}