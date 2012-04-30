package com.sudoclient.widgets.api.players;

import com.sudoclient.widgets.api.players.attributes.RSFeed;
import com.sudoclient.widgets.api.players.attributes.Skills;

/**
 * User: deprecated
 * Date: 4/29/12
 * Time: 2:48 AM
 */
public class Player {
    private RSFeed feed;
    private Skills skills;

    Player(RSFeed feed, Skills skills) {
        this.feed = feed;
        this.skills = skills;
    }

    public RSFeed getFeed() {
        return feed;
    }

    public Skills getSkills() {
        return skills;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        if (feed != null) {
            feed.remove();
        }
    }
}
