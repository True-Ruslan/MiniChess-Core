package chess.mini.engine.core;

import chess.mini.engine.data.Color;
import chess.mini.engine.data.Piece;
import chess.mini.engine.data.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для генерации возможных ходов фигур
 */
public class PieceMoves {
    
    /**
     * Генерирует все возможные ходы для фигуры на указанной позиции
     */
    public static List<Square> generateMoves(Square from, Piece piece, Board board) {
        List<Square> moves = new ArrayList<>();
        
        switch (piece.type()) {
            case PAWN -> addPawnMoves(from, piece.color(), board, moves);
            case ROOK -> addRookMoves(from, piece.color(), board, moves);
            case BISHOP -> addBishopMoves(from, piece.color(), board, moves);
            case KNIGHT -> addKnightMoves(from, piece.color(), board, moves);
            case QUEEN -> addQueenMoves(from, piece.color(), board, moves);
            case KING -> addKingMoves(from, piece.color(), board, moves);
        }
        
        return moves;
    }
    
    private static void addPawnMoves(Square from, Color color, Board board, List<Square> legalMoves) {
        int direction = (color == Color.WHITE) ? 1 : -1;
        int startRank = (color == Color.WHITE) ? 1 : 6;

        // Ход вперед на 1 клетку
        int newRank = from.rank() + direction;
        if (newRank >= 0 && newRank < 8 && board.getPiece(newRank, from.file()) == null) {
            legalMoves.add(new Square(from.file(), newRank));

            // Первый ход на 2 клетки
            if (from.rank() == startRank) {
                int doubleRank = from.rank() + 2 * direction;
                if (doubleRank >= 0 && doubleRank < 8 && board.getPiece(doubleRank, from.file()) == null) {
                    legalMoves.add(new Square(from.file(), doubleRank));
                }
            }
        }

        // Взятие по диагонали
        for (int fileOffset : new int[]{-1, 1}) {
            int newFile = from.file() + fileOffset;
            int newRankCapture = from.rank() + direction;

            if (newFile >= 0 && newFile < 8 && newRankCapture >= 0 && newRankCapture < 8) {
                Piece targetPiece = board.getPiece(newRankCapture, newFile);
                if (targetPiece != null && targetPiece.color() != color) {
                    legalMoves.add(new Square(newFile, newRankCapture));
                }
            }
        }
    }

    private static void addRookMoves(Square from, Color color, Board board, List<Square> legalMoves) {
        // Горизонтальные и вертикальные ходы
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        addSlidingMoves(from, color, board, legalMoves, directions);
    }

    private static void addBishopMoves(Square from, Color color, Board board, List<Square> legalMoves) {
        // Диагональные ходы
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        addSlidingMoves(from, color, board, legalMoves, directions);
    }

    private static void addKnightMoves(Square from, Color color, Board board, List<Square> legalMoves) {
        int[][] knightMoves = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] move : knightMoves) {
            int newRank = from.rank() + move[0];
            int newFile = from.file() + move[1];

            if (newRank >= 0 && newRank < 8 && newFile >= 0 && newFile < 8) {
                Piece targetPiece = board.getPiece(newRank, newFile);
                if (targetPiece == null || targetPiece.color() != color) {
                    legalMoves.add(new Square(newFile, newRank));
                }
            }
        }
    }

    private static void addQueenMoves(Square from, Color color, Board board, List<Square> legalMoves) {
        // Комбинация ладьи и слона
        addRookMoves(from, color, board, legalMoves);
        addBishopMoves(from, color, board, legalMoves);
    }

    private static void addKingMoves(Square from, Color color, Board board, List<Square> legalMoves) {
        // Ходы на 1 клетку во всех направлениях
        for (int rankOffset = -1; rankOffset <= 1; rankOffset++) {
            for (int fileOffset = -1; fileOffset <= 1; fileOffset++) {
                if (rankOffset == 0 && fileOffset == 0) continue;

                int newRank = from.rank() + rankOffset;
                int newFile = from.file() + fileOffset;

                if (newRank >= 0 && newRank < 8 && newFile >= 0 && newFile < 8) {
                    Piece targetPiece = board.getPiece(newRank, newFile);
                    if (targetPiece == null || targetPiece.color() != color) {
                        legalMoves.add(new Square(newFile, newRank));
                    }
                }
            }
        }
    }

    private static void addSlidingMoves(Square from, Color color, Board board, List<Square> legalMoves, int[][] directions) {
        for (int[] direction : directions) {
            int rankOffset = direction[0];
            int fileOffset = direction[1];

            for (int step = 1; step < 8; step++) {
                int newRank = from.rank() + step * rankOffset;
                int newFile = from.file() + step * fileOffset;

                if (newRank < 0 || newRank >= 8 || newFile < 0 || newFile >= 8) {
                    break;
                }

                Piece targetPiece = board.getPiece(newRank, newFile);
                if (targetPiece == null) {
                    legalMoves.add(new Square(newFile, newRank));
                } else {
                    if (targetPiece.color() != color) {
                        legalMoves.add(new Square(newFile, newRank));
                    }
                    break;
                }
            }
        }
    }
}
