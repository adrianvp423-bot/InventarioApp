package com.example.inventarioapp.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TurquoisePrimaryDark,
    onPrimary = OnTurquoisePrimaryDark,
    primaryContainer = TurquoiseContainerDark,
    onPrimaryContainer = OnTurquoiseContainerDark,
    secondary = EmeraldSecondaryDark,
    onSecondary = OnEmeraldSecondaryDark,
    secondaryContainer = MintSageContainerDark,
    onSecondaryContainer = OnMintSageContainerDark,
    tertiary = AquaTertiaryDark,
    onTertiary = OnAquaTertiaryDark,
    tertiaryContainer = AquaContainerDark,
    onTertiaryContainer = OnAquaContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark
)

private val LightColorScheme = lightColorScheme(
    primary = TurquoisePrimary,
    onPrimary = OnTurquoisePrimary,
    primaryContainer = TurquoiseContainer,
    onPrimaryContainer = OnTurquoiseContainer,
    secondary = EmeraldSecondary,
    onSecondary = OnEmeraldSecondary,
    secondaryContainer = MintSageContainer,
    onSecondaryContainer = OnMintSageContainer,
    tertiary = AquaTertiary,
    onTertiary = OnAquaTertiary,
    tertiaryContainer = AquaContainer,
    onTertiaryContainer = OnAquaContainer,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight
)

@Composable
fun InventarioAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
