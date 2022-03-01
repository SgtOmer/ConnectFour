package org.omer.connectfour.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Player {
    BOT(1), ENEMY(-1);

    private final int value;

    public static Player getOther(Player player) {
        if (player == BOT)
            return ENEMY;

        return BOT;
    }
}
