package view

import model.Field
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

class MouseClickListener(
        private val field: Field,
        private val onLeftButtonClick: (Field) -> Unit,
        private val onRightButtonClick: (Field) -> Unit
) : MouseListener {

    override fun mousePressed(e: MouseEvent?) {
        when (e?.button) {
            MouseEvent.BUTTON1 -> onLeftButtonClick(field)
            MouseEvent.BUTTON3 -> onRightButtonClick(field) // may be BUTTON2
        }
    }

    override fun mouseClicked(e: MouseEvent?) {}
    override fun mouseEntered(e: MouseEvent?) {}
    override fun mouseExited(e: MouseEvent?) {}
    override fun mouseReleased(e: MouseEvent?) {}
}