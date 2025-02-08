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
    val rotation by animateFloatAsState(if (isMenuOpen) -5f else 0f, label = "")
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
                        when {
                            dragAmount > 10 -> isMenuOpen = true
                            dragAmount < -10 -> isMenuOpen = false
                        }
                    }
                }
                .clickable {
                    if (isMenuOpen) isMenuOpen = false
                },
            contentAlignment = Alignment.Center
        ) {
            MainScreen(onMenuClick = { isMenuOpen = !isMenuOpen })
        }
    }
}

@Composable
fun MainScreen(
    onMenuClick: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp, top = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {onMenuClick()}) {
                    Icon(painter = painterResource(id = R.drawable.drawe), contentDescription = "Меню")
                }
                Row (verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 26.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.sparks),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text("Главная", fontSize = 36.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 24.dp))
                }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.bag),
                        contentDescription = null,
                    )
                }
            }
        }
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
            .padding(16.dp, top = 60.dp)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount < -10 -> onClose()
                    }
                }
            },
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

        DrawerItem("Профиль", onClick = onClose, R.drawable.profile_drawe)
        DrawerItem("Корзина", onClick = onClose, R.drawable.bag_drawe)
        DrawerItem("Избранное", onClick = onClose, R.drawable.favorite_drawe)
        DrawerItem("Заказы", onClick = onClose, R.drawable.orders_drawe)
        DrawerItem("Уведомления", onClick = onClose, R.drawable.notification_drawe_r)
        DrawerItem("Настройки", onClick = onClose, R.drawable.settings_drawe)

        HorizontalDivider(
            color = Color(0x3BF7F7F9),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row (verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    scope.launch {
                        preferencesManager.deleteRegistered()
                        navController.navigate("login") {
                            popUpTo("registration") { inclusive = true }
                        }
                    }
                }) {
            Icon(
                painter = painterResource(id = R.drawable.exit_drawe),
                contentDescription = "Выйти",
            )
            Text(
                "Выйти",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(14.dp)
            )
        }
    }
}

@Composable
fun DrawerItem(text: String, onClick: () -> Unit, image: Int) {
    Row (verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }) {
        Icon(
            painter = painterResource(id = image),
            contentDescription = text,
        )
        Text(
            text,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(14.dp)
        )
    }
}
