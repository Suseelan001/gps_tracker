package com.locationReminder.view



import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.locationReminder.R
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hex3b7ded
import com.locationReminder.ui.theme.InternBoldWithHex31394f18sp
import com.locationReminder.viewModel.AddFolderNameViewModel
import com.locationReminder.viewModel.AddImportedCategoryNameViewModel
import com.locationReminder.viewModel.SharedPreferenceVM

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddFolderNameScreen(
    navController: NavHostController,
    id: String, categoryName: String, isFrom: String,
    viewModel: AddFolderNameViewModel,
    sharedPreferenceVM: SharedPreferenceVM,
    addImportedCategoryNameViewModel: AddImportedCategoryNameViewModel
) {
    val focusManager = LocalFocusManager.current

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val successMessage by viewModel.successMessage.observeAsState("")

    val errorMessage by viewModel.errorMessage.observeAsState("")
    val context = LocalContext.current


    if (errorMessage == "Record already exist") {
        Toast.makeText(context, "Folder name already exist", Toast.LENGTH_LONG).show()
        viewModel.clearErrorMessage()

    }

    if (successMessage == "Record edited") {
        viewModel.clearSuccessMessage()
        navController.popBackStack()
    } else {
        if (successMessage == "Folder created") {
            Toast.makeText(context, "Folder created", Toast.LENGTH_SHORT).show()
            viewModel.clearSuccessMessage()
            navController.popBackStack()
        }

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
                    if (id.isNotEmpty()) {
                        Text(
                            text = "Edit Folder",
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Add Folder",
                            color = Color.White
                        )
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


            var folderName by remember { mutableStateOf("") }
            val focusManager = LocalFocusManager.current
            val context = LocalContext.current

            LaunchedEffect(categoryName) {
                if (categoryName.isNotEmpty()) {
                    folderName = categoryName
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Hex222227)
                    .padding(WindowInsets.systemBars.asPaddingValues())
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        focusManager.clearFocus()
                    }
            ) {


                CustomTextFieldWithIcon(
                    value = folderName,
                    onValueChange = { folderName = it },
                    hint = "Enter Folder Name",
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.folder),
                            contentDescription = "Folder Icon",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )



                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Hex3b7ded),
                    onClick = {
                        focusManager.clearFocus()
                        if (folderName.isEmpty()) {
                            Toast.makeText(context, "Enter folderName", Toast.LENGTH_SHORT).show()
                        } else {
                            if (isFrom == "ImportedMarker") {
                                if (id.isNotEmpty()) {
                                    val record =
                                        addImportedCategoryNameViewModel.getRecordById(id.toInt())
                                    val updatedRecord = record.copy(firstTimeImport = false, categoryName = folderName)
                                    addImportedCategoryNameViewModel.updateRecord(updatedRecord)
                                    navController.popBackStack()
                                }
                            } else {
                                if (id.isNotEmpty()) {
                                    viewModel.editCategory("eq.$id", folderName)
                                } else {
                                    viewModel.addCategoryList(
                                        folderName,
                                        sharedPreferenceVM.getUserId().toString(),
                                        sharedPreferenceVM.getUserName()
                                    )
                                }
                            }


                        }


                    },
                ) {
                    Text(text = "Save", style = InternBoldWithHex31394f18sp, color = Color.White)
                }


            }

        }
    }
}



