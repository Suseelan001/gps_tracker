package com.example.crmmainproject.view

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.core.view.WindowCompat
import com.example.crmmainproject.R
import com.example.crmmainproject.ui.theme.Hex31394f
import com.example.crmmainproject.ui.theme.Hex3b7ded
import com.example.crmmainproject.ui.theme.Hexc0d1e1
import com.example.crmmainproject.ui.theme.Hexf8fafc
import com.example.crmmainproject.ui.theme.Hexf9fafc
import com.example.crmmainproject.ui.theme.InternBoldWithHex31394f18sp
import com.example.crmmainproject.ui.theme.InternMediumWithHex3b7ded14sp
import com.example.crmmainproject.ui.theme.InternRegularWithHexa19da613sp
import com.example.crmmainproject.view.appNavigation.NavigationRoute


@Composable
fun LoginScreen(navHostController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    SetStatusBarColor()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                focusManager.clearFocus()
            }
    ) {
        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()

        ) {
        /*    Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(24.dp)
                    .clickable { navHostController.popBackStack() }
            )*/

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
        Card(
            modifier = Modifier.fillMaxWidth() ,
            shape = RoundedCornerShape(12.dp),
            colors =CardDefaults.cardColors(Hexf8fafc)
        ) {
                PasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    hint = "Enter your password"
                )

        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot Password?",
                style = InternMediumWithHex3b7ded14sp,
                modifier = Modifier
                    .clickable { /* Handle forgot password */ }
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
                navHostController.navigate(NavigationRoute.HOMESCREEN.path){
                popUpTo(NavigationRoute.LOGINHOME.path){
                    inclusive=true
                }}
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
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) Hexc0d1e1 else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                if (isFocused) Color.Transparent else Hexf9fafc,
                shape = MaterialTheme.shapes.medium
            )
            .padding(15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon() // Display the passed icon
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
                visualTransformation = if (isMobileField) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = InternRegularWithHexa19da613sp
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
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) Color(0xFFC0D1E1) else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .background(
                if (isFocused) Color.Transparent else Color(0xFFF9FAFC),
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
                    .weight(1f) // Fill available space
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style =  InternRegularWithHexa19da613sp ,
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


@Composable
fun SetStatusBarColor() {
    val context = LocalContext.current
    val activity = context as Activity
    val window = activity.window
    window.statusBarColor = Color.White.toArgb()

    WindowCompat.setDecorFitsSystemWindows(window, false)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val insetsController = window.insetsController
        insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}
