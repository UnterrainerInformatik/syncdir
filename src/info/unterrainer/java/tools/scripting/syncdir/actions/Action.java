package info.unterrainer.java.tools.scripting.syncdir.actions;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public abstract class Action {

	public abstract void doAction();

	@Override
	public abstract String toString();
}
