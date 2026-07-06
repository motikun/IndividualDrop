package net.motikun.individualdrop.event.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Collection;

public class MobLootEventUtil {
    public static Collection<ItemStack> getMobLootTable(ServerLevel serverLevel, LivingEntity entity, DamageSource damageSource) {
        ResourceKey<LootTable> resourcekey = entity.getLootTable();
        LootTable loottable = serverLevel.getServer().reloadableRegistries().getLootTable(resourcekey);

        LootParams.Builder lootparams$builder = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
                .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.getEntity())
                .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity());

        if (damageSource.getEntity() instanceof Player lastHurtByPlayer) {
            lootparams$builder = lootparams$builder.withParameter(
                    LootContextParams.LAST_DAMAGE_PLAYER, lastHurtByPlayer).withLuck(lastHurtByPlayer.getLuck());
        }

        LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
        return loottable.getRandomItems(lootparams, entity.getLootTableSeed());
    }
}