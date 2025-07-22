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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavHostController
import com.locationReminder.ui.theme.Hex222227
import com.locationReminder.ui.theme.Hexeef267
import com.locationReminder.ui.theme.InternBoldWithHex31394f18sp
import com.locationReminder.viewModel.LoginVM



 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun ChangePassword(navHostController: NavHostController, loginVM: LoginVM) {
     var email by remember { mutableStateOf("") }
     var mobile by remember { mutableStateOf("") }
     var password by remember { mutableStateOf("") }

     val focusManager = LocalFocusManager.current
     val context = LocalContext.current

     val userDetail by loginVM.userDetail.observeAsState()
     val isLoading by loginVM.loading.observeAsState(false)
     val errorMessage by loginVM.errorMessage.observeAsState()

     userDetail?.let {
         LaunchedEffect(Unit) {
             Toast.makeText(context, "Your password has been updated successfully", Toast.LENGTH_SHORT).show()
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
                         text = "Forgot Password",
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


         )
         {

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

                 Spacer(modifier = Modifier.height(20.dp))

                 CustomTextFieldWithIcon(
                     value = mobile,
                     onValueChange = { mobile = it },
                     hint = "Mobile Number"
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
                         val emailPattern = Patterns.EMAIL_ADDRESS
                         when {
                             email.isEmpty() -> Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT).show()
                             !emailPattern.matcher(email).matches() -> Toast.makeText(context, "Enter a valid email", Toast.LENGTH_SHORT).show()
                             mobile.isEmpty() -> Toast.makeText(context, "Enter mobile number", Toast.LENGTH_SHORT).show()
                             password.isEmpty() -> Toast.makeText(context, "Enter password number", Toast.LENGTH_SHORT).show()
                             else -> loginVM.updatePassword("eq.$email", "eq.$mobile",password)
                         }
                     }
                 ) {
                     Text(text ="Update", style = InternBoldWithHex31394f18sp, color = Color.Black)
                 }

                 Spacer(modifier = Modifier.height(16.dp))

             }

             if (isLoading) {
                 Box(
                     modifier = Modifier
                         .fillMaxSize()
                         .background(Color.Transparent)
                         .pointerInput(Unit) {
                             awaitPointerEventScope {
                                 while (true) {
                                     awaitPointerEvent()
                                 }
                             }
                         },
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

