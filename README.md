# Chess Engine Core

Библиотека для работы с шахматной логикой, выделенная из проекта [MiniChess](https://github.com/True-Ruslan/MiniChess).

## Описание

MiniChess Core - это автономная Java библиотека, предоставляющая основную логику для шахматной игры:

- **Представление доски** - класс `Board` для работы с шахматной доской
- **Модели данных** - классы `Piece`, `Square`, `Color`, `PieceType`
- **Валидация ходов** - класс `MoveValidator` для проверки легальности ходов
- **Генерация ходов** - класс `PieceMoves` для генерации возможных ходов
- **Обнаружение шаха/мата** - класс `CheckDetector`
- **Проверка атак** - класс `AttackChecker`
- **Шахматный движок** - класс `ChessEngine` как основной API

## 🚀 Быстрый старт

### Способ 1: JitPack (рекомендуется)

Самый простой способ подключения - через JitPack:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.True-Ruslan.MiniChess</groupId>
        <artifactId>chess-engine-core</artifactId>
        <version>master-SNAPSHOT</version>
    </dependency>
</dependencies>
```

**Преимущества:**
- Не требует настройки аутентификации
- Работает сразу после добавления в pom.xml
- Автоматически обновляется при изменениях в репозитории

### Способ 2: GitHub Packages

Для использования стабильных версий через GitHub Packages:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/True-Ruslan/MiniChess</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>chess.mini</groupId>
        <artifactId>core</artifactId>
        <version>${version}</version>
    </dependency>
</dependencies>
```
Актуальную версию смотрите [тут](https://github.com/True-Ruslan/MiniChess-Core/packages)

**Требует настройки аутентификации** (см. раздел ниже).

## Настройка аутентификации для GitHub Packages

### Шаг 1: Создание Personal Access Token

1. Перейдите в [GitHub Settings → Developer settings → Personal access tokens](https://github.com/settings/tokens)
2. Нажмите "Generate new token (classic)"
3. Выберите scope `read:packages`
4. Скопируйте токен

### Шаг 2: Настройка Maven

Создайте файл `~/.m2/settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

Замените:
- `YOUR_GITHUB_USERNAME` - ваше имя пользователя GitHub
- `YOUR_GITHUB_TOKEN` - токен, созданный в шаге 1

## Использование

### Базовое использование

```java
import chess.mini.engine.ChessEngine;
import chess.mini.engine.data.Square;

public class ChessExample {
    public static void main(String[] args) {
        // Создание новой игры
        ChessEngine engine = new ChessEngine();

        // Получение возможных ходов для пешки e2
        Square from = new Square(4, 1); // e2
        List<Square> moves = engine.getLegalMoves(from);
        System.out.println("Возможные ходы для e2: " + moves);

        // Выполнение хода e2-e4
        Square to = new Square(4, 3); // e4
        engine.makeMove(from, to);

        // Проверка состояния игры
        System.out.println("Белые под шахом: " + engine.inCheck());
        System.out.println("Мат: " + engine.isCheckmate());
    }
}
```

### Работа с доской

```java
// Создание начальной позиции
Board board = Board.initial();

// Получение фигуры на клетке
Piece piece = board.getPiece(0, 4); // Белый король на e1

// Установка фигуры
board.setPiece(4, 4, new Piece(PieceType.QUEEN, Color.WHITE));

// Проверка атаки на клетку
boolean isAttacked = AttackChecker.isSquareAttacked(
        new Square(4, 4), Color.BLACK, board
);
```

### Валидация ходов

```java
// Проверка легальности хода
boolean isValid = MoveValidator.isValidMove(
                new Square(4, 1), // e2
                new Square(4, 3), // e4
                Color.WHITE,
                board
        );

// Получение всех легальных ходов для фигуры
List<Square> legalMoves = MoveValidator.getLegalMoves(
        new Square(4, 1),
        new Piece(PieceType.PAWN, Color.WHITE),
        Color.WHITE,
        board
);
```

### Обнаружение шаха

```java
// Проверка шаха
boolean whiteInCheck = CheckDetector.inCheck(Color.WHITE, board);
boolean blackInCheck = CheckDetector.inCheck(Color.BLACK, board);

// Проверка мата
boolean isCheckmate = CheckDetector.isCheckmate(Color.WHITE, board);

// Проверка пата
boolean isStalemate = CheckDetector.isStalemate(Color.WHITE, board);
```

## API Reference

### ChessEngine

Основной класс для работы с шахматной логикой.

#### Методы:
- `reset()` - сброс к начальной позиции
- `getBoard()` - получение текущей доски
- `getSideToMove()` - получение стороны для хода
- `getLegalMoves(Square from)` - получение легальных ходов
- `makeMove(Square from, Square to)` - выполнение хода
- `inCheck()` - проверка шаха
- `isCheckmate()` - проверка мата
- `isStalemate()` - проверка пата

### Board

Представление шахматной доски.

#### Методы:
- `Board.initial()` - создание начальной позиции
- `getPiece(int rank, int file)` - получение фигуры
- `setPiece(int rank, int file, Piece piece)` - установка фигуры

### Piece

Представление шахматной фигуры.

#### Конструктор:
- `Piece(PieceType type, Color color)`

### Square

Представление клетки на доске.

#### Конструктор:
- `Square(int file, int rank)` - file: 0-7 (a-h), rank: 0-7 (1-8)

## Требования

- Java 21+
- Maven 3.6+

## Сборка

```bash
mvn clean compile
mvn test
mvn package
```

## Устранение проблем

### Проблема: "Could not resolve dependencies"

**Решение:** Проверьте настройку репозитория и аутентификации.

### Проблема: "Authentication failed"

**Решение:**
1. Проверьте правильность токена GitHub
2. Убедитесь, что токен имеет scope `read:packages`
3. Проверьте файл `~/.m2/settings.xml`

### Проблема: "Version not found"

**Решение:**
1. Для JitPack: используйте `master-SNAPSHOT` или хеш коммита
2. Для GitHub Packages: проверьте доступность версии в репозитории

### Проблема: "Compilation failed"

**Решение:**
1. Убедитесь, что используется Java 21+
2. Проверьте импорты пакетов

## 📚 Дополнительные ресурсы

- [GitHub репозиторий](https://github.com/True-Ruslan/MiniChess-Core)
- [JitPack страница](https://jitpack.io/#True-Ruslan/MiniChess-Core)
- [GitHub Packages документация](https://docs.github.com/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)

## 🤝 Поддержка

Открывайте PR и предлагайте свои решения
