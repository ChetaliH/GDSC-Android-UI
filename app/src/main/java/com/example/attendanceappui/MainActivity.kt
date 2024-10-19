package com.example.attendanceappui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.attendanceappui.ui.theme.AttendanceAppUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendanceAppUITheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(24.dp)) {
        Text(text = "Hello", modifier = modifier)
        Text(text = "$name,", modifier = modifier)
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // NavHost for navigation between screens
    NavHost(navController = navController, startDestination = "subjectCard") {
        composable("subjectCard") {
            SubjectCardScreen(navController)
        }
        composable("updateScreen") {
            UpdateScreen(navController)
        }
    }

    Surface(modifier = modifier, color = MaterialTheme.colorScheme.background) {
        // The NavHost will handle displaying the correct screen
    }
}

@Composable
fun SubjectCardScreen(navController: NavController) {
    val subjects = listOf("Maths", "DSA", "COA")
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        for (subject in subjects) {
            SubjectCard(
                subName = subject,
                attended = 28,
                total = 56,
                percentage = 78f,
                onUpdateClick = {
                    navController.navigate("updateScreen") // Navigate to UpdateScreen
                }
            )
        }
    }
}

@Composable
fun SubjectCard(
    subName: String,
    attended: Int,
    total: Int,
    percentage: Float,
    modifier: Modifier = Modifier,
    onUpdateClick: () -> Unit // Callback for button click
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = subName,
                style = MaterialTheme.typography.headlineSmall
            )


            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Attended: $attended",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Total: $total",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Percentage: ${"%.2f".format(percentage)}%",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }
            ElevatedButton(onClick = onUpdateClick) { // Use the callback to navigate
                Text("Update")
            }
        }
    }
}

@Composable
fun UpdateScreen(navController: NavController) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .size(width = 400.dp, height = 400.dp)
                    .padding(16.dp) // Add padding to the card
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Attendance Info",
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    // Row for Name of Subject and TextField
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Name of the Subject:",
                            modifier = Modifier.weight(1f)
                        )
                        TextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row for Total Credit and TextField
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Credit:",
                            modifier = Modifier.weight(1f)
                        )
                        TextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row for Number of Classes Attended and TextField
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Number of Classes Attended:",
                            modifier = Modifier.weight(1f)
                        )
                        TextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { /* Do something */ }) {
                    Text("Submit")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AttendanceAppUITheme {
       // val navController = rememberNavController()
        //UpdateScreen(navController = navController)

        MyApp()
    }
}
