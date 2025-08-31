package chess.mini.core.core;

import chess.mini.core.data.Color;
import chess.mini.core.data.Piece;
import chess.mini.core.data.PieceType;
import chess.mini.core.data.Square;

/**
 * Класс для проверки атак фигур на клетки
 */
public class AttackChecker {
    
    /**
     * Проверяет, атакуется ли указанная клетка фигурами заданного цвета
     */
    public static boolean isSquareAttacked(Square target, Color byColor, Board board) {
        // Проверяем все фигуры заданного цвета
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                Piece piece = board.getPiece(rank, file);
                if (piece != null && piece.color() == byColor) {
                    if (canPieceAttackSquare(new Square(file, rank), target, piece.type(), piece.color(), board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Проверяет, может ли фигура заданного типа атаковать указанную клетку
     */
    public static boolean canPieceAttackSquare(Square from, Square target, PieceType pieceType, Color color, Board board) {
        return switch (pieceType) {
            case PAWN -> canPawnAttackSquare(from, target, color);
            case ROOK -> canRookAttackSquare(from, target, board);
            case BISHOP -> canBishopAttackSquare(from, target, board);
            case KNIGHT -> canKnightAttackSquare(from, target);
            case QUEEN -> canQueenAttackSquare(from, target, board);
            case KING -> canKingAttackSquare(from, target);
        };
    }

    private static boolean canPawnAttackSquare(Square from, Square target, Color color) {
        int direction = (color == Color.WHITE) ? 1 : -1;

        // Проверяем диагональные атаки пешки
        for (int fileOffset : new int[]{-1, 1}) {
            int newFile = from.file() + fileOffset;
            int newRank = from.rank() + direction;

            if (newFile == target.file() && newRank == target.rank()) {
                return true;
            }
        }
        return false;
    }

    private static boolean canRookAttackSquare(Square from, Square target, Board board) {
        // Проверяем горизонтальные и вертикальные линии
        if (from.file() != target.file() && from.rank() != target.rank()) {
            return false;
        }

        return isPathClear(from, target, board);
    }

    private static boolean canBishopAttackSquare(Square from, Square target, Board board) {
        // Проверяем диагональные линии
        if (Math.abs(from.file() - target.file()) != Math.abs(from.rank() - target.rank())) {
            return false;
        }

        return isPathClear(from, target, board);
    }

    private static boolean canKnightAttackSquare(Square from, Square target) {
        int rankDiff = Math.abs(from.rank() - target.rank());
        int fileDiff = Math.abs(from.file() - target.file());
        return (rankDiff == 2 && fileDiff == 1) || (rankDiff == 1 && fileDiff == 2);
    }

    private static boolean canQueenAttackSquare(Square from, Square target, Board board) {
        // Ферзь может ходить как ладья или слон
        return canRookAttackSquare(from, target, board) || canBishopAttackSquare(from, target, board);
    }

    private static boolean canKingAttackSquare(Square from, Square target) {
        int rankDiff = Math.abs(from.rank() - target.rank());
        int fileDiff = Math.abs(from.file() - target.file());
        return rankDiff <= 1 && fileDiff <= 1;
    }

    /**
     * Проверяет, свободен ли путь между двумя клетками
     */
    private static boolean isPathClear(Square from, Square to, Board board) {
        int rankStep = Integer.compare(to.rank(), from.rank());
        int fileStep = Integer.compare(to.file(), from.file());

        int currentRank = from.rank() + rankStep;
        int currentFile = from.file() + fileStep;

        while (currentRank != to.rank() || currentFile != to.file()) {
            if (board.getPiece(currentRank, currentFile) != null) {
                return false; // Путь заблокирован
            }
            currentRank += rankStep;
            currentFile += fileStep;
        }

        return true;
    }
}
