@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.datossinmvvm

import androidx.compose.foundation.layout.*
import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.room.Room
import androidx.compose.material3.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier

@Composable
fun ScreenUser() {
    val context = LocalContext.current
    var db: UserDatabase
    var id        by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var dataUser  = remember { mutableStateOf("") }

    db = crearDatabase(context)

    val dao = db.userDao()

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Management") },
                actions = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val user = User(firstName = firstName, lastName = lastName)
                                AgregarUsuario(user, dao)
                                firstName = ""
                                lastName = ""
                            }
                        }
                    ) {
                        Text("Agregar Usuario")
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                dataUser = getUser(dao)
                            }
                        }
                    ) {
                        Text("Listar Usuarios")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(50.dp))
                TextField(
                    value = id,
                    onValueChange = { id = it },
                    label = { Text("UID (solo lectura)") },
                    readOnly = true,
                    singleLine = true
                )
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name: ") },
                    singleLine = true
                )
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name: ") },
                    singleLine = true
                )
                Spacer(Modifier.height(16.dp))
                Text(text = dataUser, fontSize = 16.sp)
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            EliminarUltimoDato(dao = dao)
                        }
                        firstName = ""
                        lastName = ""
                    }
                ) {
                    Text("Eliminar Último Daot", fontSize = 16.sp)
                }
            }
        }
    )
}

fun getUser(dao: UserDao): MutableState<String> {

    return TODO("Provide the return value")
}

fun Text(text: MutableState<String>, fontSize: TextUnit) {
    TODO("Not yet implemented")
}


@Composable
fun crearDatabase(context: Context): UserDatabase {
    return Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_db"
    ).build()
}

suspend fun getUsers(dao:UserDao): String {
    var rpta: String = ""
    //LaunchedEffect(Unit) {
    val users = dao.getAll()
    users.forEach { user ->
        val fila = user.firstName + " - " + user.lastName + "\n"
        rpta += fila
    }
    //}
    return rpta
}

suspend fun AgregarUsuario(user: User, dao:UserDao): Unit {
    //LaunchedEffect(Unit) {
    try {
        dao.insert(user)
    }
    catch (e: Exception) {
        Log.e("User","Error: insert: ${e.message}")
    }
    //}
}

//acá hice la modificación
suspend fun EliminarUltimoDato(dao: UserDao): Unit {
    try {
        val lastUser = dao.getLastUser()
        if (lastUser != null) {
            dao.delete(lastUser)
        } else {
            Log.e("User", "No hay usuarios para eliminar")
        }
    } catch (e: Exception) {
        Log.e("User", "Error: delete: ${e.message}")
    }
}