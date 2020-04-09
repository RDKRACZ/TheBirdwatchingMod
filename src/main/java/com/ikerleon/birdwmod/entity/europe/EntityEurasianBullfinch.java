package com.ikerleon.birdwmod.entity.europe;

import com.ikerleon.birdwmod.entity.EntityBirdDiurnal;
import com.ikerleon.birdwmod.init.BirdwmodItems;
import com.ikerleon.birdwmod.util.handlers.SoundHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityEurasianBullfinch extends EntityBirdDiurnal {

    public EntityEurasianBullfinch(World worldIn) {
        super(worldIn);
        this.setSize(0.3f, 0.3f);
    }

    @Override
    public int setBirdVariants() {
        return 1;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if(this.onGround && !isSleeping()){
            return SoundHandler.BULLFINCH_CALL;
        }
        else{
            return null;
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0D);
    }

    @Override
    public void onLivingUpdate() {
        if (!this.world.isRemote && !this.isChild() && --this.timeUntilNextFeather <= 0)
        {
            if(this.getGender()==0){
                this.dropItem(BirdwmodItems.EURASIANBULLFINCHDFEATHER_MALE, 1);
            }
            else{
                this.dropItem(BirdwmodItems.EURASIANBULLFINCHDFEATHER_FEMALE, 1);
            }
            this.timeUntilNextFeather = this.rand.nextInt(10000) + 10000;
        }
        super.onLivingUpdate();
    }

    @Override
    public void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        if(this.isBurning())
            this.dropItem(BirdwmodItems.PASSERINECOOCKEDMEAT, 1);
        else
            this.dropItem(BirdwmodItems.PASSERINERAWMEAT, 1);
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
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityEurasianBullfinch(this.world);
    }
}
