package com.example.roundrobin

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FuncionalesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_funcionales)

        val etNumProcesses: EditText = findViewById(R.id.et_num_processes)
        val etBurstTimes: EditText = findViewById(R.id.et_burst_times)
        val etQuantum: EditText = findViewById(R.id.et_quantum)
        val btnSimulate: Button = findViewById(R.id.btn_simulate)
        val tblResults: TableLayout = findViewById(R.id.tbl_results)
        val btnSig: Button = findViewById(R.id.btnSiguiente)

        btnSig.setOnClickListener {
            val intent = Intent(this, CreditosActivity::class.java)
            startActivity(intent)
        }

        btnSimulate.setOnClickListener {
            // Validación de entradas
            val numProcessesText = etNumProcesses.text.toString()
            val burstTimesText = etBurstTimes.text.toString()
            val quantumText = etQuantum.text.toString()

            // Validación de número de procesos
            if (numProcessesText.isEmpty()) {
                showToast("Por favor ingresa el número de procesos")
                return@setOnClickListener
            }

            val numProcesses = try {
                numProcessesText.toInt()
            } catch (e: NumberFormatException) {
                showToast("El número de procesos debe ser un número válido")
                return@setOnClickListener
            }

            // Validación de tiempos de ráfaga
            if (burstTimesText.isEmpty()) {
                showToast("Por favor ingresa los tiempos de ráfaga (separados por comas)")
                return@setOnClickListener
            }

            val burstTimes = try {
                burstTimesText.split(",").map { it.trim().toInt() }.toMutableList()
            } catch (e: Exception) {
                showToast("Los tiempos de ráfaga deben ser números válidos separados por comas")
                return@setOnClickListener
            }

            // Validación de quantum
            if (quantumText.isEmpty()) {
                showToast("Por favor ingresa el quantum")
                return@setOnClickListener
            }

            val quantum = try {
                quantumText.toInt()
            } catch (e: NumberFormatException) {
                showToast("El quantum debe ser un número válido")
                return@setOnClickListener
            }

            // Validar que el número de procesos coincida con la cantidad de tiempos de ráfaga
            if (burstTimes.size != numProcesses) {
                showToast("El número de procesos no coincide con la cantidad de tiempos de ráfaga")
                return@setOnClickListener
            }

            // Se limpia la tabla antes de cada simulación
            tblResults.removeViews(1, tblResults.childCount - 1)

            // Simulación del algoritmo Round Robin
            val waitingTimes = MutableList(numProcesses) { 0 }
            val turnaroundTimes = MutableList(numProcesses) { 0 }
            var currentTime = 0
            val remainingTimes = burstTimes.toMutableList()

            while (true) {
                var done = true
                for (i in 0 until numProcesses) {
                    if (remainingTimes[i] > 0) {
                        done = false
                        if (remainingTimes[i] > quantum) {
                            currentTime += quantum
                            remainingTimes[i] -= quantum
                        } else {
                            currentTime += remainingTimes[i]
                            waitingTimes[i] = currentTime - burstTimes[i]
                            remainingTimes[i] = 0
                        }
                    }
                }

                if (done) break
            }

            for (i in 0 until numProcesses) {
                turnaroundTimes[i] = burstTimes[i] + waitingTimes[i]
            }

            // Poblamos la tabla con los datos de cada proceso
            for (i in 0 until numProcesses) {
                val tableRow = TableRow(this)

                val tvProcess = TextView(this)
                tvProcess.text = (i + 1).toString()
                tvProcess.gravity = Gravity.CENTER
                tableRow.addView(tvProcess)

                val tvWaitTime = TextView(this)
                tvWaitTime.text = waitingTimes[i].toString()
                tvWaitTime.gravity = Gravity.CENTER
                tableRow.addView(tvWaitTime)

                val tvTurnaroundTime = TextView(this)
                tvTurnaroundTime.text = turnaroundTimes[i].toString()
                tvTurnaroundTime.gravity = Gravity.CENTER
                tableRow.addView(tvTurnaroundTime)

                tblResults.addView(tableRow)
            }
        }
    }

    // Función para mostrar un mensaje Toast con el error
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun roundRobin(numProcesses: Int, burstTimes: MutableList<Int>, quantum: Int): String {
        val waitingTimes = MutableList(numProcesses) { 0 }
        val turnaroundTimes = MutableList(numProcesses) { 0 }
        var currentTime = 0
        val remainingTimes = burstTimes.toMutableList()

        while (true) {
            var done = true

            for (i in 0 until numProcesses) {
                if (remainingTimes[i] > 0) {
                    done = false // Si se encuentran procesos pendientes

                    if (remainingTimes[i] > quantum) {
                        currentTime += quantum
                        remainingTimes[i] -= quantum
                    } else {
                        currentTime += remainingTimes[i]
                        waitingTimes[i] = currentTime - burstTimes[i]
                        remainingTimes[i] = 0
                    }
                }
            }

            if (done) break
        }

        for (i in 0 until numProcesses) {
            turnaroundTimes[i] = burstTimes[i] + waitingTimes[i]
        }

        // Generar el resultado en forma de String
        var result = "Resultados:\n"

        for (i in 0 until numProcesses) {
            result += "Proceso ${i + 1}: Tiempo de espera = ${waitingTimes[i]}, Tiempo de retorno = ${turnaroundTimes[i]}\n"
        }
        return result
    }
}
