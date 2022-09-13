package ryoske.api.visibility;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@NoArgsConstructor
public abstract class VisibilityType {

    @Setter
    protected Visibility visibility;

    public void start() {
    }

    public void clear() {
    }

    public abstract Set<String> newSeers();

    public abstract Set<String> clearSeers();

}
