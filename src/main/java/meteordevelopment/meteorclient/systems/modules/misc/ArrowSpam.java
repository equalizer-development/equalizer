
// This file is NOT a part of the Meteor Client distribution.
/*
package meteordevelopment.meteorclient.systems.modules.misc;

import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;


import java.util.ArrayList;
import java.util.List;

public class ArrowSpam extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgEnchantments = settings.createGroup("Enchantments");

    private final Setting<Integer> arrowamount = sgGeneral.add(new IntSetting.Builder()
        .name("Projectile Amount")
        .description("How many projectiles to shoot")
        .defaultValue(3)
        .sliderMax(300)
        .sliderMin(1)
        .build()
    );

    private final Setting<Projectile> oProjectile = sgGeneral.add(new EnumSetting.Builder<Projectile>()
        .name("Projectile")
        .description("Select the projectile to shoot")
        .defaultValue(Projectile.Arrow)
        .build()
    );

    private final Setting<Boolean> enchantMultishotOn = sgEnchantments.add(new BoolSetting.Builder()
        .name("Multishot")
        .description("Use multishot")
        .defaultValue(false)
        .build());

    private final Setting<Integer> enchantMultishot = sgEnchantments.add(new IntSetting.Builder()
        .name("Multishot Level")
        .defaultValue(1)
        .sliderMax(255)
        .sliderMin(1)
        .visible(enchantMultishotOn::get)
        .build()
    );

    private final Setting<Boolean> enchantFlameOn = sgEnchantments.add(new BoolSetting.Builder()
        .name("Flame")
        .description("Use flame")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> enchantFlame = sgEnchantments.add(new IntSetting.Builder()
        .name("Flame level")
        .defaultValue(3)
        .sliderMax(255)
        .sliderMin(1)
        .visible(enchantFlameOn::get)
        .build()
    );


    private final Setting<Boolean> enchantPiercingOn = sgEnchantments.add(new BoolSetting.Builder()
        .name("Piercing")
        .description("Use piercing")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> enchantPiercing = sgEnchantments.add(new IntSetting.Builder()
        .name("Piercing Level")
        .defaultValue(3)
        .sliderMax(255)
        .sliderMin(1)
        .visible(enchantPiercingOn::get)
        .build()
    );

    public ArrowSpam() {
        super(Categories.Misc, "arrow-spam", "spams arrows from the player");
    }


    public void ArrowShoot(Projectile ProjectileType) {
        assert mc.player != null;
        assert mc.world != null;
        if (mc.player.getAbilities().creativeMode) {
            ItemStack item = new ItemStack(Items.CROSSBOW);
            List<ItemStack> projectilestack = new ArrayList<>();
            RegistryKey<Enchantment> enchantmentRegistry = mc.world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
            RegistryEntry<Enchantment> Piercing = enchantmentRegistry.entryOf(Enchantments.PIERCING);
            RegistryEntry<Enchantment> Multishot = enchantmentRegistry.entryOf(Enchantments.MULTISHOT);
            RegistryEntry<Enchantment> Flame = enchantmentRegistry.entryOf(Enchantments.FLAME);

            assert mc.world != null;

            if (ProjectileType == Projectile.Firework) {
                ItemStack proj = new ItemStack(Items.FIREWORK_ROCKET);

                if (enchantPiercingOn.get()) item.addEnchantment(Piercing, enchantPiercing.get());
                if (enchantMultishotOn.get()) item.addEnchantment(Multishot, enchantMultishot.get());
                if (enchantFlameOn.get()) item.addEnchantment(Flame, enchantFlame.get());

                for (int i = 0; i < arrowamount.get(); i++) {
                    projectilestack.add(proj);
                    item.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectilestack));
                }

                //ItemStack arrow = new ItemStack(Items.FIREWORK_ROCKET);
                //projectilestack.add(arrow);
                //item.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectiles));
            } else if (ProjectileType == Projectile.Arrow) {
                ItemStack proj = new ItemStack(Items.ARROW);

                if (enchantPiercingOn.get()) item.addEnchantment(Piercing, enchantPiercing.get());
                if (enchantMultishotOn.get()) item.addEnchantment(Multishot, enchantMultishot.get());
                if (enchantFlameOn.get()) item.addEnchantment(Flame, enchantFlame.get());

                for (int i = 0; i < arrowamount.get(); i++) {
                    projectilestack.add(proj);
                    item.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectilestack));
                }
            } else if (ProjectileType == Projectile.Spectral) {
                ItemStack proj = new ItemStack(Items.SPECTRAL_ARROW);

                if (enchantPiercingOn.get()) item.addEnchantment(Piercing, enchantPiercing.get());
                if (enchantMultishotOn.get()) item.addEnchantment(Multishot, enchantMultishot.get());
                if (enchantFlameOn.get()) item.addEnchantment(Flame, enchantFlame.get());

                for (int i = 0; i < arrowamount.get(); i++) {
                    projectilestack.add(proj);
                    item.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectilestack));
                }
            } else if (ProjectileType == Projectile.Tipped) {
                ItemStack proj = new ItemStack(Items.TIPPED_ARROW);

                if (enchantPiercingOn.get()) item.addEnchantment(Piercing, enchantPiercing.get());
                if (enchantMultishotOn.get()) item.addEnchantment(Multishot, enchantMultishot.get());
                if (enchantFlameOn.get()) item.addEnchantment(Flame, enchantFlame.get());

                for (int i = 0; i < arrowamount.get(); i++) {
                    projectilestack.add(proj);
                    item.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(projectilestack));
                }
            }

            assert mc.interactionManager != null;
            mc.interactionManager.clickCreativeStack(item, 36 + mc.player.getInventory().selectedSlot);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.player.getInventory();
            mc.interactionManager.clickCreativeStack(new ItemStack(Items.AIR), 36 + mc.player.getInventory().selectedSlot);
        }else {
            ChatUtils.errorPrefix("error","Insufficient Permissions, Needs creative mode!");
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        assert mc.player != null;
        if (mc.player.getAbilities().creativeMode) {
            ArrowShoot(oProjectile.get());
        }
    }

    public enum Projectile {
        Arrow,
        Firework,
        Spectral,
        Tipped
    }

}

*/