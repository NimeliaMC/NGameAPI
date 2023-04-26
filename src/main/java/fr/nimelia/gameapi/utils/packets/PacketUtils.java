package fr.nimelia.gameapi.utils.packets;

import net.minecraft.server.v1_8_R3.*;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtils {

    private static IChatBaseComponent toChatComponent(String text) {
        return IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}");
    }

    private static PlayerConnection getPlayerConnection(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }

    /**
     * Send a NMS packet to all online players.
     *
     * @param packet The NMS packet
     */
    public static void broadcastPacket(Packet<?> packet) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, packet));
    }

    /**
     * Send a NMS packet to the player.
     *
     * @param player The player
     * @param packet The NMS packet
     */
    public static void sendPacket(Player player, Packet<?> packet) {
        getPlayerConnection(player).sendPacket(packet);
    }

    public static void setField(Object object, String fieldName, Object value) {
        try {
			FieldUtils.writeDeclaredField(object, fieldName, value, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a text in the player's action bar
     *
     * @param text   The text
     * @param player The player
     */
    public static void sendActionBar(String text, Player player) {
		sendPacket(player, new PacketPlayOutChat(toChatComponent(text), (byte) 2));
    }

    private static void sendTitleTimes(Player player, int fadeIn, int stay, int fadeOut) {
		sendPacket(player, new PacketPlayOutTitle(
			PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut
		));
    }

    /**
     * Send title and subtitle to the player with time settings.
     *
     * @param player   the player
     * @param title    the title
     * @param subtitle the subtitle
     * @param fadeIn   the fade in ticks
     * @param stay     the stay ticks
     * @param fadeOut  the fade out ticks
     */
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, toChatComponent(title));
        sendTitleTimes(player, fadeIn, stay, fadeOut);
        sendPacket(player, titlePacket);
        if (subtitle == null || subtitle.isEmpty()) return;
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, toChatComponent(subtitle));
        sendPacket(player, subtitlePacket);        
    }

    /**
     * Render a big text on the player's screen.
     *
     * @param title  the text
     * @param player the player
     */
    public static void sendTitle(String title, Player player) {
        sendTitle(player, title, null, 10, 30, 10);
    }

    /**
     * Render a text on the player screen.
     * You also need to send a title to show the sub title.
     *
     * @param subtitle the text
     * @param player   the player
     */
    public static void sendSubTitle(String subtitle, Player player) {
        sendTitle(player, "", subtitle, 10, 30, 10);
    }

    /**
     * Send a player list header and footer.
     *
     * @param header the header text
     * @param footer the footer text
     * @param player the player
     */
    public static void sendPlayerListHeaderFooter(String header, String footer, Player player) {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		setField(packet.getClass(), "a", toChatComponent(header));
		setField(packet.getClass(), "b", toChatComponent(footer));
        sendPacket(player, packet);
    }

    public static void spawnParticle(Location loc, EnumParticle particle) {
		Bukkit.getOnlinePlayers().forEach(p -> spawnParticle(p, loc, particle));
    }

    public static void spawnParticle(Player player, Location loc, EnumParticle particle) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.0f, 0.0f, 0.0f, 0.0f, 1);
		sendPacket(player, packet);
    }
}
