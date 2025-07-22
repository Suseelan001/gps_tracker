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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.google.firebase.messaging.FirebaseMessaging
import com.locationReminder.ui.theme.Hexc0d1e1
import com.locationReminder.R
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.ui.theme.InternBoldWithHex31394f18sp
import com.locationReminder.ui.theme.InternMediumWithHexa0a0a014sp
import com.locationReminder.ui.theme.InternRegularWithHexa19da613sp
import com.locationReminder.ui.theme.RobotoRegularWithHexHexeef26718sp
import com.locationReminder.view.appNavigation.NavigationRoute
import com.locationReminder.viewModel.LoginVM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navHostController: NavHostController, loginVM: LoginVM) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val userDetail by loginVM.userDetail.observeAsState()
    val isLoading by loginVM.loading.observeAsState(false)
    val errorMessage by loginVM.errorMessage.observeAsState()
    val fcmToken = remember { mutableStateOf("")   }

    LaunchedEffect(Unit) {
        updateFCMDetails(fcmToken)
    }

    userDetail?.let {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Welcome back! You’re now signed in", Toast.LENGTH_SHORT).show()
            navHostController.popBackStack()
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
                        text = "Sign In",
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


        )  {

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

                CustomTextFieldWithIcon(
                    value = email,
                    onValueChange = { email = it },
                    hint = "Email"
                )

                Spacer(modifier = Modifier.height(30.dp))

                PasswordTextField(
                    value = password,
                    onValueChange = { password = it },
                    hint = "Password"
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Forgot Password?",
                        style = InternMediumWithHexa0a0a014sp,
                        modifier = Modifier
                            .clickable {
                                navHostController.navigate(NavigationRoute.CHANGEPASSWORD.path)
                            }
                            .padding(end = 24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Hexeef267),
                    onClick = {
                        focusManager.clearFocus()
                        val emailPattern = Patterns.EMAIL_ADDRESS
                        when {
                            email.isEmpty() -> Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show()
                            !emailPattern.matcher(email).matches() -> Toast.makeText(context, "Enter a valid email", Toast.LENGTH_SHORT).show()
                            password.isEmpty() -> Toast.makeText(context, "Enter your password", Toast.LENGTH_SHORT).show()
                            else -> loginVM.callLogin(email, password, fcmToken.value)
                        }
                    }
                ) {
                    Text(text = "Sign In", style = InternBoldWithHex31394f18sp, color = Color.Black)
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
                        style = RobotoRegularWithHexHexeef26718sp.copy(fontSize = 13.sp),
                        modifier = Modifier.clickable {
                            navHostController.navigate(NavigationRoute.SIGNUPSCREEN.path)
                        }
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


@Composable
fun CustomTextFieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    val isMobileField = hint.contains("Mobile", ignoreCase = true)
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (enabled && !readOnly) {
                    onValueChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            enabled = enabled,
            readOnly = readOnly,
            keyboardOptions = if (isMobileField) {
                KeyboardOptions(keyboardType = KeyboardType.Number)
            } else {
                KeyboardOptions.Default
            },
            interactionSource = interactionSource,
            textStyle = TextStyle(color = Color.White),
            cursorBrush = SolidColor(Color.White),
            visualTransformation = VisualTransformation.None,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = Hexc0d1e1
        )
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


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {


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
                            color = Color.Gray
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

        Spacer(modifier = Modifier.height(5.dp))

        HorizontalDivider(
             thickness = 1.dp,
            color = Color(0xFFC0D1E1)
        )
    }
}


fun updateFCMDetails(fcmToken: MutableState<String>) {
    FirebaseMessaging.getInstance().deleteToken()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseMessaging.getInstance().token
                    .addOnCompleteListener { newTask ->
                        if (!newTask.isSuccessful) {
                            println("CHECK_TAG_UN_isSuccessful "  )
                            return@addOnCompleteListener
                        }
                        fcmToken.value =newTask.result
                        println("CHECK_TAG_isSuccessful " +  fcmToken.value  )

                    }
            }
        }
}
