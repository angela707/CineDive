package com.adimovska.cinedive.presentation.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.adimovska.cinedive.domain.models.MediaType

@Composable
fun MediaDropDownItem(
    type: MediaType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenuItem(
        modifier = modifier,
        onClick = onClick,
        text = {
            Text(
                text = stringResource(type.nameId),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )
}