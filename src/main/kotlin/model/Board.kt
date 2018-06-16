package model

import java.util.*

enum class BoardEvents { WIN, LOSS }

class Board(val lineCount: Int, val columnCount: Int, private val mineCount: Int) {

    private val fields = ArrayList<ArrayList<Field>>()
    private val callbacks = ArrayList<(BoardEvents) -> Unit>()

    init {
        generateFields()
        associateNeighbors()
        raffleMines()
    }


    private fun generateFields() {
        for (line in 0 until lineCount) {
            fields.add(ArrayList())
            for (column in 0 until columnCount) {
                val newField = Field(line, column)
                newField.onEvent(this::verifyWonOrLoss)
                fields[line].add(newField)
            }
        }
    }

    private fun associateNeighbors() {
        forEachField { associateNeighbors(it) }
    }

    private fun associateNeighbors(field: Field) {
        val (lineNumber, columnNumber) = field
        val lines = arrayOf(lineNumber - 1, lineNumber, lineNumber + 1)
        val columns = arrayOf(columnNumber - 1, columnNumber, columnNumber + 1)

        lines.forEach { line ->
            columns.forEach { column ->
                val current = fields.getOrNull(line)?.getOrNull(column)
                current?.takeIf { field != it }?.let { field.addNeighbor(it) }
            }
        }
    }

    private fun isObjectiveAchieved(): Boolean {
        var playerWon = true
        forEachField { if (!it.isObjectiveAchieved) playerWon = false }
        return playerWon
    }

    private fun verifyWonOrLoss(field: Field, event: FieldEvents) {
        if (event == FieldEvents.EXPLODING) {
            callbacks.forEach { it(BoardEvents.LOSS) }
        } else if (isObjectiveAchieved()) {
            callbacks.forEach { it(BoardEvents.WIN) }
        }
    }

    private fun raffleMines() {
        val valueGenerator = Random()

        var chosenLine: Int
        var chosenColumn: Int
        var currentMineCount = 0

        while (currentMineCount < this.mineCount) {
            chosenLine = valueGenerator.nextInt(this.lineCount)
            chosenColumn = valueGenerator.nextInt(this.columnCount)

            val sortedField = fields[chosenLine][chosenColumn]
            if (sortedField.isSafe) {
                sortedField.mine()
                currentMineCount++
            }
        }
    }

    fun forEachField(callback: (Field) -> Unit) {
        fields.forEach { line -> line.forEach(callback) }
    }

    fun onEvent(callback: (BoardEvents) -> Unit) {
        callbacks.add(callback)
    }

    fun reset() {
        forEachField { it.reset() }
        raffleMines()
    }
}
