package pro.jayeshseth.animations.ui.screens

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import pro.jayeshseth.commoncomponents.SystemBarAwareThemedLazyColumn

private const val ITEM_COUNT = 1000000

@Composable
fun TrippyBlinders(modifier: Modifier = Modifier) {
    val lazyListState = rememberLazyListState()
    val animatedSpace by animateDpAsState(
        targetValue = if (lazyListState.isScrollInProgress) 4.dp else 0.dp,
        animationSpec = tween(1200, easing = EaseInOutBounce),
        label = "animated space"
    )
    val animatedPadding by animateDpAsState(
        targetValue = if (lazyListState.isScrollInProgress) 40.dp else 0.dp,
        animationSpec = tween(2800, easing = EaseInOutBounce),
        label = "animated padding"
    )
    val animatedShape by animateDpAsState(
        targetValue = if (lazyListState.isScrollInProgress) 50.dp else 0.dp,
        animationSpec = tween(2800, easing = EaseInOutBounce),
        label = "animated shape"
    )
    LaunchedEffect(Unit) {
        lazyListState.scrollToItem(ITEM_COUNT / 2)
    }
    SystemBarAwareThemedLazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(animatedSpace),
        modifier = modifier
            .background(Color.White),
        systemBarColor = Color.Transparent
    ) {
        items(ITEM_COUNT) {
            TrippyBlinderItem(
                index = it, shape = RoundedCornerShape(animatedShape),
                padding = animatedPadding
            )
        }
    }
}

@Composable
private fun TrippyBlinderItem(
    index: Int,
    shape: Shape,
    padding: Dp,
    modifier: Modifier = Modifier
) {
    val animatedProgress =
        remember { Animatable(initialValue = 360f) }
    val context = LocalContext.current
    val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)!!
    LaunchedEffect(index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        }
        animatedProgress.animateTo(
            targetValue = 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
        ),
        shape = shape,
        modifier = modifier
            .padding(horizontal = padding)
            .fillMaxWidth()
            .graphicsLayer(
                shadowElevation = animatedProgress.value,
                shape = shape,
                rotationX = animatedProgress.value,
            )
    ) { Spacer(Modifier.size(50.dp)) }
}