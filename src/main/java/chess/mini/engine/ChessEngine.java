package chess.mini.engine;

import chess.mini.engine.core.AttackChecker;
import chess.mini.engine.core.Board;
import chess.mini.engine.core.CheckDetector;
import chess.mini.engine.core.MoveValidator;
import chess.mini.engine.data.Color;
import chess.mini.engine.data.Piece;
import chess.mini.engine.data.Square;

import java.util.List;

/**
 * Основной класс шахматного движка
 */
public class ChessEngine {
    private Board board;
    private Color sideToMove;

    public ChessEngine() {
        reset();
    }

    public ChessEngine(Board board, Color sideToMove) {
        this.board = board;
        this.sideToMove = sideToMove;
    }

    /**
     * Сбрасывает игру к начальной позиции
     */
    public void reset() {
        board = Board.initial();
        sideToMove = Color.WHITE;
    }

    /**
     * Получает текущую доску
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Получает сторону, которая должна ходить
     */
    public Color getSideToMove() {
        return sideToMove;
    }

    /**
     * Получает список легальных ходов для фигуры на указанной позиции
     */
    public List<Square> getLegalMoves(Square from) {
        Piece piece = board.getPiece(from.rank(), from.file());
        return MoveValidator.getLegalMoves(from, piece, sideToMove, board);
    }

    /**
     * Проверяет, находится ли король под шахом
     */
    public boolean inCheck() {
        return CheckDetector.inCheck(sideToMove, board);
    }

    /**
     * Проверяет, находится ли король под матом
     */
    public boolean isCheckmate() {
        return CheckDetector.isCheckmate(sideToMove, board);
    }

    /**
     * Проверяет, находится ли король под патом
     */
    public boolean isStalemate() {
        return CheckDetector.isStalemate(sideToMove, board);
    }

    /**
     * Выполняет ход
     */
    public void makeMove(Square from, Square to) {
        // Валидация
        if (!MoveValidator.isValidMove(from, to, sideToMove, board)) {
            throw new IllegalArgumentException("Недопустимый ход");
        }

        // Выполнение хода
        Piece piece = board.getPiece(from.rank(), from.file());
        board.setPiece(from.rank(), from.file(), null);
        board.setPiece(to.rank(), to.file(), piece);

        // Смена стороны
        sideToMove = (sideToMove == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    /**
     * Проверяет, атакуется ли указанная клетка
     */
    public boolean isSquareAttacked(Square target, Color byColor) {
        return AttackChecker.isSquareAttacked(target, byColor, board);
    }

    /**
     * Получает копию доски
     */
    public Board getBoardCopy() {
        Board copy = new Board();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board.getPiece(rank, file);
                if (piece != null) {
                    copy.setPiece(rank, file, new Piece(piece.type(), piece.color()));
                }
            }
        }
        return copy;
    }
}
