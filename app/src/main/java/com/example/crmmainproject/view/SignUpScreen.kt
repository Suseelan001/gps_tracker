package com.example.crmmainproject.view


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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.crmmainproject.ui.theme.Hex31394f
import com.example.crmmainproject.ui.theme.Hex3b7ded
import com.example.crmmainproject.ui.theme.InternBoldWithHex31394f18sp
import com.example.crmmainproject.ui.theme.InternRegularWithHexa19da613sp


@Composable
fun SignUpScreen(navHostController: NavHostController) {
    var companyName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }

    SetStatusBarColor()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            value = companyName,
            onValueChange = { companyName = it },
            hint = "Type your Company Name",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Person",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
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
            onClick = { /* Handle Sign-In */ },
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


