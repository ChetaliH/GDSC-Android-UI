package com.example.attendanceappui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.attendanceappui.ui.theme.AttendanceAppUITheme

// Data class to hold subject information
data class SubjectData(
    val name: String,
    val attendedClasses: Int,
    val totalClasses: Int
) {
    val attendancePercentage: Float
        get() = (attendedClasses.toFloat() / totalClasses) * 100
}

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
fun MyApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "subjectCard") {
            composable("subjectCard") {
                SubjectCardScreen(navController)
            }
            composable("updateScreen/{subjectName}") { backStackEntry ->
                val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
                UpdateScreen(navController, subjectName)
            }
            composable(
                route = "detailScreen/{subjectName}/{attended}/{total}",
                arguments = listOf(
                    navArgument("subjectName") { type = NavType.StringType },
                    navArgument("attended") { type = NavType.IntType },
                    navArgument("total") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val subjectName = backStackEntry.arguments?.getString("subjectName") ?: ""
                val attended = backStackEntry.arguments?.getInt("attended") ?: 0
                val total = backStackEntry.arguments?.getInt("total") ?: 0
                AttendanceScreen(
                    navController = navController,
                    subjectData = SubjectData(subjectName, attended, total)
                )
            }
        }
    }
}

@Composable
fun SubjectCardScreen(navController: NavController) {

    val subjects = listOf(
        SubjectData("Mathematics", 28, 35),
        SubjectData("Data Structures", 30, 40),
        SubjectData("Computer Architecture", 25, 30)
    )

    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Attendance Tracker",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )

        for (subject in subjects) {
            SubjectCard(
                subjectData = subject,
                onUpdateClick = {
                    navController.navigate("updateScreen/${subject.name}")
                },
                onViewDetailsClick = {
                    navController.navigate(
                        "detailScreen/${subject.name}/${subject.attendedClasses}/${subject.totalClasses}"
                    )
                }
            )
        }
    }
}

@Composable
fun SubjectCard(
    subjectData: SubjectData,
    modifier: Modifier = Modifier,
    onUpdateClick: () -> Unit,
    onViewDetailsClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = subjectData.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Attended: ${subjectData.attendedClasses}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Total: ${subjectData.totalClasses}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "%.1f%%".format(subjectData.attendancePercentage),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (subjectData.attendancePercentage >= 75f)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onUpdateClick) {
                    Text("Update")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onViewDetailsClick) {
                    Text("View Details")
                }
            }
        }
    }
}

@Composable
fun UpdateScreen(navController: NavController, subjectName: String) {
    var attendedClasses by remember { mutableStateOf("") }
    var totalClasses by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Update Attendance",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Subject: $subjectName",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = attendedClasses,
                    onValueChange = { attendedClasses = it },
                    label = { Text("Classes Attended") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = totalClasses,
                    onValueChange = { totalClasses = it },
                    label = { Text("Total Classes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("Cancel")
                    }
                    Button(onClick = {
                        // Here you would typically update the database
                        navController.popBackStack()
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceScreen(navController: NavController, subjectData: SubjectData) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = subjectData.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(200.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = subjectData.attendancePercentage / 100f,
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 16.dp,
                            color = when {
                                subjectData.attendancePercentage >= 75f -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.error
                            }
                        )
                        Text(
                            text = "%.1f%%".format(subjectData.attendancePercentage),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        AttendanceInfoColumn(
                            title = "Classes Attended",
                            value = subjectData.attendedClasses.toString()
                        )
                        AttendanceInfoColumn(
                            title = "Total Classes",
                            value = subjectData.totalClasses.toString()
                        )
                    }
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            ) {
                Text("Back")
            }
        }
    }
}

@Composable
private fun AttendanceInfoColumn(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AttendanceAppUITheme {
        val navController = rememberNavController()
        val sampleData = SubjectData("Mathematics", 28, 35)
        AttendanceScreen(navController, sampleData)
    }
}