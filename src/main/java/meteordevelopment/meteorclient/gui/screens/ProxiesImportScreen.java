/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.gui.screens;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WVerticalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.systems.proxies.Proxies;
import meteordevelopment.meteorclient.systems.proxies.Proxy;
import meteordevelopment.meteorclient.systems.proxies.ProxyType;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class ProxiesImportScreen extends WindowScreen {

    private final File file;
    public ProxiesImportScreen(GuiTheme theme, File file) {
        super(theme, "Import Proxies");
        this.file = file;
        this.onClosed(() -> {
            if (parent instanceof ProxiesScreen screen) {
                screen.reload();
            }
        });
    }

    @Override
    public void initWidgets() {
        if (file.exists() && file.isFile()) {
            add(theme.label("Importing proxies from " + file.getName() + "...").color(Color.GREEN));
            WVerticalList list = add(theme.section("Log", false)).widget().add(theme.verticalList()).expandX().widget();
            Proxies proxies = Proxies.get();
    
            try {
                int successfulImports = 0, failedImports = 0;
    
                // Proxy pattern updated to support new formats
                Pattern PROXY_PATTERN = Pattern.compile(
                    "(?:(\\S+):(\\S+)@)?(\\S+):(\\d+)(?::(\\S+):(\\S+))?"
                );
    
                for (String line : Files.readAllLines(file.toPath())) {
                    Matcher matcher = PROXY_PATTERN.matcher(line);
    
                    if (matcher.matches()) {
                        String username = matcher.group(1); // Extract username from username:password@ip:port
                        String password = matcher.group(2); // Extract password from username:password@ip:port
                        String address = matcher.group(3);  // Extract IP address
                        int port = Integer.parseInt(matcher.group(4)); // Extract port
                        if (username == null) {
                            username = matcher.group(5); // Extract username from ip:port:username:password
                            password = matcher.group(6); // Extract password from ip:port:username:password
                        }
    
                        Proxy proxy = new Proxy.Builder()
                            .address(address)
                            .port(port)
                            .username(username != null ? username : "")
                            .password(password != null ? password : "")
                            .name(username != null ? username + "@" + address + ":" + port : address + ":" + port)
                            .type(ProxyType.Socks5) // Defaulting type to Socks5; adjust if type parsing is needed
                            .build();
    
                        if (proxies.add(proxy)) {
                            list.add(theme.label("Imported proxy: " + proxy.name.get()).color(Color.GREEN));
                            successfulImports++;
                        } else {
                            list.add(theme.label("Proxy already exists: " + proxy.name.get()).color(Color.ORANGE));
                            failedImports++;
                        }
                    } else {
                        list.add(theme.label("Invalid proxy: " + line).color(Color.RED));
                        failedImports++;
                    }
                }
    
                add(theme
                    .label("Successfully imported " + successfulImports + "/" + (successfulImports + failedImports) + " proxies.")
                    .color(Utils.lerp(Color.RED, Color.GREEN, (float) successfulImports / (successfulImports + failedImports)))
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            add(theme.label("Invalid File!"));
        }
    
        add(theme.horizontalSeparator()).expandX();
        WButton btnBack = add(theme.button("Back")).expandX().widget();
        btnBack.action = this::close;
    }
}
