package net.momirealms.craftengine.core.font;

import net.kyori.adventure.text.Component;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.pack.LoadingSequence;
import net.momirealms.craftengine.core.pack.Pack;
import net.momirealms.craftengine.core.pack.ResourceLocation;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.plugin.config.ConfigSectionParser;
import net.momirealms.craftengine.core.plugin.locale.TranslationManager;
import net.momirealms.craftengine.core.util.*;
import net.momirealms.craftengine.core.util.context.ContextHolder;
import net.momirealms.craftengine.core.util.context.PlayerContext;
import org.ahocorasick.trie.Token;
import org.ahocorasick.trie.Trie;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractFontManager implements FontManager {
    private final CraftEngine plugin;
                // namespace:font font
    private final Map<Key, Font> fonts = new HashMap<>();
    // namespace:id emoji
    private final Map<Key, Emoji> emojis = new HashMap<>();
    // namespace:id image
    private final Map<Key, BitmapImage> images = new HashMap<>();
    private final Set<Integer> illegalChars = new HashSet<>();
    private final ImageParser imageParser;
    private final EmojiParser emojiParser;
    private OffsetFont offsetFont;

    protected Trie imageTagTrie;
    protected Trie emojiKeywordTrie;
    protected Map<String, Component> tagMapper;
    protected Map<String, Emoji> emojiMapper;

    protected List<Emoji> emojiList;
    protected List<String> allEmojiSuggestions;

    public AbstractFontManager(CraftEngine plugin) {
        this.plugin = plugin;
        this.imageParser = new ImageParser();
        this.emojiParser = new EmojiParser();
    }

    @Override
    public void load() {
        this.offsetFont = Optional.ofNullable(plugin.config().settings().getSection("image.offset-characters"))
                .map(OffsetFont::new)
                .orElse(null);
    }

    @Override
    public void unload() {
        this.fonts.clear();
        this.images.clear();
        this.illegalChars.clear();
        this.emojis.clear();
    }

    @Override
    public void disable() {
        this.unload();
    }

    @Override
    public ConfigSectionParser[] parsers() {
        return new ConfigSectionParser[] {this.imageParser, this.emojiParser};
    }

    @Override
    public void delayedLoad() {
        Optional.ofNullable(this.fonts.get(DEFAULT_FONT)).ifPresent(font -> this.illegalChars.addAll(font.codepointsInUse()));
        this.buildImageTagTrie();
        this.buildEmojiKeywordsTrie();
        this.emojiList = new ArrayList<>(this.emojis.values());
        this.allEmojiSuggestions = this.emojis.values().stream()
                .flatMap(emoji -> emoji.keywords().stream())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Component> matchTags(String json) {
        if (this.imageTagTrie == null) {
            return Collections.emptyMap();
        }
        Map<String, Component> tags = new HashMap<>();
        for (Token token : this.imageTagTrie.tokenize(json)) {
            if (token.isMatch()) {
                tags.put(token.getFragment(), this.tagMapper.get(token.getFragment()));
            }
        }
        return tags;
    }

    @Override
    public EmojiTextProcessResult replaceMiniMessageEmoji(@NotNull String miniMessage, Player player, int maxTimes) {
        if (this.emojiKeywordTrie == null || maxTimes <= 0) {
            return EmojiTextProcessResult.notReplaced(miniMessage);
        }
        Map<String, String> replacements = new HashMap<>();
        for (Token token : this.emojiKeywordTrie.tokenize(miniMessage)) {
            if (!token.isMatch())
                continue;
            String fragment = token.getFragment();
            if (replacements.containsKey(fragment))
                continue;
            Emoji emoji = this.emojiMapper.get(fragment);
            if (emoji == null || (player != null && emoji.permission() != null && !player.hasPermission(emoji.permission())))
                continue;
            Component content = AdventureHelper.miniMessage().deserialize(
                    emoji.content(),
                    PlayerContext.of(player, ContextHolder.builder()
                            .withOptionalParameter(EmojiParameters.EMOJI, emoji.emojiImage())
                            .withParameter(EmojiParameters.KEYWORD, emoji.keywords().get(0))
                            .build()).tagResolvers()
            );
            replacements.put(fragment, AdventureHelper.componentToMiniMessage(content));
        }
        if (replacements.isEmpty()) return EmojiTextProcessResult.notReplaced(miniMessage);
        String regex = replacements.keySet().stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(miniMessage);
        StringBuilder sb = new StringBuilder();
        int count = 0;
        while (matcher.find() && count < maxTimes) {
            String key = matcher.group();
            String replacement = replacements.get(key);
            if (replacement != null) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
                count++;
            } else {
                // should not reach this
                matcher.appendReplacement(sb, Matcher.quoteReplacement(matcher.group()));
            }
        }
        matcher.appendTail(sb);
        return EmojiTextProcessResult.replaced(sb.toString());
    }

    @Override
    public EmojiTextProcessResult replaceJsonEmoji(@NotNull String jsonText, Player player, int maxTimes) {
        if (this.emojiKeywordTrie == null) {
            return EmojiTextProcessResult.notReplaced(jsonText);
        }
        Map<String, Component> emojis = new HashMap<>();
        for (Token token : this.emojiKeywordTrie.tokenize(jsonText)) {
            if (!token.isMatch())
                continue;
            String fragment = token.getFragment();
            if (emojis.containsKey(fragment)) continue;
            Emoji emoji = this.emojiMapper.get(fragment);
            if (emoji == null || (player != null && emoji.permission() != null && !player.hasPermission(emoji.permission())))
                continue;
            emojis.put(fragment, AdventureHelper.miniMessage().deserialize(
                    emoji.content(),
                    PlayerContext.of(player, ContextHolder.builder()
                            .withOptionalParameter(EmojiParameters.EMOJI, emoji.emojiImage())
                            .withParameter(EmojiParameters.KEYWORD, emoji.keywords().get(0))
                            .build()).tagResolvers())
            );
            if (emojis.size() >= maxTimes) break;
        }
        if (emojis.isEmpty()) return EmojiTextProcessResult.notReplaced(jsonText);
        Component component = AdventureHelper.jsonToComponent(jsonText);
        String patternString = emojis.keySet().stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
        component = component.replaceText(builder -> builder.times(maxTimes)
                .match(Pattern.compile(patternString))
                .replacement((result, b) -> emojis.get(result.group())));
        return EmojiTextProcessResult.replaced(AdventureHelper.componentToJson(component));
    }

    @Override
    public EmojiComponentProcessResult replaceComponentEmoji(@NotNull Component text, Player player, @NotNull String raw, int maxTimes) {
        Map<String, Component> emojis = new HashMap<>();
        for (Token token : this.emojiKeywordTrie.tokenize(raw)) {
            if (!token.isMatch())
                continue;
            String fragment = token.getFragment();
            if (emojis.containsKey(fragment))
                continue;
            Emoji emoji = this.emojiMapper.get(token.getFragment());
            if (emoji == null || (player != null && emoji.permission() != null && !player.hasPermission(Objects.requireNonNull(emoji.permission()))))
                continue;
            emojis.put(fragment, AdventureHelper.miniMessage().deserialize(
                    emoji.content(),
                    PlayerContext.of(player,
                            ContextHolder.builder()
                                    .withOptionalParameter(EmojiParameters.EMOJI, emoji.emojiImage())
                                    .withParameter(EmojiParameters.KEYWORD, emoji.keywords().get(0))
                                    .build()
                    ).tagResolvers()
            ));
            if (emojis.size() >= maxTimes) break;
        }
        if (emojis.isEmpty()) return EmojiComponentProcessResult.failed();
        String patternString = emojis.keySet().stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
        text = text.replaceText(builder -> builder.times(maxTimes)
                .match(Pattern.compile(patternString))
                .replacement((result, b) -> emojis.get(result.group())));
        return EmojiComponentProcessResult.success(text);
    }

    @Override
    public IllegalCharacterProcessResult processIllegalCharacters(String raw, char replacement) {
        boolean hasIllegal = false;
        // replace illegal image usage
        Map<String, Component> tokens = matchTags(raw);
        if (!tokens.isEmpty()) {
            for (Map.Entry<String, Component> entry : tokens.entrySet()) {
                raw = raw.replace(entry.getKey(), String.valueOf(replacement));
                hasIllegal = true;
            }
        }

        if (this.isDefaultFontInUse()) {
            // replace illegal codepoint
            char[] chars = raw.toCharArray();
            int[] codepoints = CharacterUtils.charsToCodePoints(chars);
            int[] newCodepoints = new int[codepoints.length];

            for (int i = 0; i < codepoints.length; i++) {
                int codepoint = codepoints[i];
                if (!isIllegalCodepoint(codepoint)) {
                    newCodepoints[i] = codepoint;
                } else {
                    newCodepoints[i] = replacement;
                    hasIllegal = true;
                }
            }

            if (hasIllegal) {
                return IllegalCharacterProcessResult.has(new String(newCodepoints, 0, newCodepoints.length));
            }
        } else if (hasIllegal) {
            return IllegalCharacterProcessResult.has(raw);
        }
        return IllegalCharacterProcessResult.not();
    }

    private void buildEmojiKeywordsTrie() {
        this.emojiMapper = new HashMap<>();
        for (Emoji emoji : this.emojis.values()) {
            for (String keyword : emoji.keywords()) {
                this.emojiMapper.put(keyword, emoji);
            }
        }
        this.emojiKeywordTrie = Trie.builder()
                .ignoreOverlaps()
                .addKeywords(this.emojiMapper.keySet())
                .build();
    }

    private void buildImageTagTrie() {
        this.tagMapper = new HashMap<>();
        for (BitmapImage image : this.images.values()) {
            String id = image.id().toString();
            this.tagMapper.put(imageTag(id), AdventureHelper.miniMessage().deserialize(image.miniMessage(0, 0)));
            this.tagMapper.put("\\" + imageTag(id), Component.text(imageTag(id)));
            for (int i = 0; i < image.rows(); i++) {
                for (int j = 0; j < image.columns(); j++) {
                    this.tagMapper.put(imageTag(id + ":" + i + ":" + j), AdventureHelper.miniMessage().deserialize(image.miniMessage(i, j)));
                    this.tagMapper.put(imageTag("\\" + id + ":" + i + ":" + j), Component.text(imageTag(id + ":" + i + ":" + j)));
                }
            }
        }
        for (int i = -256; i <= 256; i++) {
            this.tagMapper.put("<shift:" + i + ">", AdventureHelper.miniMessage().deserialize(this.offsetFont.createOffset(i, FormatUtils::miniMessageFont)));
            this.tagMapper.put("\\<shift:" + i + ">", Component.text("<shift:" + i + ">"));
        }
        this.imageTagTrie = Trie.builder()
                .ignoreOverlaps()
                .addKeywords(this.tagMapper.keySet())
                .build();
    }

    private static String imageTag(String text) {
        return "<image:" + text + ">";
    }

    @Override
    public boolean isDefaultFontInUse() {
        return !this.illegalChars.isEmpty();
    }

    @Override
    public boolean isIllegalCodepoint(int codepoint) {
        return this.illegalChars.contains(codepoint);
    }

    @Override
    public Collection<Font> fonts() {
        return Collections.unmodifiableCollection(this.fonts.values());
    }

    @Override
    public Optional<BitmapImage> bitmapImageByCodepoint(Key font, int codepoint) {
        return fontById(font).map(f -> f.bitmapImageByCodepoint(codepoint));
    }

    @Override
    public Optional<BitmapImage> bitmapImageByImageId(Key id) {
        return Optional.ofNullable(this.images.get(id));
    }

    @Override
    public int codepointByImageId(Key key, int x, int y) {
        BitmapImage image = this.images.get(key);
        if (image == null) return -1;
        return image.codepointAt(x, y);
    }

    @Override
    public String createOffsets(int offset, FontTagFormatter tagFormatter) {
        return Optional.ofNullable(this.offsetFont).map(it -> it.createOffset(offset, tagFormatter)).orElse("");
    }

    @Override
    public Optional<Font> fontById(Key id) {
        return Optional.ofNullable(this.fonts.get(id));
    }

    private Font getOrCreateFont(Key key) {
        return this.fonts.computeIfAbsent(key, Font::new);
    }

    public class EmojiParser implements ConfigSectionParser {
        public static final String[] CONFIG_SECTION_NAME = new String[] {"emoji", "emojis"};

        @Override
        public String[] sectionId() {
            return CONFIG_SECTION_NAME;
        }

        @Override
        public int loadingSequence() {
            return LoadingSequence.EMOJI;
        }

        @Override
        public void parseSection(Pack pack, Path path, Key id, Map<String, Object> section) {
            if (emojis.containsKey(id)) {
                TranslationManager.instance().log("warning.config.emoji.duplicated", path.toString(), id.toString());
                return;
            }
            String permission = (String) section.get("permission");
            Object keywordsRaw = section.get("keywords");
            if (keywordsRaw == null) {
                TranslationManager.instance().log("warning.config.emoji.lack_keywords", path.toString(), id.toString());
                return;
            }
            List<String> keywords = MiscUtils.getAsStringList(keywordsRaw);
            if (keywords.isEmpty()) {
                TranslationManager.instance().log("warning.config.emoji.lack_keywords", path.toString(), id.toString());
                return;
            }
            String content = section.getOrDefault("content", "<arg:emoji>").toString();
            String image = null;
            if (section.containsKey("image")) {
                String rawImage = section.get("image").toString();
                String[] split = rawImage.split(":");
                if (split.length == 2) {
                    Key imageId = new Key(split[0], split[1]);
                    Optional<BitmapImage> bitmapImage = bitmapImageByImageId(imageId);
                    if (bitmapImage.isPresent()) {
                        image = bitmapImage.get().miniMessage(0, 0);
                    } else {
                        TranslationManager.instance().log("warning.config.emoji.invalid_image", path.toString(), id.toString(), rawImage);
                        return;
                    }
                } else if (split.length == 4) {
                    Key imageId = new Key(split[0], split[1]);
                    Optional<BitmapImage> bitmapImage = bitmapImageByImageId(imageId);
                    if (bitmapImage.isPresent()) {
                        image = bitmapImage.get().miniMessage(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                    } else {
                        TranslationManager.instance().log("warning.config.emoji.invalid_image", path.toString(), id.toString(), rawImage);
                        return;
                    }
                } else {
                    TranslationManager.instance().log("warning.config.emoji.invalid_image", path.toString(), id.toString(), rawImage);
                    return;
                }
            }
            Emoji emoji = new Emoji(content, permission, image, keywords);
            emojis.put(id, emoji);
        }
    }

    public class ImageParser implements ConfigSectionParser {
        public static final String[] CONFIG_SECTION_NAME = new String[] {"images", "image"};

        @Override
        public String[] sectionId() {
            return CONFIG_SECTION_NAME;
        }

        @Override
        public int loadingSequence() {
            return LoadingSequence.IMAGE;
        }

        @Override
        public void parseSection(Pack pack, Path path, Key id, Map<String, Object> section) {
            if (images.containsKey(id)) {
                TranslationManager.instance().log("warning.config.image.duplicated", path.toString(), id.toString());
                return;
            }

            Object heightObj = section.get("height");
            if (heightObj == null) {
                TranslationManager.instance().log("warning.config.image.lack_height", path.toString(), id.toString());
                return;
            }

            int height = MiscUtils.getAsInt(heightObj);
            int ascent = MiscUtils.getAsInt(section.getOrDefault("ascent", height - 1));
            if (height < ascent) {
                TranslationManager.instance().log("warning.config.image.height_smaller_than_ascent", path.toString(), id.toString());
                return;
            }

            Object file = section.get("file");
            if (file == null) {
                TranslationManager.instance().log("warning.config.image.no_file", path.toString(), id.toString());
                return;
            }

            String resourceLocation = file.toString().replace("\\", "/");
            if (!ResourceLocation.isValid(resourceLocation)) {
                TranslationManager.instance().log("warning.config.image.invalid_resource_location", path.toString(), id.toString(), resourceLocation);
                return;
            }

            String fontName = (String) section.getOrDefault("font", "minecraft:default");
            if (!ResourceLocation.isValid(fontName)) {
                TranslationManager.instance().log("warning.config.image.invalid_font_name", path.toString(), id.toString(), fontName);
                return;
            }

            Key fontKey = Key.withDefaultNamespace(fontName, id.namespace());
            Font font = getOrCreateFont(fontKey);
            List<char[]> chars;
            if (section.containsKey("chars")) {
                chars = MiscUtils.getAsStringList(section.get("chars")).stream().map(it -> {
                    if (it.startsWith("\\u")) {
                        return CharacterUtils.decodeUnicodeToChars(it);
                    } else {
                        return it.toCharArray();
                    }
                }).toList();
            } else {
                String character = (String) section.get("char");
                if (character == null) {
                    TranslationManager.instance().log("warning.config.image.lack_char", path.toString(), id.toString());
                    return;
                }
                if (character.length() == 1) {
                    chars = List.of(character.toCharArray());
                } else {
                    chars = List.of(CharacterUtils.decodeUnicodeToChars(character));
                }
            }

            int size = -1;
            int[][] codepointGrid = new int[chars.size()][];
            for (int i = 0; i < chars.size(); ++i) {
                int[] codepoints = CharacterUtils.charsToCodePoints(chars.get(i));
                for (int codepoint : codepoints) {
                    if (font.isCodepointInUse(codepoint)) {
                        BitmapImage image = font.bitmapImageByCodepoint(codepoint);
                        TranslationManager.instance().log("warning.config.image.codepoint_in_use",
                                path.toString(),
                                id.toString(),
                                fontKey.toString(),
                                CharacterUtils.encodeCharsToUnicode(Character.toChars(codepoint)),
                                new String(Character.toChars(codepoint)),
                                image.id().toString()
                        );
                        return;
                    }
                }
                codepointGrid[i] = codepoints;
                if (size == -1) size = codepoints.length;
                if (size != codepoints.length) {
                    TranslationManager.instance().log("warning.config.image.invalid_codepoint_grid", path.toString(), id.toString());
                    return;
                }
            }

            if (!resourceLocation.endsWith(".png")) resourceLocation += ".png";
            Key namespacedPath = Key.of(resourceLocation);
            Path targetImagePath = pack.resourcePackFolder()
                    .resolve("assets")
                    .resolve(namespacedPath.namespace())
                    .resolve("textures")
                    .resolve(namespacedPath.value());

            if (!Files.exists(targetImagePath)) {
                TranslationManager.instance().log("warning.config.image.file_not_exist", path.toString(), id.toString(), targetImagePath.toString());
                // DO NOT RETURN, JUST GIVE WARNINGS
            }

            BitmapImage bitmapImage = new BitmapImage(id, fontKey, height, ascent, resourceLocation, codepointGrid);
            for (int[] y : codepointGrid) {
                for (int x : y) {
                    font.addBitMapImage(x, bitmapImage);
                }
            }

            AbstractFontManager.this.images.put(id, bitmapImage);
        }
    }
}
