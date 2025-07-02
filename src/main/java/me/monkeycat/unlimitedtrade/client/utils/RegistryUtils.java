package me.monkeycat.unlimitedtrade.client.utils;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.List;

public class RegistryUtils {
    public static String getItemId(Item item) {
        return Registries.ITEM.getId(item).toString();
    }

    public static String getBlockId(Block block) {
        return Registries.BLOCK.getId(block).toString();
    }

    public static ImmutableList<String> getItemIds(List<Item> items) {
        return ImmutableList.copyOf(items.stream().map(RegistryUtils::getItemId).toList());
    }

    public static ImmutableList<String> getBlockIds(List<Block> blocks) {
        return ImmutableList.copyOf(blocks.stream().map(RegistryUtils::getBlockId).toList());
    }
}
