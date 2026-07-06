package net.motikun.individualdrop.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.motikun.individualdrop.entity.IndividualItemEntity;
import net.motikun.individualdrop.event.util.MobLootEventUtil;
import net.motikun.individualdrop.register.ModDataComponents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.Collection;
import java.util.List;

@EventBusSubscriber
public class MobLootEventHandler {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof ServerPlayer) return;
        if (!(event.getEntity().level() instanceof ServerLevel level)) return;
        if (event.getEntity() instanceof WitherBoss) return;

        event.setCanceled(true);
        event.getDrops().clear();

        double x = event.getEntity().getX();
        double y = event.getEntity().getY();
        double z = event.getEntity().getZ();

        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        List<ServerPlayer> serverPlayers = level.getServer().getPlayerList().getPlayers();

        for (ServerPlayer player : serverPlayers) {

            Collection<ItemStack> dropItems =
                    MobLootEventUtil.getMobLootTable(level, entity, source);

            for (ItemStack stack : dropItems) {
                if (stack.isEmpty()) continue;

                stack.set(ModDataComponents.ITEM_OWNER.get(), player.getUUID());

                IndividualItemEntity individualItemEntity = new IndividualItemEntity(
                        level, x, y, z, stack, player.getUUID()
                );

                level.addFreshEntity(individualItemEntity);
            }
        }
    }
}