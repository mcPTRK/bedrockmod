package pl.bedrockmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.glfw.GLFW;

public class BedrockKey implements ClientModInitializer {
    private static KeyBinding key;

    @Override
    public void onInitializeClient() {
        key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bedrockmod.bedrock",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.bedrockmod"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (key.wasPressed()) {
                if (client.player == null || client.interactionManager == null) return;

                if (client.player.isCreative()) {
                    int hotbar = client.player.getInventory().selectedSlot;
                    ItemStack bedrock = new ItemStack(Items.BEDROCK);
                    client.player.getInventory().setStack(hotbar, bedrock);
                    client.interactionManager.clickCreativeStack(bedrock, 36 + hotbar);
                } else {
                    client.player.networkHandler.sendChatCommand(
                            "item replace entity @s weapon.mainhand with minecraft:bedrock");
                }
            }
        });
    }
}
