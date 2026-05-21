/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package me.lucko.luckperms.placeholders;

import at.helpch.placeholderapi.expansion.PlaceholderExpansion;
import com.hypixel.hytale.common.plugin.PluginIdentifier;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import me.lucko.luckperms.common.placeholders.PlaceholderContext;
import me.lucko.luckperms.common.placeholders.PlaceholderResolver;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.platform.PlayerAdapter;
import net.luckperms.api.query.QueryOptions;

/**
 * PlaceholderAPI Expansion for LuckPerms, implemented using the LuckPerms API.
 */
public class LuckPermsExpansion extends PlaceholderExpansion {
    private static final String IDENTIFIER = "luckperms";
    private static final String PLUGIN_NAME = "LuckPerms:LuckPerms";
    private static final String AUTHOR = "Luck";
    private static final String VERSION = "5.5-R1";

    private LuckPerms luckPerms;
    private PlayerAdapter<PlayerRef> playerAdapter;
    private PlaceholderResolver resolver;

    @Override
    public boolean canRegister() {
        return HytaleServer.get().getPluginManager().getPlugin(PluginIdentifier.fromString(PLUGIN_NAME)) != null;
    }

    @Override
    public boolean register() {
        if (!canRegister()) {
            return false;
        }

        this.luckPerms = LuckPermsProvider.get();
        this.playerAdapter = this.luckPerms.getPlayerAdapter(PlayerRef.class);
        this.resolver = new PlaceholderResolver();
        return super.register();
    }

    @Override
    public String onPlaceholderRequest(PlayerRef player, String identifier) {
        if (player == null || this.luckPerms == null || this.resolver == null) {
            return "";
        }

        User user = this.playerAdapter.getUser(player);
        QueryOptions queryOptions = this.playerAdapter.getQueryOptions(player);
        PlaceholderContext context = new PlaceholderContext(this.luckPerms, user, queryOptions);

        return this.resolver.resolve(context, identifier);
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public String getRequiredPlugin() {
        return PLUGIN_NAME;
    }

    @Override
    public String getAuthor() {
        return AUTHOR;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

}
