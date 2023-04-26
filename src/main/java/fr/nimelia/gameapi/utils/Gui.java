package fr.nimelia.gameapi.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.stream.IntStream;

public interface Gui {
    void open(Player player);
    
    default void fill(Inventory inventory) {
        ItemStack glass = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11)).setName("§c").build();
        IntStream.of(0, 1, 7, 8, 9, 17, 36, 44, 45, 46, 52, 53)
            .forEach(slot -> inventory.setItem(slot, glass));
    }
}