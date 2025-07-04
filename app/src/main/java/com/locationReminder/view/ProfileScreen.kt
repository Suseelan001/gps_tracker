package com.locationReminder.view

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.sp
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hex31394f
import com.locationReminder.ui.theme.Hex3b7ded
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.ui.theme.InternBoldWithHex31394f18sp
import com.locationReminder.ui.theme.InternRegularWithHexa19da613sp
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.LoginVM


@Composable
fun ProfileScreen(navHostController: NavHostController, loginVM: LoginVM) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val isLoading by loginVM.loading.observeAsState(false)
    val errorMessage by loginVM.errorMessage.observeAsState()

    LaunchedEffect(Unit) {
        val userData=loginVM.getAllRecord()
        name= userData.username.toString()
        email= userData.userMail.toString()
        mobileNumber= userData.mobilenumber.toString()
        userId= userData.id.toString()
    }

    val successMessage by  loginVM.successMessage.observeAsState("")

    if (successMessage=="Login updated"){
        loginVM.clearSuccessMessage()
        navHostController.popBackStack()

    }



    errorMessage?.let {
        LaunchedEffect(Unit) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            loginVM.clearErrorMessage()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Hex222227)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                }
        ) {
            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Edit Profile",
                    style = InternBoldWithHex31394f18sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .background(Hex31394f)
            )

            Spacer(modifier = Modifier.height(32.dp))


            CustomTextFieldWithIcon(
                value = name,
                onValueChange = { name = it },
                hint = "Name",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Name Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(20.dp))

            CustomTextFieldWithIcon(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                hint = "Mobile Number",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = "Phone",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))


            CustomTextFieldWithIcon(
                value = email,
                onValueChange = { },
                hint = "Email",
                enabled = true,
                readOnly = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.MailOutline,
                        contentDescription = "Email Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )


            Spacer(modifier = Modifier.height(20.dp))

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                hint = "Password"
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Hexeef267),
                onClick = {
                    focusManager.clearFocus()
                    when {
                        email.isEmpty() -> Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show()
                        password.isEmpty() -> Toast.makeText(context, "Enter your password", Toast.LENGTH_SHORT).show()
                        name.isEmpty() -> Toast.makeText(context, "Enter your name", Toast.LENGTH_SHORT).show()
                        mobileNumber.isEmpty() -> Toast.makeText(context, "Enter your mobileNumber", Toast.LENGTH_SHORT).show()
                        else -> loginVM.updateUserLogin(userId,email, password,name,mobileNumber)
                    }
                }
            ) {
                Text(text ="Save", style = InternBoldWithHex31394f18sp, color = Color.Black)
            }

        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Hex3b7ded
                )
            }
        }
    }
}


