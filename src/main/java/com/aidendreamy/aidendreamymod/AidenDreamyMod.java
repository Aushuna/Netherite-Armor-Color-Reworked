package com.aidendreamy.aidendreamymod;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import net.minecraft.recipe.Ingredient;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

import net.minecraft.sound.SoundEvents;

import net.minecraft.util.Identifier;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class AidenDreamyMod implements ModInitializer {
    public static final String MODID = "aidendreamymod";

    // -------------------- Block: smooth_amethyste --------------------
    // You have block tags + block loot table referencing this, so it must be a real block.
    public static final Block SMOOTH_AMETHYSTE_BLOCK = Registry.register(
            Registries.BLOCK,
            Identifier.of(MODID, "smooth_amethyste"),
            new Block(AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK))
    );

    // -------------------- Armor materials mapped to YOUR worn-texture PNG base names --------------------
    // These will look for:
    // assets/aidendreamymod/textures/models/armor/<base>_layer_1.png
    // assets/aidendreamymod/textures/models/armor/<base>_layer_2.png
    public static final RegistryEntry<ArmorMaterial> AME_MATERIAL =
            registerNetheriteLikeMaterial("amenetherite");

    public static final RegistryEntry<ArmorMaterial> BLUE_MATERIAL =
            registerNetheriteLikeMaterial("lappisnetherite");

    public static final RegistryEntry<ArmorMaterial> GOLD_MATERIAL =
            registerNetheriteLikeMaterial("goldnetherite");

    // NOTE: Your file name has triple 'p': copppernetherite_layer_1/2.png
    public static final RegistryEntry<ArmorMaterial> COPPER_MATERIAL =
            registerNetheriteLikeMaterial("copppernetherite");

    public static final RegistryEntry<ArmorMaterial> DARK_MATERIAL =
            registerNetheriteLikeMaterial("darknetherite");

    public static final RegistryEntry<ArmorMaterial> AQUA_MATERIAL =
            registerNetheriteLikeMaterial("aquanetherite");

    public static final RegistryEntry<ArmorMaterial> EMERALD_MATERIAL =
            registerNetheriteLikeMaterial("emeraldnetherite");

    // You said:
    // nnetherite_layer_1/2.png (used for quartz)
    // rednetherite_layer_1/2.png (used for redstone)
    public static final RegistryEntry<ArmorMaterial> QUARTZ_MATERIAL =
            registerNetheriteLikeMaterial("nnetherite");

    public static final RegistryEntry<ArmorMaterial> REDSTONE_MATERIAL =
            registerNetheriteLikeMaterial("rednetherite");

    @Override
    public void onInitialize() {
        // Register block item (inventory + recipes use the item form)
        Registry.register(
                Registries.ITEM,
                Identifier.of(MODID, "smooth_amethyste"),
                new BlockItem(SMOOTH_AMETHYSTE_BLOCK, new Item.Settings())
        );

        // Register armor sets (item IDs must match your recipe result IDs)
        registerSet("netherite_ame_dye", AME_MATERIAL);
        registerSet("netherite_blue_dye", BLUE_MATERIAL);
        registerSet("netherite_gold_dye", GOLD_MATERIAL);
        registerSet("netherite_copper_dye", COPPER_MATERIAL);
        registerSet("netherite_dark_dye", DARK_MATERIAL);
        registerSet("netherite_aqua_dye", AQUA_MATERIAL);
        registerSet("netherite_emerald_dye", EMERALD_MATERIAL);
        registerSet("netherite_quartz_dye", QUARTZ_MATERIAL);
        registerSet("netherite_redstone_dye", REDSTONE_MATERIAL);
    }

    private static void registerSet(String prefix, RegistryEntry<ArmorMaterial> material) {
        registerArmor(prefix + "_helmet", ArmorItem.Type.HELMET, material);
        registerArmor(prefix + "_chestplate", ArmorItem.Type.CHESTPLATE, material);
        registerArmor(prefix + "_leggings", ArmorItem.Type.LEGGINGS, material);
        registerArmor(prefix + "_boots", ArmorItem.Type.BOOTS, material);
    }

    private static void registerArmor(String id, ArmorItem.Type type, RegistryEntry<ArmorMaterial> material) {
        Registry.register(
                Registries.ITEM,
                Identifier.of(MODID, id),
                new ArmorItem(material, type, new Item.Settings().fireproof())
        );
    }

    /**
     * Registers an ArmorMaterial with Netherite-like stats but custom worn-armor textures.
     *
     * Texture lookup is driven by the ArmorMaterial layer id.
     * With base = "aquanetherite", Minecraft will use:
     * - assets/aidendreamymod/textures/models/armor/aquanetherite_layer_1.png
     * - assets/aidendreamymod/textures/models/armor/aquanetherite_layer_2.png
     */
    private static RegistryEntry<ArmorMaterial> registerNetheriteLikeMaterial(String textureBase) {
        // Netherite defense values: helmet 3, chest 8, legs 6, boots 3
        EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        defense.put(ArmorItem.Type.HELMET, 3);
        defense.put(ArmorItem.Type.CHESTPLATE, 8);
        defense.put(ArmorItem.Type.LEGGINGS, 6);
        defense.put(ArmorItem.Type.BOOTS, 3);

        int enchantability = 15;
        float toughness = 3.0F;
        float knockbackResistance = 0.1F;

        Supplier<Ingredient> repairIngredient = () -> Ingredient.ofItems(Items.NETHERITE_INGOT);

        // One layer is enough for standard armor rendering
        List<ArmorMaterial.Layer> layers = List.of(
                new ArmorMaterial.Layer(Identifier.of(MODID, textureBase))
        );

        ArmorMaterial material = new ArmorMaterial(
                defense,
                enchantability,
                SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
                repairIngredient,
                layers,
                toughness,
                knockbackResistance
        );

        // Register the material so ArmorItem can hold a RegistryEntry<ArmorMaterial>
        return Registry.registerReference(
                Registries.ARMOR_MATERIAL,
                Identifier.of(MODID, textureBase),
                material
        );
    }
}
