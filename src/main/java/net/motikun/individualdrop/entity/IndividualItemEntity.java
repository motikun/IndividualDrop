package net.motikun.individualdrop.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.motikun.individualdrop.register.ModDataComponents;

import java.util.UUID;

public class IndividualItemEntity extends ItemEntity {
    private UUID ownerUUID;

    public IndividualItemEntity(EntityType<? extends IndividualItemEntity> type, Level level) {
        super(type, level);
    }

    public IndividualItemEntity(Level level, double x, double y, double z, ItemStack stack, UUID ownerUUID) {
        super(level, x, y, z, stack);
        this.ownerUUID = ownerUUID;

        this.setTarget(ownerUUID);
        this.setItem(stack);
        this.setPos(x, y, z);
    }

    @Override
    public boolean broadcastToPlayer(ServerPlayer player) {
        return player.getUUID().equals(this.ownerUUID);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.ownerUUID != null) {
            compound.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("OwnerUUID")) {
            this.ownerUUID = compound.getUUID("OwnerUUID");
            this.setTarget(this.ownerUUID);
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (ownerUUID.equals(player.getUUID())) {
            this.getItem().remove(ModDataComponents.ITEM_OWNER.get());
            super.playerTouch(player);
        }
    }
}
