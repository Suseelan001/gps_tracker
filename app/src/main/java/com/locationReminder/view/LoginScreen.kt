package com.locationReminder.view


import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.locationReminder.ui.theme.Hex31394f
import com.locationReminder.ui.theme.Hex3b7ded
import com.locationReminder.ui.theme.Hexc0d1e1
import com.locationReminder.R
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.HexFFFFFF
import com.locationReminder.ui.theme.InternBoldWithHex31394f18sp
import com.locationReminder.ui.theme.InternRegularWithHexa19da613sp
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.LoginVM


@Composable
fun LoginScreen(navHostController: NavHostController,loginVM:LoginVM) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current


    val userDetail by loginVM.userDetail.observeAsState()
    val isLoading by loginVM.loading.observeAsState(false)
    val errorMessage by loginVM.errorMessage.observeAsState()

    userDetail?.let {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Your account has been logged in", Toast.LENGTH_SHORT).show()
            navHostController.popBackStack()
        }
    }

    if (isLoading) {
        println("CHECK_TAG_ISLOADING " + isLoading)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .background(HexFFFFFF)
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
                text = "Sign In",
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

        Text(
            text = "Give credentials to sign in your account",
            style = InternRegularWithHexa19da613sp,
            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomTextFieldWithIcon(
            value = email,
            onValueChange = { email = it },
            hint = "Type your email",
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


        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot Password?",
                style = InternRegularWithHexa19da613sp,
                modifier = Modifier
                    .clickable { }
                    .padding(end = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Hex3b7ded),
            onClick = {
                focusManager.clearFocus()
                val emailPattern = Patterns.EMAIL_ADDRESS
                if (email.isEmpty()) {
                    Toast.makeText(context, "Enter email", Toast.LENGTH_SHORT).show()
                } else if (!emailPattern.matcher(email).matches()) {
                    Toast.makeText(context, "Enter a valid email", Toast.LENGTH_SHORT).show()
                } else if (password.isEmpty()) {
                    Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show()
                } else {
                    loginVM.callLogin(email, password)
                }



            },
        ) {
            Text(text = "Sign In",  style = InternBoldWithHex31394f18sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp)
        ) {
            Text(
                text = "Don't have an account?",
                style = InternRegularWithHexa19da613sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(

                text = "Sign Up",
                style = InternBoldWithHex31394f18sp,
                fontSize = 13.sp,
                color = Hex3b7ded,
                modifier = Modifier.clickable {
                    navHostController.navigate(NavigationRoute.SIGNUPSCREEN.path) },

            )
        }
    }
}


@Composable
fun CustomTextFieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    val isMobileField = hint.contains("Mobile", ignoreCase = true)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
            .border(
                width = 2.dp,
                color = Hexc0d1e1,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .padding(15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                keyboardOptions = if (isMobileField) {
                    KeyboardOptions(keyboardType = KeyboardType.Number)
                } else {
                    KeyboardOptions.Default
                },
                textStyle = TextStyle(color = Color.White),
                visualTransformation =
                    VisualTransformation.None
                ,
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}


@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String = "Password"
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp)
            .border(
                width = 2.dp,
                color = Color(0xFFC0D1E1),
                shape = MaterialTheme.shapes.medium
            )
            .background(
                Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .padding(15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Password Icon",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.White),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            color = Color.Gray ,
                        )
                    }
                    innerTextField()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = if (passwordVisible) R.drawable.visibility else R.drawable.visibility_off),
                contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        passwordVisible = !passwordVisible
                    }
            )


        }
    }
}



