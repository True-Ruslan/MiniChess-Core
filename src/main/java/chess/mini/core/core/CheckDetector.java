package chess.mini.core.core;

import chess.mini.core.data.Color;
import chess.mini.core.data.Piece;
import chess.mini.core.data.PieceType;
import chess.mini.core.data.Square;

/**
 * Класс для обнаружения шаха и мата
 */
public class CheckDetector {
    
    /**
     * Проверяет, находится ли король заданного цвета под шахом
     */
    public static boolean inCheck(Color color, Board board) {
        // Находим короля заданного цвета
        Square kingSquare = findKing(color, board);
        if (kingSquare == null) {
            return false; // Король не найден
        }

        // Проверяем, атакуется ли клетка короля фигурами противника
        Color opponentColor = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        return AttackChecker.isSquareAttacked(kingSquare, opponentColor, board);
    }

    /**
     * Находит короля заданного цвета на доске
     */
    public static Square findKing(Color color, Board board) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board.getPiece(rank, file);
                if (piece != null && piece.type() == PieceType.KING && piece.color() == color) {
                    return new Square(file, rank);
                }
            }
        }
        return null;
    }

    /**
     * Проверяет, находится ли король под матом
     */
    public static boolean isCheckmate(Color color, Board board) {
        if (!inCheck(color, board)) {
            return false;
        }

        // Проверяем, есть ли легальные ходы
        return !hasLegalMoves(color, board);
    }

    /**
     * Проверяет, есть ли у стороны легальные ходы
     */
    private static boolean hasLegalMoves(Color color, Board board) {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board.getPiece(rank, file);
                if (piece != null && piece.color() == color) {
                    Square from = new Square(file, rank);
                    if (!MoveValidator.getLegalMoves(from, piece, color, board).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Проверяет, находится ли король под патом (нет легальных ходов, но нет шаха)
     */
    public static boolean isStalemate(Color color, Board board) {
        if (inCheck(color, board)) {
            return false;
        }

        return !hasLegalMoves(color, board);
    }
}
