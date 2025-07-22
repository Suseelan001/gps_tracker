package com.locationReminder.view

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.locationReminder.reponseModel.ContactDetail
import com.locationReminder.reponseModel.ContactDetailLocal
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.ui.theme.RobotoMediumWithHexFFFFFF18sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHex80808016sp
import com.locationReminder.viewModel.AddContactViewModel
import com.locationReminder.viewModel.AddLocationViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddContactNumberScreen(
    navController: NavHostController,
    viewModel: AddContactViewModel,
    addLocationViewModel: AddLocationViewModel

) {
    val context = LocalContext.current
    val contacts by viewModel.deviceContacts.collectAsState()
    val savedContacts by viewModel.getAllRecord().observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    var permissionGranted by remember { mutableStateOf(false) }
    val selectedContacts = remember { mutableStateListOf<ContactDetailLocal>() }
    var showContactList by remember { mutableStateOf(false) }
    var hasLoadedInitially by remember { mutableStateOf(false) }
    var showDialogForContact by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val getAccountList by addLocationViewModel.getAllRecord().observeAsState(emptyList())
    val exitList = getAccountList.filter { it.entryType.equals("Exit", ignoreCase = true) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            viewModel.loadDeviceContacts()
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }



    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
            viewModel.getAllRecord()
            viewModel.loadDeviceContacts()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    LaunchedEffect(contacts, savedContacts) {
        val savedNumbers = savedContacts.map { it.mobileNumber }
        val matching = contacts.filter { it.mobileNumber in savedNumbers }

        selectedContacts.clear()
        selectedContacts.addAll(matching.distinctBy { it.mobileNumber })

        if (!hasLoadedInitially) {
            showContactList = false
            delay(500)
            showContactList = true
            hasLoadedInitially = true
        } else {
            showContactList = true
        }
    }


        if(savedContacts.isEmpty()){
            if (exitList.isNotEmpty()){
                showDialogForContact=true
            }
        }


    if (showDialog) {
        EditContact(
            onDismiss = {
                handleContactsUpdate(selectedContacts, viewModel, navController)
                showDialog = false
            },
            onConfirmLogout = {
                viewModel.enableNotificationsForExitType()
                handleContactsUpdate(selectedContacts, viewModel, navController)
                showDialog = false
            }
        )
    }


    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                focusManager.clearFocus()
            },
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Select Contact",
                        color = Color.White
                    )
                },
                actions = {
                    if (selectedContacts.isNotEmpty()) {
                        TextButton(onClick = {
                            if (showDialogForContact){
                                showDialog=true

                            }else{
                                handleContactsUpdate(selectedContacts, viewModel, navController)

                            }



                        }) {
                            if (hasLoadedInitially) {
                                Text("Add", color = Color.White)
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Hex222227,
                    scrolledContainerColor = Hex222227
                ),
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Hex222227)
                .padding(top = padding.calculateTopPadding())
                .padding(bottom = 50.dp)


        ) {
            if (permissionGranted) {
                if (showContactList) {
                    val listState = rememberLazyListState()

                    val filteredContacts = contacts
                        .filter {
                            it.name.contains(searchQuery, ignoreCase = true) ||
                                    it.mobileNumber.contains(searchQuery)
                        }
                        .sortedBy { it.name.lowercase() }

                    val groupedContacts = filteredContacts.groupBy {
                        it.name.firstOrNull()?.uppercaseChar() ?: '#'
                    }

                    Column(modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }) {
                            focusManager.clearFocus()
                        }) {


                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search by name or number", color = Color.Gray) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .onFocusChanged {
                                    if (!it.isFocused) focusManager.clearFocus()
                                },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedIndicatorColor = Color.Gray,
                                unfocusedIndicatorColor = Color.DarkGray,
                                cursorColor = Color.White,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color(0xFF2A2A2E),
                            )
                        )



                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            if (selectedContacts.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "${selectedContacts.size} contact${if (selectedContacts.size > 1) "s" else ""} selected",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }

                            groupedContacts.forEach { (initial, contactsForInitial) ->
                                stickyHeader {
                                    Text(
                                        text = initial.toString(),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(Hex222227)
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White
                                    )
                                }

                                items(contactsForInitial) { contact ->
                                    val isSelected =
                                        selectedContacts.any { it.mobileNumber == contact.mobileNumber }

                                    ContactCard(
                                        contact = contact,
                                        isSelected = isSelected,
                                        onClick = {
                                            if (isSelected) {
                                                selectedContacts.removeAll { it.mobileNumber == contact.mobileNumber }
                                                if (selectedContacts.isEmpty()) {
                                                    viewModel.clearUserDB()
                                                }
                                            } else {
                                                selectedContacts.add(contact)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Permission not granted", color = Color.White)
                }
            }
        }
    }
}
private fun handleContactsUpdate(
    selectedContacts: List<ContactDetailLocal>,
    viewModel: AddContactViewModel,
    navController: NavController
) {
    viewModel.clearUserDB()
    selectedContacts.forEach { contact ->
        viewModel.insertRecord(
            ContactDetail(
                name = contact.name,
                mobileNumber = contact.mobileNumber
            )
        )
    }
    navController.popBackStack()
}




@Composable
fun EditContact(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1A1D), // Updated dark background
        title = {
            Text(
                text = "Add",
                color = Color.White
            )
        },
        text = {
            Text(
                text = "Send message to all these contacts",
                color = Color.White
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmLogout) {
                Text("Yes", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No", color = Color.White)
            }
        }
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactCard(
    contact: ContactDetailLocal,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = {}
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Hexeef267 else Color(0xFF36374A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF546E7A), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.firstOrNull()?.uppercase() ?: "",
                    style = RobotoMediumWithHexFFFFFF18sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = contact.name,
                    style = RobotoMediumWithHexFFFFFF18sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = contact.mobileNumber,
                    style = RobotoRegularWithHexHex80808016sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

