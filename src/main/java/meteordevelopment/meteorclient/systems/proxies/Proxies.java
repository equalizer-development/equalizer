/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.proxies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;
import net.minecraft.nbt.NbtCompound;

public class Proxies extends System<Proxies> implements Iterable<Proxy> {
    // Updated Regex Pattern to handle all formats
    public static final Pattern PROXY_PATTERN = Pattern.compile(
        "(?:(\\S+):(\\S+)@)?(\\S+):(\\d+)(?::(\\S+):(\\S+))?",
        Pattern.MULTILINE
    );

    private List<Proxy> proxies = new ArrayList<>();

    public Proxies() {
        super("proxies");
    }

    public static Proxies get() {
        return Systems.get(Proxies.class);
    }

    /**
     * Add a proxy if it doesn't already exist.
     */
    public boolean add(Proxy proxy) {
        for (Proxy p : proxies) {
            if (p.type.get().equals(proxy.type.get()) &&
                p.address.get().equals(proxy.address.get()) &&
                p.port.get() == proxy.port.get() &&
                Objects.equals(p.username.get(), proxy.username.get()) &&
                Objects.equals(p.password.get(), proxy.password.get())) {
                return false; // Proxy already exists
            }
        }

        if (proxies.isEmpty()) proxy.enabled.set(true);

        proxies.add(proxy);
        save();

        return true;
    }

    /**
     * Parse a proxy string into a Proxy object.
     */
    public Proxy parseProxy(String proxyString) {
        Matcher matcher = PROXY_PATTERN.matcher(proxyString);

        if (matcher.matches()) {
            String username = matcher.group(1); // Extract username from username:password@ip:port
            String password = matcher.group(2); // Extract password from username:password@ip:port
            String address = matcher.group(3);  // Extract IP address
            int port = Integer.parseInt(matcher.group(4)); // Extract port

            if (username == null) {
                username = matcher.group(5); // Extract username from ip:port:username:password
                password = matcher.group(6); // Extract password from ip:port:username:password
            }

            return new Proxy.Builder()
                .address(address)
                .port(port)
                .username(username != null ? username : "")
                .password(password != null ? password : "")
                .name(username != null ? username + "@" + address + ":" + port : address + ":" + port)
                .type(ProxyType.Socks5) // Default type to Socks5, modify if type parsing is needed
                .build();
        }

        throw new IllegalArgumentException("Invalid proxy format: " + proxyString);
    }

    /**
     * Remove a proxy.
     */
    public void remove(Proxy proxy) {
        if (proxies.remove(proxy)) {
            save();
        }
    }

    /**
     * Get the currently enabled proxy.
     */
    public Proxy getEnabled() {
        for (Proxy proxy : proxies) {
            if (proxy.enabled.get()) return proxy;
        }

        return null;
    }

    /**
     * Enable or disable a specific proxy.
     */
    public void setEnabled(Proxy proxy, boolean enabled) {
        for (Proxy p : proxies) {
            p.enabled.set(false);
        }

        proxy.enabled.set(enabled);
        save();
    }

    /**
     * Check if the list of proxies is empty.
     */
    public boolean isEmpty() {
        return proxies.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<Proxy> iterator() {
        return proxies.iterator();
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.put("proxies", NbtUtils.listToTag(proxies));

        return tag;
    }

    @Override
    public Proxies fromTag(NbtCompound tag) {
        proxies = NbtUtils.listFromTag(tag.getList("proxies", 10), Proxy::new);

        return this;
    }
}