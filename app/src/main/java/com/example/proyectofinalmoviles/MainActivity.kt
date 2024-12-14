@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.proyectofinalmoviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.proyectofinalmoviles.ui.theme.ProyectoFinalMovilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancialCalculatorApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialCalculatorApp() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora Financiera", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        }
    ) { innerPadding ->
        FinancialCalculatorScreen(Modifier.padding(innerPadding))
    }
}

@Composable
fun FinancialCalculatorScreen(modifier: Modifier = Modifier) {
    var selectedCategory by remember { mutableStateOf("Productos") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Seleccione una categoría:",
            style = MaterialTheme.typography.titleMedium
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { selectedCategory = "Productos" }) {
                Text("Productos")
            }
            Button(onClick = { selectedCategory = "Empleador" }) {
                Text("Empleador")
            }
            Button(onClick = { selectedCategory = "Empleado" }) {
                Text("Empleado")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedCategory) {
            "Productos" -> ProductCalculationsScreen(onResult = { result = it })
            "Empleador" -> EmployerCalculationsScreen(onResult = { result = it })
            "Empleado" -> EmployeeCalculationsScreen(onResult = { result = it })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Resultado:", style = MaterialTheme.typography.titleMedium)
        Text(text = result, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ProductCalculationsScreen(onResult: (String) -> Unit) {
    var basePrice by remember { mutableStateOf("") }
    var cost by remember { mutableStateOf("") }
    var fixedCosts by remember { mutableStateOf("") }
    var salesPrice by remember { mutableStateOf("") }
    var investment by remember { mutableStateOf("") }
    var income by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth() // Asegura que la columna ocupe todo el ancho disponible
    ) {
        OutlinedTextField(
            value = basePrice,
            onValueChange = { basePrice = it },
            label = { Text("Precio base") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cost,
            onValueChange = { cost = it },
            label = { Text("Costo variable") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fixedCosts,
            onValueChange = { fixedCosts = it },
            label = { Text("Costos fijos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = salesPrice,
            onValueChange = { salesPrice = it },
            label = { Text("Precio de venta unitario") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = income,
            onValueChange = { income = it },
            label = { Text("Ingresos") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = investment,
            onValueChange = { investment = it },
            label = { Text("Inversión") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de calcular, asegúrate que este al final
        Button(onClick = {
            val priceWithIVA = basePrice.toFloatOrNull()?.times(1.19)
            val margin = if (salesPrice.isNotEmpty() && cost.isNotEmpty()) {
                val sp = salesPrice.toFloat()
                val c = cost.toFloat()
                ((sp - c) / sp) * 100
            } else null
            val equilibriumPoint = if (fixedCosts.isNotEmpty() && salesPrice.isNotEmpty() && cost.isNotEmpty()) {
                val fc = fixedCosts.toFloat()
                val sp = salesPrice.toFloat()
                val c = cost.toFloat()
                fc / (sp - c)
            } else null
            val roi = if (income.isNotEmpty() && investment.isNotEmpty()) {
                val inc = income.toFloat()
                val inv = investment.toFloat()
                ((inc - inv) / inv) * 100
            } else null

            onResult(
                """
                    Precio con IVA: ${priceWithIVA?.let { "%.2f".format(it) } ?: "N/A"}
                    Margen de Ganancia: ${margin?.let { "%.2f".format(it) } ?: "N/A"}%
                    Punto de Equilibrio: ${equilibriumPoint?.let { "%.2f".format(it) } ?: "N/A"} unidades
                    ROI: ${roi?.let { "%.2f".format(it) } ?: "N/A"}%
                """.trimIndent()
            )
        }) {
            Text("Calcular")
        }
    }
}


@Composable
fun EmployerCalculationsScreen(onResult: (String) -> Unit) {
    var baseSalary by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = baseSalary,
            onValueChange = { baseSalary = it },
            label = { Text("Salario base") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            val salary = baseSalary.toFloatOrNull()
            if (salary != null) {
                val parafiscales = salary * 0.09
                val seguridadSocial = salary * 0.205
                val prestaciones = salary * 0.2183
                val totalNomina = salary + parafiscales + seguridadSocial + prestaciones
                onResult(
                    """
                        Costo Total de Nómina: ${"%.2f".format(totalNomina)}
                        Aportes Parafiscales: ${"%.2f".format(parafiscales)}
                        Seguridad Social: ${"%.2f".format(seguridadSocial)}
                        Prestaciones Sociales: ${"%.2f".format(prestaciones)}
                    """.trimIndent()
                )
            } else {
                onResult("Por favor ingrese un salario válido.")
            }
        }) {
            Text("Calcular")
        }
    }
}

@Composable
fun EmployeeCalculationsScreen(onResult: (String) -> Unit) {
    var baseSalary by remember { mutableStateOf("") }
    var extraHours by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = baseSalary,
            onValueChange = { baseSalary = it },
            label = { Text("Salario base") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = extraHours,
            onValueChange = { extraHours = it },
            label = { Text("Horas extras diurnas") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(onClick = {
            val salary = baseSalary.toFloatOrNull()
            val hours = extraHours.toIntOrNull()
            if (salary != null) {
                val netSalary = salary - (salary * 0.04) - (salary * 0.04)
                val extraHourlyRate = (salary / 240) * 1.25 * (hours ?: 0)
                onResult(
                    """
                        Salario Neto: ${"%.2f".format(netSalary)}
                        Pago por Horas Extras: ${"%.2f".format(extraHourlyRate)}
                    """.trimIndent()
                )
            } else {
                onResult("Por favor ingrese un salario válido.")
            }
        }) {
            Text("Calcular")
        }
    }
}
