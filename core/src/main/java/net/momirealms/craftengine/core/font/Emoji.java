package net.momirealms.craftengine.core.font;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Emoji {
    private final String content;
    private final String permission;
    private final String image;
    private final List<String> keywords;

    public Emoji(String content, String permission, String image, List<String> keywords) {
        this.content = content;
        this.image = image;
        this.permission = permission;
        this.keywords = keywords;
    }

    public String content() {
        return content;
    }

    @Nullable
    public String emojiImage() {
        return image;
    }

    @Nullable
    public String permission() {
        return permission;
    }

    public List<String> keywords() {
        return keywords;
    }
}
