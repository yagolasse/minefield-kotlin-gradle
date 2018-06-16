package view

import model.Board
import model.BoardEvents
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main(args: Array<String>) {
    MainScreen()
}

class MainScreen: JFrame() {

    private val board = Board(16, 30, 30)
    private val boardPanel = BoardPanel(board)

    init {
        board.onEvent(this::showResult)
        add(boardPanel)

        setSize(640, 480)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Campo Minado"
        isVisible = true
    }

    private fun showResult(event: BoardEvents) {
        SwingUtilities.invokeLater {
            val message = when(event) {
                BoardEvents.WIN -> "Você ganhou!"
                BoardEvents.LOSS -> "Você perdeu!"
            }
            JOptionPane.showMessageDialog(this, message)
            board.reset()

            boardPanel.repaint()
            boardPanel.revalidate()
        }
    }
}