package fr.nimelia.gameapi.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Blendman974
 */
public class ItemBuilder implements Cloneable {

    private ItemStack item;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material);
    }

    public ItemStack build() {
        return this.item;
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this.item.clone());
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = this.item.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(Short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        this.item.setDurability((short) (durability));
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLores(List<String> list) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(list);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLores(String... list) {
        setLores(Arrays.asList(list));
        return this;
    }

    public ItemBuilder addLore(String lore) {
        ItemMeta meta = this.item.getItemMeta();
        List<String> lores = meta.getLore();
        if (lores == null)
            lores = new ArrayList<>();
        lores.add((lore != null) ? lore : " ");
        meta.setLore(lores);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setGlow(boolean glow) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItemstack = CraftItemStack.asNMSCopy(this.item);
        NBTTagCompound tag;
        if (glow) {
            tag = (!nmsItemstack.hasTag()) ? new NBTTagCompound() : nmsItemstack.getTag();
            tag.set("ench", new NBTTagList());
            nmsItemstack.setTag(tag);
            this.item = CraftItemStack.asCraftMirror(nmsItemstack);
        } else if (nmsItemstack.hasTag()) {
            tag = nmsItemstack.getTag();
            if (tag.hasKey("ench")) {
                tag.remove("ench");
                nmsItemstack.setTag(tag);
                this.item = CraftItemStack.asCraftMirror(nmsItemstack);
            }
        }
        return this;
    }

    public ItemBuilder setColorLeather(int red, int green, int blue) {
        if (item.getType().toString().contains("LEATHER")) {
            LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
            lam.setColor(Color.fromRGB(red, green, blue));
            item.setItemMeta(lam);
        }
        return this;
    }

    public ItemBuilder setColorLeather(Color colorLeather) {
        if (item.getType().toString().contains("LEATHER")) {
            LeatherArmorMeta lam = ((LeatherArmorMeta) item.getItemMeta());
            lam.setColor(colorLeather);
            item.setItemMeta(lam);
        }
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int lvl) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(enchantment, lvl, true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantment(int lvl, Enchantment... enchantments) {
        ItemMeta meta = this.item.getItemMeta();
        for (Enchantment enchantment : enchantments)
            meta.addEnchant(enchantment, lvl, true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addStoredEnchantment(Enchantment storedEnchantment, int lvl) {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.item.getItemMeta();
            meta.addStoredEnchant(storedEnchantment, lvl, true);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setItemFlags(List<ItemFlag> itemFlags) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta.getItemFlags() != null) {
            for (ItemFlag itemflag : new ArrayList<>(meta.getItemFlags()))
                meta.removeItemFlags(itemflag);
        }
        for (ItemFlag itemflag : itemFlags)
            meta.addItemFlags(itemflag);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag itemFlag) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(itemFlag);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addallItemsflags() {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setSkullMeta(SkullMeta skullMeta) {
        if (this.item.getType().equals(Material.SKULL_ITEM))
            this.item.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder setOwner(String owner) {
        if (this.item.getType().equals(Material.SKULL_ITEM)) {
            SkullMeta meta = (SkullMeta) this.item.getItemMeta();
            meta.setOwner(owner);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setSkin(String skin, String signature) {
        if (Material.SKULL_ITEM == this.item.getType()) {
            ItemMeta meta = this.item.getItemMeta();

            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", new Property("textures", skin, signature));

            try {
                Field field = meta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(meta, profile);
            } catch (Exception e) {
                return this;
            }

            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setSkin(String data) {
        if (Material.SKULL_ITEM == this.item.getType()) {
            ItemMeta meta = this.item.getItemMeta();

            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            profile.getProperties().removeAll("textures");
            profile.getProperties().put("textures", new Property("textures", data));

            try {
                Field field = meta.getClass().getDeclaredField("profile");
                field.setAccessible(true);
                field.set(meta, profile);
            } catch (Exception e) {
                return this;
            }

            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setSkinUncoded(String data) {
        if (Material.SKULL_ITEM == this.item.getType()) {
            if (!data.startsWith("http://textures.minecraft.net/texture/")) {
                data = "http://textures.minecraft.net/texture/" + data;
            }
            byte[] d = Base64.getEncoder()
                    .encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[]{data}).getBytes());
            setSkin(new String(d));
        }
        return this;
    }


    public ItemBuilder setBannerMeta(BannerMeta bannerMeta) {
        if (this.item.getType().equals(Material.BANNER))
            this.item.setItemMeta(bannerMeta);
        return this;
    }

    public ItemBuilder setBasecolor(DyeColor dyeColor) {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta) this.item.getItemMeta();
            meta.setBaseColor(dyeColor);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setPatterns(List<Pattern> patterns) {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta) this.item.getItemMeta();
            meta.setPatterns(patterns);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addPattern(Pattern pattern) {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta) this.item.getItemMeta();
            meta.addPattern(pattern);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder addCustomEffect(PotionEffectType type, int amplifier, int duration) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound tag = (itemStack.getTag() != null) ? itemStack.getTag() : new NBTTagCompound();
        if (!tag.hasKey("CustomPotionEffects")) {
            tag.set("CustomPotionEffects", new NBTTagList());
            itemStack.setTag(tag);
        }

        tag = new NBTTagCompound();
        tag.setByte("Id", (byte) type.getId());
        tag.setByte("Amplifier", (byte) amplifier);
        tag.setInt("Duration", duration);

        ((NBTTagList) itemStack.getTag().get("CustomPotionEffects")).add(tag);
        this.item = CraftItemStack.asCraftMirror(itemStack);
        return this;
    }

    public ItemBuilder addAttributEffect(String name, AttributeName attributeName, double level) {
        net.minecraft.server.v1_8_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(item);

        NBTTagCompound itemTags = (itemStack.getTag() != null) ? itemStack.getTag() : new NBTTagCompound();
        if (!itemTags.hasKey("AttributeModifiers")) {
            itemTags.set("AttributeModifiers", new NBTTagList());
            itemStack.setTag(itemTags);
        }

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("AttributeName", attributeName.getName());
        tag.setString("Name", name);
        tag.setInt("Operation", 1);
        tag.setDouble("Amount", level);
        tag.setLong("UUIDMost", 1L);
        tag.setLong("UUIDLeast", 1L);

        ((NBTTagList) itemTags.get("AttributeModifiers")).add(tag);
        this.item = CraftItemStack.asCraftMirror(itemStack);
        return this;
    }

    public ItemBuilder playerSkull() {
        if (Material.SKULL_ITEM == this.item.getType()) {
            setDurability(3);
        }
        return this;
    }

    public enum AttributeName {
        MAXHEALTH("generic.maxHealth"),
        KNOCKBACK_RESISTANCE("generic.knockbackResistance"),
        SPEED("generic.movementSpeed"),
        FOLLOW_RANGE("generic.followRange"),
        ATTACK_DAMAGE("generic.attackDamage"),
        LUCK("generic.luck"),
        HORSE_JUMP_STRENGTH("horse.jumpStrength"),
        ZOMBIE_SPAWN_REINFORCEMENTS("zombie.spawnReinforcements");

        private final String name;

        AttributeName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}