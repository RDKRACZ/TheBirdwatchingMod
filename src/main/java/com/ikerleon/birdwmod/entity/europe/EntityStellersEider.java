package com.ikerleon.birdwmod.entity.europe;

import com.ikerleon.birdwmod.entity.EntityBirdDiurnal;
import com.ikerleon.birdwmod.init.BirdwmodItems;
import com.ikerleon.birdwmod.util.handlers.SoundHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityStellersEider extends EntityBirdDiurnal {

    public EntityStellersEider(World worldIn) {
        super(worldIn);
        this.setSize(0.4f, 0.6f);
    }

    @Override
    public int setBirdVariants() {
        return 1;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if((this.onGround || this.inWater) && this.getGender()==0){
            return SoundHandler.EIDER_CALL;
        }
        else{
            return SoundHandler.EIDER_FLYING;
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }

    @Override
    public void onLivingUpdate() {
        if(this.inWater && !this.isChild()){
            this.motionY=0;
        }
        if (!this.world.isRemote && !this.isChild() && --this.timeUntilNextFeather <= 0)
        {
            if(this.getGender()==0){
                this.dropItem(BirdwmodItems.STELLERSEIDERFEATHER_MALE, 1);
            }
            else{
                this.dropItem(BirdwmodItems.STELLERSEIDERFEATHER_FEMALE, 1);
            }
            this.timeUntilNextFeather = this.rand.nextInt(10000) + 10000;
        }
        super.onLivingUpdate();
    }

    @Override
    public void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        if(this.isBurning())
            this.dropItem(BirdwmodItems.DUCKCOOCKEDMEAT, 1);
        else
            this.dropItem(BirdwmodItems.DUCKRAWMEAT, 1);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityStellersEider(this.world);
    }
}
