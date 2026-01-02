package org.omer.connectfour.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.omer.connectfour.bot.Bot;
import org.omer.connectfour.enums.Player;

import java.util.UUID;

/**
 * Represents a Connect Four game session with board, bot, and unique
 * identifier.
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Game {
    @EqualsAndHashCode.Include
    private final UUID uuid;
    private final Board board;
    private final Bot bot;

    public Game(Board board, Bot bot) {
        uuid = UUID.randomUUID();
        this.board = board;
        this.bot = bot;
    }

    public int playMove(int move) {
        board.setMove(move - 1);
        int botMove = bot.makeMove(0, Player.BOT);
        board.setMove(botMove);
        return botMove + 1;
    }
}
