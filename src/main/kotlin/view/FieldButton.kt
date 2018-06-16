package view

import model.Field
import model.FieldEvents
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingUtilities

private val NORMAL_BG_COLOR = Color(184, 184, 184)
private val MARKED_BG_COLOR = Color(8, 179, 247)
private val EXPLODED_BG_COLOR = Color(189, 66, 68)
private val GREEN_TEXT = Color(0, 100, 0)

class FieldButton(private val field: Field) : JButton() {

    init {
        font = font.deriveFont(Font.BOLD)
        background = NORMAL_BG_COLOR
        isOpaque = true
        border = BorderFactory.createBevelBorder(0)
        addMouseListener(MouseClickListener(field, { it.open() }, { it.markModify() }))

        field.onEvent(this::applyStyle)
    }

    private fun applyStyle(field: Field, event: FieldEvents) {
        when(event) {
            FieldEvents.EXPLODING -> applyExplodedStyle()
            FieldEvents.OPENING -> applyOpenedStyle()
            FieldEvents.MARKING -> applyMarkedStyle()
            else -> applyDefaultStyle()
        }
        SwingUtilities.invokeLater {
            repaint()
            validate()
        }
    }

    private fun applyExplodedStyle() {
        background = EXPLODED_BG_COLOR
        text = "X"
    }

    private fun applyOpenedStyle() {
        background = NORMAL_BG_COLOR
        border = BorderFactory.createLineBorder(Color.GRAY)
        foreground = when(field.minedNeighborCount) {
            1 -> GREEN_TEXT
            2 -> Color.BLUE
            4, 5, 6 -> Color.RED
            else -> Color.PINK
        }
        text = if(field.minedNeighborCount > 0) field.minedNeighborCount.toString() else ""
    }

    private fun applyMarkedStyle() {
        background = MARKED_BG_COLOR
        foreground = Color.BLACK
        text = "M"
    }

    private fun applyDefaultStyle() {
        background = NORMAL_BG_COLOR
        border = BorderFactory.createBevelBorder(0)
        text = ""
    }
}

