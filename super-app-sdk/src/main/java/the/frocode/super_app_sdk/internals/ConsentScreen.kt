package the.frocode.super_app_sdk.internals
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConsentScreen(
    onCancel: () -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF3F4F6)), // Gray background
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Content Section
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to SuperApp",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Would you like to sign in using Fusion? Your name will be shared.",
                fontSize = 18.sp,
                color = Color(0xFF6B7280), // Gray text
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Buttons Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onCancel() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f).padding(end = 8.dp).background(Color(0xFF9CA3AF))
            ) {
                Text("Cancel")
            }
            Button(
                onClick = { onLogin() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f).padding(start = 8.dp).background(Color(0xFF1F2937))
            ) {
                Text("Login")
            }
        }
    }
}
