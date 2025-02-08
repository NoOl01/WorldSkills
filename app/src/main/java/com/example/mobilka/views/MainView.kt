package com.example.mobilka.views

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mobilka.R
import com.example.mobilka.data.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun ManiView(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    var isMenuOpen by remember { mutableStateOf(false) }

    val offsetX by animateFloatAsState(if (isMenuOpen) 180f else 0f, label = "")
    val rotation by animateFloatAsState(if (isMenuOpen) -10f else 0f, label = "")
    val scale by animateFloatAsState(if (isMenuOpen) 0.6f else 1f, label = "")
    val cornerRadius by animateDpAsState(
        targetValue = if (isMenuOpen) 40.dp else 0.dp, label = ""
    )
    val shadowSize by animateDpAsState(
        targetValue = if (isMenuOpen) 20.dp else 0.dp, label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        DrawerContent(navController, scope, preferencesManager, onClose = { isMenuOpen = false })

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = offsetX.dp)
                .rotate(rotation)
                .scale(scale)
                .shadow(shadowSize, shape = RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        isMenuOpen = dragAmount > 10
                    }
                }
                .clickable {
                    if (isMenuOpen) isMenuOpen = false
                },
            contentAlignment = Alignment.Center
        ) {
            MainScreen(navController, scope, preferencesManager)
        }
    }
}

@Composable
fun MainScreen(
    navController: NavController,
    scope: CoroutineScope,
    preferencesManager: PreferencesManager,
){
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ){
        Text("Main")
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    scope: CoroutineScope,
    preferencesManager: PreferencesManager,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF42A5F5))
            .padding(16.dp, top = 60.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.height(16.dp))
        Text("Эмануэль Кверти", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(Modifier.height(20.dp))

        DrawerItem("Профиль", onClick = onClose)
        DrawerItem("Корзина", onClick = onClose)
        DrawerItem("Избранное", onClick = onClose)
        DrawerItem("Заказы", onClick = onClose)
        DrawerItem("Уведомления", onClick = onClose)
        DrawerItem("Настройки", onClick = onClose)

        Spacer(Modifier.weight(1f))

        Text(
            "Выйти",
            fontSize = 18.sp,
            color = Color.Red,
            modifier = Modifier
                .padding(16.dp, bottom = 20.dp)
                .clickable {
                scope.launch {
                    preferencesManager.deleteRegistered()
                    navController.navigate("login") {
                        popUpTo("registration") { inclusive = true }
                    }
                }
            }
        )
    }
}

@Composable
fun DrawerItem(text: String, onClick: () -> Unit) {
    Text(
        text,
        fontSize = 18.sp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    )
}
