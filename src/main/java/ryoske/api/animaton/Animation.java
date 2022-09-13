package ryoske.api.animaton;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Animation {

    SWING_MAIN_HAND(0),
    HURT(1),
    WAKE_UP(2),
    SWING_OFF_HAND(3),
    CRITICAL_HIT(4),
    MAGIC_CRITICAL_HIT(5);

    private final int action;

}
