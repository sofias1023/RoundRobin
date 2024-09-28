package com.example.roundrobin

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
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

        btnSimulate.setOnClickListener {
            val numProcesses = etNumProcesses.text.toString().toInt()
            val burstTimes =
                etBurstTimes.text.toString().split(",").map { it.trim().toInt() }.toMutableList()
            val quantum = etQuantum.text.toString().toInt()

            val waitingTimes = MutableList(numProcesses) { 0 }
            val turnaroundTimes = MutableList(numProcesses) { 0 }
            var currentTime = 0
            val remainingTimes = burstTimes.toMutableList()

            // Limpiar la tabla antes de cada simulación
            tblResults.removeViews(1, tblResults.childCount - 1)

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

    private fun roundRobin(numProcesses: Int, burstTimes: MutableList<Int>, quantum: Int): String {
        val waitingTimes = MutableList(numProcesses) { 0 }
        val turnaroundTimes = MutableList(numProcesses) { 0 }
        var currentTime = 0
        val remainingTimes = burstTimes.toMutableList()

        while (true) {
            var done = true

            for (i in 0 until numProcesses) {
                if (remainingTimes[i] > 0) {
                    done = false // Hay procesos pendientes

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
