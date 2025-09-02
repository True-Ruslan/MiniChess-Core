package chess.mini.engine.core;

import chess.mini.engine.data.Color;
import chess.mini.engine.data.Piece;
import chess.mini.engine.data.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для валидации ходов
 */
public class MoveValidator {
    
    /**
     * Получает список легальных ходов для фигуры
     */
    public static List<Square> getLegalMoves(Square from, Piece piece, Color sideToMove, Board board) {
        List<Square> legalMoves = new ArrayList<>();
        
        if (piece == null || piece.color() != sideToMove) {
            return legalMoves;
        }

        // Получаем все возможные ходы для фигуры
        List<Square> allMoves = PieceMoves.generateMoves(from, piece, board);

        // Фильтруем ходы, оставляющие короля под боем
        for (Square move : allMoves) {
            if (!wouldMoveLeaveKingInCheck(from, move, sideToMove, board)) {
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }

    /**
     * Проверяет, оставляет ли ход короля под боем
     */
    public static boolean wouldMoveLeaveKingInCheck(Square from, Square to, Color sideToMove, Board board) {
        // Сохраняем текущее состояние
        Piece fromPiece = board.getPiece(from.rank(), from.file());
        Piece toPiece = board.getPiece(to.rank(), to.file());

        // Виртуально выполняем ход
        board.setPiece(from.rank(), from.file(), null);
        board.setPiece(to.rank(), to.file(), fromPiece);

        // Проверяем, под шахом ли король
        boolean inCheck = CheckDetector.inCheck(sideToMove, board);

        // Восстанавливаем состояние
        board.setPiece(from.rank(), from.file(), fromPiece);
        board.setPiece(to.rank(), to.file(), toPiece);

        return inCheck;
    }

    /**
     * Проверяет, является ли ход легальным
     */
    public static boolean isValidMove(Square from, Square to, Color sideToMove, Board board) {
        Piece piece = board.getPiece(from.rank(), from.file());
        if (piece == null || piece.color() != sideToMove) {
            return false;
        }

        List<Square> legalMoves = getLegalMoves(from, piece, sideToMove, board);
        return legalMoves.contains(to);
    }
}
