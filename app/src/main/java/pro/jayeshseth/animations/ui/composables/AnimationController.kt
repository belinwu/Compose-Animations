package pro.jayeshseth.animations.ui.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import pro.jayeshseth.animations.util.AnimationControllerState

/**
 * A common animation controller for all item placement animations
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationController(
    state: AnimationControllerState,
    onStateUpdate: (AnimationControllerState) -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    roundToInt: Boolean = true
) {
    val animationSpecs = remember { mutableListOf("Tween", "Spring") }
    var expanded by remember { mutableStateOf(false) }
    var dampingRatioExpanded by remember { mutableStateOf(false) }
    var stiffnessExpanded by remember { mutableStateOf(false) }
    val updatedInitialValue = rememberUpdatedState(state.initialValue)
    val updatedTweenDuration = rememberUpdatedState(state.tweenDuration)
    val updatedInitialValueRange = rememberUpdatedState(state.initialValueRange)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .animateContentSize(tween(450))
    ) {
        content()
        Toggler(
            title = "Vibration Effect",
            checked = state.vibrationEffect,
            onCheckedChanged = { onStateUpdate(state.copy(vibrationEffect = it)) },
            modifier = Modifier.fillMaxWidth()
        )
        Toggler(
            title = "Shadow",
            checked = state.showShadow,
            onCheckedChanged = { onStateUpdate(state.copy(showShadow = it)) },
            modifier = Modifier.fillMaxWidth()
        )
        SliderTemplate(
            title = "Initial Value",
            value = updatedInitialValue.value,
            step = state.initialValueSteps,
            onValueChange = { onStateUpdate(state.copy(initialValue = it)) },
            valueRange = updatedInitialValueRange.value,
            roundToInt = roundToInt
        )
        SingleChoiceSegmentedButtonRow {
            animationSpecs.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index, animationSpecs.size),
                    selected = index == state.selectedIndex,
                    onClick = {
                        onStateUpdate(state.copy(selectedIndex = index))
                    }
                ) {
                    Text(label)
                }
            }
        }
        Crossfade(
            targetState = state.selectedIndex,
            animationSpec = tween(850, easing = LinearEasing),
            label = "control switcher"
        ) {
            when (it) {
                0 -> {
                    Column {
                        SliderTemplate(
                            title = "Tween Duration",
                            value = updatedTweenDuration.value.toFloat(),
                            step = 5f,
                            onValueChange = { duration -> onStateUpdate(state.copy(tweenDuration = duration.toInt())) },
                            valueRange = 0f..1000f
                        )

                        DropDownTemplate(
                            value = state.easing.second,
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            onDismissRequest = { expanded = false },
                            title = {
                                Text(
                                    text = "Easing",
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            },
                            content = {
                                state.easingList.forEach { easing ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                easing.second,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        },
                                        onClick = {
                                            onStateUpdate(state.copy(easing = easing))
                                            expanded = false
                                        },
                                        modifier = Modifier.clip(CircleShape),
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        )
                    }
                }

                1 -> {
                    Column {
                        DropDownTemplate(
                            value = state.dampingRatio.second,
                            expanded = dampingRatioExpanded,
                            onExpandedChange = { dampingRatioExpanded = it },
                            onDismissRequest = { dampingRatioExpanded = false },
                            title = {
                                Text(
                                    text = "Damping Ratio",
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            },
                            content = {
                                state.dampingRatioList.forEach { ratio ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                ratio.second,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        },
                                        onClick = {
                                            onStateUpdate(state.copy(dampingRatio = ratio))
                                            dampingRatioExpanded = false
                                        },
                                        modifier = Modifier.clip(CircleShape),
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }

                            }
                        )
                        DropDownTemplate(
                            value = state.stiffness.second,
                            expanded = stiffnessExpanded,
                            onExpandedChange = { stiffnessExpanded = it },
                            onDismissRequest = { stiffnessExpanded = false },
                            title = {
                                Text(
                                    text = "Stiffness",
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            },
                            content = {
                                state.stiffnessList.forEach { stiffness ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                stiffness.second,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        },
                                        onClick = {
                                            onStateUpdate(state.copy(stiffness = stiffness))
                                            stiffnessExpanded = false
                                        },
                                        modifier = Modifier.clip(CircleShape),
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}