package com.peeteer.cobblemondetector.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peeteer.cobblemondetector.client.config.CobblemonDetectorConfig;
import com.peeteer.cobblemondetector.client.config.ConfigBuilder;

public class CobblemonDetectorClient implements ClientModInitializer {
    public ArrayList<UUID> cobblemonCache = new ArrayList<>();
    public static final String MOD_ID = "pokefinder";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public CobblemonDetectorConfig config = ConfigBuilder.make();
    public String[] allowList = config.getCombinedAllowlist();
    
    public final static Identifier NOTIFICATION_SOUND_ID = new Identifier(MOD_ID + ":pla_notification");
    public final static SoundEvent NOTIFICATION_SOUND_EVENT = SoundEvent.of(NOTIFICATION_SOUND_ID);

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;
            
            if (client.world == null) {
                return;
            }

            for (Entity entity : client.world.getEntities()) {
                if (
                    player == null
                    || !entity.getType().toString().equals("entity.cobblemon.pokemon")
                    || cobblemonCache.contains(entity.getUuid())
                ) {
                    continue;
                }
                cobblemonCache.add(entity.getUuid());

                // using PokemonEntity is pointless since the
                // attributes of Pokemon are server-only
                // so isPlayerOwned() returns its default-value (false)
                // PokemonEntity pokemonEntity = (PokemonEntity) entity;

                for (String allowedPokemon : this.allowList) {
                    if (allowedPokemon.equals(entity.getName().getString().toLowerCase())) {
                        int x = (int) entity.getX();
                        int y = (int) entity.getY();
                        int z = (int) entity.getZ();

                        Text message = Text.literal("Found ")
                            .append(entity.getName())
                            .append(Text.literal(" at " + "X: " + x + " Y: " + y + " Z: " + z));
                        player.sendMessage(message);

                        this.playSoundNotification(player);

                        break;
                    }
                }
                continue;
            }
        });
    }

    public void playSoundNotification(PlayerEntity player){
        player.playSound(NOTIFICATION_SOUND_EVENT, SoundCategory.NEUTRAL, 10f, 1f);
    }
}
