package net.motikun.individualdrop.event;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.motikun.individualdrop.entity.IndividualItemEntity;
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

        event.getDrops().clear();

        ResourceKey<LootTable> lootTableResourceKey = event.getEntity().getLootTable();
        LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(lootTableResourceKey);

        double x = event.getEntity().getX();
        double y = event.getEntity().getY();
        double z = event.getEntity().getZ();

        List<ServerPlayer> serverPlayers = level.getServer().getPlayerList().getPlayers();

        for (ServerPlayer player : serverPlayers) {
            LootParams.Builder builder = new LootParams.Builder(level)
                    .withParameter(LootContextParams.ORIGIN, event.getEntity().position())
                    .withParameter(LootContextParams.THIS_ENTITY, event.getEntity())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, event.getSource());

            if (event.getSource().getEntity() != null) {
                builder.withParameter(LootContextParams.ATTACKING_ENTITY, event.getSource().getEntity());
            }

            if (event.getSource().getDirectEntity() != null) {
                builder.withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, event.getSource().getDirectEntity());
            }

            LootParams params = builder.create(LootContextParamSets.ENTITY);

            Collection<ItemStack> dropItems = lootTable.getRandomItems(params);

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