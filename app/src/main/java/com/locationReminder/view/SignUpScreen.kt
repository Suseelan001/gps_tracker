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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hex31394f
import com.locationReminder.ui.theme.Hex3b7ded
import com.locationReminder.ui.theme.InternBoldWithHex31394f18sp
import com.locationReminder.ui.theme.InternRegularWithHexa19da613sp
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.LoginVM


@Composable
fun SignUpScreen(navHostController: NavHostController,loginVM:LoginVM) {
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
            Toast.makeText(context, "Your account has been logged in", Toast.LENGTH_SHORT).show()
            navHostController.navigate(NavigationRoute.FOLLOWSUPSCREEN.path) {
                popUpTo(NavigationRoute.FOLLOWSUPSCREEN.path) { inclusive = true }
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp)
            )
        }
    }

    errorMessage?.let {
        LaunchedEffect(Unit) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            loginVM.clearErrorMessage()


        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Hex222227)
            .padding(
                WindowInsets.systemBars.asPaddingValues()
            ),
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(24.dp)
                    .clickable { navHostController.popBackStack() }
            )

            Text(
                text = "Create an account",
                style = InternBoldWithHex31394f18sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .height(1.dp)
                .background(Hex31394f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextFieldWithIcon(
            value = userName,
            onValueChange = { userName = it },
            hint = "Type your Username",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "userName",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextFieldWithIcon(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            hint = "Type your Mobile",
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
            onValueChange = { email = it },
            hint = "Type your Email",
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
            hint = "Enter your password"
        )




        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Hex3b7ded),
            onClick = {
                val emailPattern = Patterns.EMAIL_ADDRESS

                if (userName.isEmpty()) {
                    Toast.makeText(context, "Enter userName", Toast.LENGTH_SHORT).show()
                } else if (mobileNumber.isEmpty()) {
                    Toast.makeText(context, "Enter mobileNumber", Toast.LENGTH_SHORT).show()
                }else if (email.isEmpty()) {
                    Toast.makeText(context, "Enter email", Toast.LENGTH_SHORT).show()
                } else if (!emailPattern.matcher(email).matches()) {
                    Toast.makeText(context, "Enter a valid email", Toast.LENGTH_SHORT).show()
                } else if (password.isEmpty()) {
                    Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show()
                } else {
                    loginVM.callSignUp(userName,mobileNumber,email,password)
                }


            },
        ) {
            Text(text = "Sign Up", style = InternBoldWithHex31394f18sp,  color = Color.White)
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
                style = InternBoldWithHex31394f18sp,
                fontSize = 13.sp,
                color = Hex3b7ded,
                modifier = Modifier.clickable { navHostController.popBackStack()}
            )
        }
    }
}


