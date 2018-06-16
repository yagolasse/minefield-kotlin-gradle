package model

enum class FieldEvents { OPENING, MARKING, CLEARING, EXPLODING, RESETTING }

data class Field(val line: Int, val column: Int) {

    private val neighbors = ArrayList<Field>()
    private val callbacks = ArrayList<(Field, FieldEvents) -> Unit>()

    var isMarked = false
    var isOpen = false
    var isMined = false

    //read only
    val isClear get() = !isMarked
    val isClosed get() = !isOpen
    val isSafe get() = !isMined
    val isObjectiveAchieved get() = isSafe && isOpen || isMined && isMarked
    val minedNeighborCount get() = neighbors.filter { it.isMined }.size
    val isNeighborhoodSafe get() = neighbors.map { it.isSafe }.reduce { result, safe -> result && safe }

    fun addNeighbor(neighbor: Field) {
        neighbors.add(neighbor)
    }

    fun onEvent(callback: (Field, FieldEvents) -> Unit) {
        callbacks.add(callback)
    }

    fun open() {
        if (isClosed) {
            isOpen = true
            if (isMined) {
                callbacks.forEach { it(this, FieldEvents.EXPLODING) }
            } else {
                callbacks.forEach { it(this, FieldEvents.OPENING) }
                neighbors.filter { it.isClosed && it.isSafe && isNeighborhoodSafe }.forEach { it.open() }
            }
        }
    }

    fun markModify() {
        if(isClosed) {
            isMarked = !isMarked
            val event = if(isMarked) FieldEvents.MARKING else FieldEvents.CLEARING
            callbacks.forEach { it(this, event) }
        }
    }

    fun mine() {
        isMined = true
    }

    fun reset() {
        isOpen = false
        isMined = false
        isMarked = false
        callbacks.forEach { it(this, FieldEvents.RESETTING) }
    }

}

