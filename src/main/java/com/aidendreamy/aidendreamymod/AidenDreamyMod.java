package com.aidendreamy.aidendreamymod;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentType;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import net.minecraft.util.Identifier;

public class AidenDreamyMod implements ModInitializer {
    public static final String MODID = "aidendreamymod";

    // -------------------- Block: smooth_amethyste --------------------
    // 1.21.2+ requires that Settings have a registry key set before constructing the Block/Item.
    private static final Identifier SMOOTH_AMETHYSTE_ID = Identifier.of(MODID, "smooth_amethyste");
    private static final RegistryKey<Block> SMOOTH_AMETHYSTE_KEY =
            RegistryKey.of(RegistryKeys.BLOCK, SMOOTH_AMETHYSTE_ID);

    public static final Block SMOOTH_AMETHYSTE_BLOCK =
            Blocks.register(
                    SMOOTH_AMETHYSTE_KEY,
                    Block::new,
                    AbstractBlock.Settings.copy(Blocks.AMETHYST_BLOCK)
            );

    // -------------------- Armor materials (Minecraft 1.21.2+) --------------------
    // Each ArmorMaterial has a modelId (Identifier). In 1.21.2+, that modelId maps to:
    //
    // assets/aidendreamymod/models/equipment/<path>.json
    // which then points to textures:
    // assets/aidendreamymod/textures/entity/equipment/humanoid/<path>.png
    // assets/aidendreamymod/textures/entity/equipment/humanoid_leggings/<path>.png
    //
    // IMPORTANT:
    // - textureBase here controls your WORN armor look (equipment model + PNG names)
    // - registerSet(...) controls your ITEM ids (recipes/models/item/*.json)
    public static final ArmorMaterial AME_MATERIAL = netheriteLike("amenetherite");
    public static final ArmorMaterial BLUE_MATERIAL = netheriteLike("lapisnetherite");
    public static final ArmorMaterial GOLD_MATERIAL = netheriteLike("goldnetherite");
    public static final ArmorMaterial COPPER_MATERIAL = netheriteLike("coppernetherite");
    public static final ArmorMaterial DARK_MATERIAL = netheriteLike("darknetherite");
    public static final ArmorMaterial AQUA_MATERIAL = netheriteLike("aquanetherite");
    public static final ArmorMaterial EMERALD_MATERIAL = netheriteLike("emeraldnetherite");
    public static final ArmorMaterial QUARTZ_MATERIAL = netheriteLike("quartznetherite");
    public static final ArmorMaterial REDSTONE_MATERIAL = netheriteLike("rednetherite");

    @Override
    public void onInitialize() {
        // Registers the matching BlockItem (and handles required registry key wiring)
        Items.register(SMOOTH_AMETHYSTE_BLOCK);

        // Register armor sets (item IDs MUST match your recipe result IDs + models/item/*.json)
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

    private static void registerSet(String prefix, ArmorMaterial material) {
        registerArmor(prefix + "_helmet", EquipmentType.HELMET, material);
        registerArmor(prefix + "_chestplate", EquipmentType.CHESTPLATE, material);
        registerArmor(prefix + "_leggings", EquipmentType.LEGGINGS, material);
        registerArmor(prefix + "_boots", EquipmentType.BOOTS, material);
    }

    /**
     * 1.21.2+ requires item Settings to have the item's RegistryKey set (registryKey(...)),
     * otherwise you'll crash at runtime with "Item id not set".
     */
    private static void registerArmor(String id, EquipmentType type, ArmorMaterial material) {
        Identifier itemId = Identifier.of(MODID, id);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, itemId);

        Item.Settings settings = material
                .applySettings(new Item.Settings().fireproof(), type)
                .registryKey(itemKey);

        Registry.register(Registries.ITEM, itemKey, new Item(settings));
    }

    /**
     * Creates a Netherite-like ArmorMaterial but with a custom modelId (texture base).
     *
     * With textureBase = "amenetherite", Minecraft will look for:
     * - assets/aidendreamymod/models/equipment/amenetherite.json
     * Which should reference textures:
     * - textures/entity/equipment/humanoid/amenetherite.png
     * - textures/entity/equipment/humanoid_leggings/amenetherite.png
     */
    private static ArmorMaterial netheriteLike(String textureBase) {
        ArmorMaterial base = ArmorMaterials.NETHERITE; // vanilla netherite stats/repair/sound

        return new ArmorMaterial(
                base.durability(),
                base.defense(),
                base.enchantmentValue(),
                base.equipSound(),
                base.toughness(),
                base.knockbackResistance(),
                base.repairIngredient(),
                Identifier.of(MODID, textureBase) // modelId / texture base
        );
    }
}
