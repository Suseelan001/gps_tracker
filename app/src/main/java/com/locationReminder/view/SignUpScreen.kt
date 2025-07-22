package com.locationReminder.view


import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.ui.theme.InternBoldWithHex31394f18sp
import com.locationReminder.ui.theme.InternRegularWithHexa19da613sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHexeef26718sp
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.LoginVM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navHostController: NavHostController, loginVM: LoginVM) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userDetail by loginVM.userDetail.observeAsState()
    val isLoading by loginVM.loading.observeAsState(false)
    val errorMessage by loginVM.errorMessage.observeAsState()

    userDetail?.let {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Your account was created successfully", Toast.LENGTH_SHORT).show()
            navHostController.navigate(NavigationRoute.HOMESCREEN.path) {
                popUpTo(NavigationRoute.HOMESCREEN.path) {
                    inclusive = true
                }
            }
        }
    }


    errorMessage?.let {
        LaunchedEffect(Unit) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            loginVM.clearErrorMessage()
        }
    }


    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Sign Up",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Hex222227,
                    scrolledContainerColor = Hex222227
                ))
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Hex222227)
                .padding(top = paddingValues.calculateTopPadding())
                .padding(bottom = 50.dp)


        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.systemBars.asPaddingValues())
            ) {

                CustomTextFieldWithIcon(
                    value = userName,
                    onValueChange = { userName = it },
                    hint = "Name"
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextFieldWithIcon(
                    value = mobileNumber,
                    onValueChange = { mobileNumber = it },
                    hint = "Mobile Number"
                )

                Spacer(modifier = Modifier.height(20.dp))

                CustomTextFieldWithIcon(
                    value = email,
                    onValueChange = { email = it },
                    hint = "Email"
                )

                Spacer(modifier = Modifier.height(20.dp))

                PasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    hint = "Password"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Hexeef267),
                    onClick = {
                        val emailPattern = Patterns.EMAIL_ADDRESS

                        when {
                            userName.isEmpty() -> Toast.makeText(context, "Enter userName", Toast.LENGTH_SHORT).show()
                            mobileNumber.isEmpty() -> Toast.makeText(context, "Enter mobile number", Toast.LENGTH_SHORT).show()
                            email.isEmpty() -> Toast.makeText(context, "Enter email", Toast.LENGTH_SHORT).show()
                            !emailPattern.matcher(email).matches() -> Toast.makeText(context, "Enter a valid email", Toast.LENGTH_SHORT).show()
                            password.isEmpty() -> Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show()
                            else -> loginVM.callSignUp(userName, mobileNumber, email, password)
                        }
                    }
                ) {
                    Text(text = "Sign Up", style = InternBoldWithHex31394f18sp, color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 70.dp)
                ) {
                    Text(
                        text = "Already have an account?",
                        style = InternRegularWithHexa19da613sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sign In",
                        style = RobotoRegularWithHexHexeef26718sp.copy(fontSize = 13.sp),
                        modifier = Modifier.clickable { navHostController.popBackStack() }
                    )
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
                        color = Hexeef267
                    )
                }
            }
        }
    }


}



