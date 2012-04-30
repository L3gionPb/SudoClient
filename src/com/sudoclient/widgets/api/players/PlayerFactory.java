package com.sudoclient.widgets.api.players;

import com.sudoclient.widgets.api.players.attributes.RSFeed;
import com.sudoclient.widgets.api.players.attributes.Skills;

/**
 * User: deprecated
 * Date: 4/29/12
 * Time: 2:49 AM
 */

public class PlayerFactory {
    public final static int SKILLS = 0x01;
    public final static int FEED = 0x02;

    public static Player define(String username) {
        return define(username, SKILLS | FEED);
    }

    public static Player define(String username, int flags) {
        RSFeed feed = ((flags & FEED) != 0 ? createFeed(username) : null);
        Skills skills = ((flags & SKILLS) != 0 ? createSkills(username) : null);

        return new Player(feed, skills);
    }

    private static RSFeed createFeed(String username) {
        return new RSFeed(username);
    }

    private static Skills createSkills(String username) {
        return null;
    }
}
