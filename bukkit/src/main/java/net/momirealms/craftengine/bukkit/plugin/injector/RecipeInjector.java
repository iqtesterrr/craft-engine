package net.momirealms.craftengine.bukkit.plugin.injector;

import com.mojang.datafixers.util.Pair;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import net.momirealms.craftengine.bukkit.item.BukkitItemManager;
import net.momirealms.craftengine.bukkit.item.recipe.BukkitRecipeManager;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MRecipeTypes;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.recipe.CustomCookingRecipe;
import net.momirealms.craftengine.core.item.recipe.OptimizedIDItem;
import net.momirealms.craftengine.core.item.recipe.RecipeTypes;
import net.momirealms.craftengine.core.item.recipe.input.SingleItemInput;
import net.momirealms.craftengine.core.registry.BuiltInRegistries;
import net.momirealms.craftengine.core.registry.Holder;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ReflectionUtils;
import net.momirealms.craftengine.core.util.VersionHelper;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class RecipeInjector {
    private static Class<?> clazz$InjectedCacheChecker;
    
    public static void init() {
        ByteBuddy byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V17);
        clazz$InjectedCacheChecker = byteBuddy
                .subclass(Object.class, ConstructorStrategy.Default.IMITATE_SUPER_CLASS_OPENING)
                .name("net.momirealms.craftengine.bukkit.entity.InjectedCacheChecker")
                .implement(CoreReflections.clazz$RecipeManager$CachedCheck)
                .implement(InjectedCacheCheck.class)
                .defineField("recipeType", Object.class, Visibility.PUBLIC)
                .method(ElementMatchers.named("recipeType"))
                .intercept(FieldAccessor.ofField("recipeType"))
                .defineField("lastRecipe", Object.class, Visibility.PUBLIC)
                .method(ElementMatchers.named("lastRecipe"))
                .intercept(FieldAccessor.ofField("lastRecipe"))
                .method(ElementMatchers.named("setLastRecipe"))
                .intercept(FieldAccessor.ofField("lastRecipe"))
                .defineField("lastCustomRecipe", Key.class, Visibility.PUBLIC)
                .method(ElementMatchers.named("lastCustomRecipe"))
                .intercept(FieldAccessor.ofField("lastCustomRecipe"))
                .method(ElementMatchers.named("getRecipeFor").or(ElementMatchers.named("a")))
                .intercept(MethodDelegation.to(
                        VersionHelper.isOrAbove1_21_2() ?
                                GetRecipeForMethodInterceptor1_21_2.INSTANCE :
                                (VersionHelper.isOrAbove1_21() ?
                                        GetRecipeForMethodInterceptor1_21.INSTANCE :
                                        VersionHelper.isOrAbove1_20_5() ?
                                                GetRecipeForMethodInterceptor1_20_5.INSTANCE :
                                                GetRecipeForMethodInterceptor1_20.INSTANCE)
                ))
                .make()
                .load(RecipeInjector.class.getClassLoader())
                .getLoaded();
    }

    public static void injectCookingBlockEntity(Object entity) throws ReflectiveOperationException {
        if (CoreReflections.clazz$AbstractFurnaceBlockEntity.isInstance(entity)) {
            Object quickCheck = CoreReflections.field$AbstractFurnaceBlockEntity$quickCheck.get(entity);
            if (clazz$InjectedCacheChecker.isInstance(quickCheck)) return; // already injected
            Object recipeType = FastNMS.INSTANCE.field$AbstractFurnaceBlockEntity$recipeType(entity);
            InjectedCacheCheck injectedChecker = (InjectedCacheCheck) ReflectionUtils.UNSAFE.allocateInstance(clazz$InjectedCacheChecker);
            injectedChecker.recipeType(recipeType);
            CoreReflections.field$AbstractFurnaceBlockEntity$quickCheck.set(entity, injectedChecker);
        } else if (!VersionHelper.isOrAbove1_21_2() && CoreReflections.clazz$CampfireBlockEntity.isInstance(entity)) {
            Object quickCheck = CoreReflections.field$CampfireBlockEntity$quickCheck.get(entity);
            if (clazz$InjectedCacheChecker.isInstance(quickCheck)) return; // already injected
            InjectedCacheCheck injectedChecker = (InjectedCacheCheck) ReflectionUtils.UNSAFE.allocateInstance(clazz$InjectedCacheChecker);
            injectedChecker.recipeType(MRecipeTypes.CAMPFIRE_COOKING);
            CoreReflections.field$CampfireBlockEntity$quickCheck.set(entity, injectedChecker);
        }
    }

    public static class GetRecipeForMethodInterceptor1_20 {
        public static final GetRecipeForMethodInterceptor1_20 INSTANCE = new GetRecipeForMethodInterceptor1_20();

        @SuppressWarnings("unchecked")
        @RuntimeType
        public Object intercept(@This Object thisObj, @AllArguments Object[] args) throws Exception {
            Object mcRecipeManager = BukkitRecipeManager.nmsRecipeManager();
            InjectedCacheCheck injectedCacheCheck = (InjectedCacheCheck) thisObj;
            Object type = injectedCacheCheck.recipeType();
            Object lastRecipe = injectedCacheCheck.lastRecipe();
            Optional<Pair<Object, Object>> optionalRecipe = FastNMS.INSTANCE.method$RecipeManager$getRecipeFor(mcRecipeManager, type, args[0], args[1], lastRecipe);
            if (optionalRecipe.isPresent()) {
                Pair<Object, Object> pair = optionalRecipe.get();
                Object resourceLocation = pair.getFirst();
                Key recipeId = Key.of(resourceLocation.toString());
                BukkitRecipeManager recipeManager = BukkitRecipeManager.instance();

                ItemStack itemStack;
                List<Object> items;
                if (type == MRecipeTypes.CAMPFIRE_COOKING) {
                    items = (List<Object>) CoreReflections.field$SimpleContainer$items.get(args[0]);
                } else {
                    items = (List<Object>) CoreReflections.field$AbstractFurnaceBlockEntity$items.get(args[0]);
                }
                itemStack = FastNMS.INSTANCE.method$CraftItemStack$asCraftMirror(items.get(0));

                // it's a recipe from other plugins
                boolean isCustom = recipeManager.isCustomRecipe(recipeId);
                if (!isCustom) {
                    injectedCacheCheck.lastRecipe(resourceLocation);
                    return Optional.of(pair.getSecond());
                }

                Item<ItemStack> wrappedItem = BukkitItemManager.instance().wrap(itemStack);
                Optional<Holder.Reference<Key>> idHolder = BuiltInRegistries.OPTIMIZED_ITEM_ID.get(wrappedItem.id());
                if (idHolder.isEmpty()) {
                    return Optional.empty();
                }

                SingleItemInput<ItemStack> input = new SingleItemInput<>(new OptimizedIDItem<>(idHolder.get(), itemStack));
                CustomCookingRecipe<ItemStack> ceRecipe;
                Key lastCustomRecipe = injectedCacheCheck.lastCustomRecipe();
                if (type == MRecipeTypes.SMELTING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.SMELTING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.BLASTING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.BLASTING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.SMOKING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.SMOKING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.CAMPFIRE_COOKING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.CAMPFIRE_COOKING, input, lastCustomRecipe);
                } else  {
                    return Optional.empty();
                }
                if (ceRecipe == null) {
                    return Optional.empty();
                }

                // Cache recipes, it might be incorrect on reloading
                injectedCacheCheck.lastCustomRecipe(ceRecipe.id());
                // It doesn't matter at all
                injectedCacheCheck.lastRecipe(resourceLocation);
                return Optional.of(Optional.ofNullable(recipeManager.nmsRecipeHolderByRecipe(ceRecipe)).orElse(pair.getSecond()));
            } else {
                return Optional.empty();
            }
        }
    }

    public static class GetRecipeForMethodInterceptor1_20_5 {
        public static final GetRecipeForMethodInterceptor1_20_5 INSTANCE = new GetRecipeForMethodInterceptor1_20_5();

        @SuppressWarnings("unchecked")
        @RuntimeType
        public Object intercept(@This Object thisObj, @AllArguments Object[] args) throws Exception {
            Object mcRecipeManager = BukkitRecipeManager.nmsRecipeManager();
            InjectedCacheCheck injectedCacheCheck = (InjectedCacheCheck) thisObj;
            Object type = injectedCacheCheck.recipeType();
            Object lastRecipe = injectedCacheCheck.lastRecipe();
            Optional<Object> optionalRecipe = (Optional<Object>) FastNMS.INSTANCE.method$RecipeManager$getRecipeFor(mcRecipeManager, type, args[0], args[1], lastRecipe);
            if (optionalRecipe.isPresent()) {
                Object holder = optionalRecipe.get();
                Object id = FastNMS.INSTANCE.field$RecipeHolder$id(holder);
                Key recipeId = Key.of(id.toString());
                BukkitRecipeManager recipeManager = BukkitRecipeManager.instance();

                ItemStack itemStack;
                List<Object> items;
                if (type == MRecipeTypes.CAMPFIRE_COOKING) {
                    items = (List<Object>) CoreReflections.field$SimpleContainer$items.get(args[0]);
                } else {
                    items = (List<Object>) CoreReflections.field$AbstractFurnaceBlockEntity$items.get(args[0]);
                }
                itemStack = FastNMS.INSTANCE.method$CraftItemStack$asCraftMirror(items.get(0));

                // it's a recipe from other plugins
                boolean isCustom = recipeManager.isCustomRecipe(recipeId);
                if (!isCustom) {
                    injectedCacheCheck.lastRecipe(id);
                    return optionalRecipe;
                }

                Item<ItemStack> wrappedItem = BukkitItemManager.instance().wrap(itemStack);
                Optional<Holder.Reference<Key>> idHolder = BuiltInRegistries.OPTIMIZED_ITEM_ID.get(wrappedItem.id());
                if (idHolder.isEmpty()) {
                    return Optional.empty();
                }

                SingleItemInput<ItemStack> input = new SingleItemInput<>(new OptimizedIDItem<>(idHolder.get(), itemStack));
                CustomCookingRecipe<ItemStack> ceRecipe;
                Key lastCustomRecipe = injectedCacheCheck.lastCustomRecipe();
                if (type == MRecipeTypes.SMELTING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.SMELTING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.BLASTING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.BLASTING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.SMOKING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.SMOKING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.CAMPFIRE_COOKING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.CAMPFIRE_COOKING, input, lastCustomRecipe);
                } else  {
                    return Optional.empty();
                }
                if (ceRecipe == null) {
                    return Optional.empty();
                }

                // Cache recipes, it might be incorrect on reloading
                injectedCacheCheck.lastCustomRecipe(ceRecipe.id());
                // It doesn't matter at all
                injectedCacheCheck.lastRecipe(id);
                return Optional.of(Optional.ofNullable(recipeManager.nmsRecipeHolderByRecipe(ceRecipe)).orElse(holder));
            } else {
                return Optional.empty();
            }
        }
    }

    public static class GetRecipeForMethodInterceptor1_21 {
        public static final GetRecipeForMethodInterceptor1_21 INSTANCE = new GetRecipeForMethodInterceptor1_21();

        @SuppressWarnings("unchecked")
        @RuntimeType
        public Object intercept(@This Object thisObj, @AllArguments Object[] args) throws Exception {
            Object mcRecipeManager = BukkitRecipeManager.nmsRecipeManager();
            InjectedCacheCheck injectedCacheCheck = (InjectedCacheCheck) thisObj;
            Object type = injectedCacheCheck.recipeType();
            Object lastRecipe = injectedCacheCheck.lastRecipe();
            Optional<Object> optionalRecipe = (Optional<Object>) FastNMS.INSTANCE.method$RecipeManager$getRecipeFor(mcRecipeManager, type, args[0], args[1], lastRecipe);
            if (optionalRecipe.isPresent()) {
                Object holder = optionalRecipe.get();
                Object id = FastNMS.INSTANCE.field$RecipeHolder$id(holder);
                Key recipeId = Key.of(id.toString());
                BukkitRecipeManager recipeManager = BukkitRecipeManager.instance();
                ItemStack itemStack = FastNMS.INSTANCE.method$CraftItemStack$asCraftMirror(CoreReflections.field$SingleRecipeInput$item.get(args[0]));

                // it's a recipe from other plugins
                boolean isCustom = recipeManager.isCustomRecipe(recipeId);
                if (!isCustom) {
                    injectedCacheCheck.lastRecipe(id);
                    return optionalRecipe;
                }

                Item<ItemStack> wrappedItem = BukkitItemManager.instance().wrap(itemStack);
                Optional<Holder.Reference<Key>> idHolder = BuiltInRegistries.OPTIMIZED_ITEM_ID.get(wrappedItem.id());
                if (idHolder.isEmpty()) {
                    return Optional.empty();
                }

                SingleItemInput<ItemStack> input = new SingleItemInput<>(new OptimizedIDItem<>(idHolder.get(), itemStack));
                CustomCookingRecipe<ItemStack> ceRecipe;
                Key lastCustomRecipe = injectedCacheCheck.lastCustomRecipe();
                if (type == MRecipeTypes.SMELTING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.SMELTING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.BLASTING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.BLASTING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.SMOKING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.SMOKING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.CAMPFIRE_COOKING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.CAMPFIRE_COOKING, input, lastCustomRecipe);
                } else  {
                    return Optional.empty();
                }
                if (ceRecipe == null) {
                    return Optional.empty();
                }

                // Cache recipes, it might be incorrect on reloading
                injectedCacheCheck.lastCustomRecipe(ceRecipe.id());
                // It doesn't matter at all
                injectedCacheCheck.lastRecipe(id);
                return Optional.of(Optional.ofNullable(recipeManager.nmsRecipeHolderByRecipe(ceRecipe)).orElse(holder));
            } else {
                return Optional.empty();
            }
        }
    }

    public static class GetRecipeForMethodInterceptor1_21_2 {
        public static final GetRecipeForMethodInterceptor1_21_2 INSTANCE = new GetRecipeForMethodInterceptor1_21_2();

        @SuppressWarnings("unchecked")
        @RuntimeType
        public Object intercept(@This Object thisObj, @AllArguments Object[] args) throws Exception {
            Object mcRecipeManager = BukkitRecipeManager.nmsRecipeManager();
            InjectedCacheCheck injectedCacheCheck = (InjectedCacheCheck) thisObj;
            Object type = injectedCacheCheck.recipeType();
            Object lastRecipe = injectedCacheCheck.lastRecipe();
            Optional<Object> optionalRecipe = (Optional<Object>) FastNMS.INSTANCE.method$RecipeManager$getRecipeFor(mcRecipeManager, type, args[0], args[1], lastRecipe);
            if (optionalRecipe.isPresent()) {
                Object holder = optionalRecipe.get();
                Object id = FastNMS.INSTANCE.field$RecipeHolder$id(holder);
                Object resourceLocation = FastNMS.INSTANCE.field$ResourceKey$location(id);
                Key recipeId = Key.of(resourceLocation.toString());
                BukkitRecipeManager recipeManager = BukkitRecipeManager.instance();
                ItemStack itemStack = FastNMS.INSTANCE.method$CraftItemStack$asCraftMirror(CoreReflections.field$SingleRecipeInput$item.get(args[0]));

                // it's a recipe from other plugins
                boolean isCustom = recipeManager.isCustomRecipe(recipeId);
                if (!isCustom) {
                    injectedCacheCheck.lastRecipe(id);
                    return optionalRecipe;
                }

                Item<ItemStack> wrappedItem = BukkitItemManager.instance().wrap(itemStack);
                Optional<Holder.Reference<Key>> idHolder = BuiltInRegistries.OPTIMIZED_ITEM_ID.get(wrappedItem.id());
                if (idHolder.isEmpty()) {
                    return Optional.empty();
                }

                SingleItemInput<ItemStack> input = new SingleItemInput<>(new OptimizedIDItem<>(idHolder.get(), itemStack));
                CustomCookingRecipe<ItemStack> ceRecipe;
                Key lastCustomRecipe = injectedCacheCheck.lastCustomRecipe();
                if (type == MRecipeTypes.SMELTING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.SMELTING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.BLASTING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.BLASTING, input, lastCustomRecipe);
                } else if (type == MRecipeTypes.SMOKING) {
                    ceRecipe = (CustomCookingRecipe<ItemStack>) recipeManager.recipeByInput(RecipeTypes.SMOKING, input, lastCustomRecipe);
                } else {
                    return Optional.empty();
                }
                if (ceRecipe == null) {
                    return Optional.empty();
                }

                // Cache recipes, it might be incorrect on reloading
                injectedCacheCheck.lastCustomRecipe(ceRecipe.id());
                // It doesn't matter at all
                injectedCacheCheck.lastRecipe(id);
                return Optional.of(Optional.ofNullable(recipeManager.nmsRecipeHolderByRecipe(ceRecipe)).orElse(holder));
            } else {
                return Optional.empty();
            }
        }
    }
}
