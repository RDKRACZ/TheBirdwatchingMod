package com.ikerleon.birdwmod.entity.northamerica;

import com.ikerleon.birdwmod.entity.DiurnalBirdEntity;
import com.ikerleon.birdwmod.items.InitItems;
import com.ikerleon.birdwmod.util.SoundHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

public class EasternBluebirdEntity extends DiurnalBirdEntity {

    public EasternBluebirdEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public int setBirdVariants() {
        return 1;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if(this.onGround && !isSleeping()){
            if(this.getGender()==0) {
                return SoundHandler.BLUEBIRD_SONG;
            }
            else{
                return SoundHandler.BLUEBIRD_CALL;
            }
        }
        else{
            return null;
        }
    }

    public static DefaultAttributeContainer.Builder createBirdAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20D).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.70D).add(EntityAttributes.GENERIC_MAX_HEALTH, 5.0D);
    }

    @Override
    public void mobTick() {
        if (!this.world.isClient() && !this.isBaby() && --this.timeUntilNextFeather <= 0)
        {
            if(this.getGender()==0){
                this.dropItem(InitItems.EASTERNBLUEBIRDFEATHER_MALE, 1);
            }
            else{
                this.dropItem(InitItems.EASTERNBLUEBIRDFEATHER_FEMALE, 1);
            }
            this.timeUntilNextFeather = this.random.nextInt(10000) + 10000;
        }
        super.mobTick();
    }

    @Override
    protected void dropLoot(DamageSource source, boolean causedByPlayer) {
        if(this.isOnFire())
            this.dropItem(InitItems.SMALLCOOCKEDMEAT, 1);
        else
            this.dropItem(InitItems.SMALLRAWMEAT, 1);
    }

    @Override
    public boolean goesToFeeders() {
        return true;
    }

    @Override
    public boolean isAquatic() {
        return false;
    }

    @Override
    public boolean isGroupBird() {
        return true;
    }

    @Override
    public PassiveEntity createChild(PassiveEntity mate) {
        return (EasternBluebirdEntity)this.getType().create(this.world);
    }
}