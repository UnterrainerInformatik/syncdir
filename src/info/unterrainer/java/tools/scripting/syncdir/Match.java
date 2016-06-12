package info.unterrainer.java.tools.scripting.syncdir;

import java.util.ArrayList;
import java.util.List;

class Match {
    String match;
    List<String> groups = new ArrayList<String>();

    Match(String match, List<String> groups) {
        super();
        this.match = match;
        this.groups = groups;
    }

    String getMatch() {
        return match;
    }

    List<String> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return match;
    }
}
