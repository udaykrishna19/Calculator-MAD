package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var display: EditText
    private var currentInput = StringBuilder()
    private var currentOperator: String? = null
    private var firstOperand: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Changed to activity_main

        display = findViewById(R.id.display)

        // Number buttons
        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        numberButtons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                appendNumber((it as Button).text.toString())
            }
        }

        // Operator buttons
        val operatorButtons = listOf(
            R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide
        )

        operatorButtons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                setOperator((it as Button).text.toString())
            }
        }

        // Other buttons
        findViewById<Button>(R.id.btnDecimal).setOnClickListener { appendDecimal() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { toggleSign() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { applyPercent() }
    }

    private fun appendNumber(number: String) {
        if (currentInput.isEmpty() && number == "0") return // Prevent leading zeros

        currentInput.append(number)
        updateDisplay()
    }

    private fun appendDecimal() {
        if (currentInput.isEmpty()) {
            currentInput.append("0.")
        } else if (!currentInput.contains(".")) {
            currentInput.append(".")
        }
        updateDisplay()
    }

    private fun clearAll() {
        currentInput.clear()
        firstOperand = null
        currentOperator = null
        updateDisplay()
    }

    private fun setOperator(operator: String) {
        if (currentInput.isNotEmpty()) {
            firstOperand = currentInput.toString().toDouble()
            currentOperator = operator
            currentInput.clear()
        } else if (firstOperand != null) {
            // Allow changing the operator without new input
            currentOperator = operator
        }
    }

    private fun calculateResult() {
        if (firstOperand != null && currentOperator != null && currentInput.isNotEmpty()) {
            val secondOperand = currentInput.toString().toDouble()
            val result = when (currentOperator) {
                "+" -> firstOperand!! + secondOperand
                "-" -> firstOperand!! - secondOperand
                "×" -> firstOperand!! * secondOperand
                "/" -> if (secondOperand != 0.0) firstOperand!! / secondOperand else Double.NaN
                else -> secondOperand
            }

            currentInput.clear()
            currentInput.append(if (result.isNaN()) "Error" else removeTrailingZeros(result))
            firstOperand = null
            currentOperator = null
            updateDisplay()
        }
    }

    private fun toggleSign() {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toString().toDouble() * -1
            currentInput.clear()
            currentInput.append(removeTrailingZeros(value))
            updateDisplay()
        }
    }

    private fun applyPercent() {
        if (currentInput.isNotEmpty()) {
            val value = currentInput.toString().toDouble() / 100
            currentInput.clear()
            currentInput.append(removeTrailingZeros(value))
            updateDisplay()
        }
    }

    private fun updateDisplay() {
        val text = if (currentInput.isEmpty()) "0" else currentInput.toString()
        val selection = display.selectionEnd
        display.setText(text)
        if (selection <= text.length) {
            display.setSelection(selection)
        }
    }

    private fun removeTrailingZeros(number: Double): String {
        return if (number % 1 == 0.0) {
            String.format("%.0f", number)
        } else {
            number.toString()
        }
    }
}