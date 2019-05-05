package com.ikerleon.birdwmod.entity.northamerica;

import com.ikerleon.birdwmod.entity.EntityBirdDiurnal;
import com.ikerleon.birdwmod.entity.europe.EntityStellersEider;
import com.ikerleon.birdwmod.init.BirdwmodItems;
import com.ikerleon.birdwmod.util.PosesUtil;
import com.ikerleon.birdwmod.util.handlers.SoundHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.soggymustache.bookworm.client.animation.lerp.Animation;
import net.soggymustache.bookworm.client.animation.lerp.AnimationHandler;

public class EntityGreenHeron extends EntityBirdDiurnal {

    public static final int SPEAK = 0;
    public AnimationHandler animator = new AnimationHandler();

    public EntityGreenHeron(World worldIn) {
        super(worldIn);
        this.setSize(0.3f, 0.3f);

        if(this.world.isRemote) {
            Animation speak = new Animation(PosesUtil.HERON_FLYING_POSE, PosesUtil.HERON_FLYING_SPEAKING_POSE, PosesUtil.HERON_FLYING_POSE);
            speak.speed = 0.2F;
            animator.abruptStopping = false;
            animator.addAnimation(SPEAK, speak);
        }
    }

    @Override
    public int setBirdVariants() {
        return 3;
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if(!(this.onGround || this.inWater)){
            if(this.world.isRemote){
                this.animator.play(SPEAK);
            }
            return SoundHandler.HERON_FLYING;
        }
        else {
            return null;
        }
    }

    @Override
    public void onLivingUpdate() {
        if (!this.world.isRemote && !this.isChild() && --this.timeUntilNextFeather <= 0)
        {
            this.dropItem(BirdwmodItems.GREENHERONFEATHER, 1);
            this.timeUntilNextFeather = this.rand.nextInt(10000) + 10000;
        }
        super.onLivingUpdate();
    }

    @Override
    public void onUpdate() {
        if(this.world.isRemote)
            animator.onEntityUpdate(this);

        super.onUpdate();
    }

    @Override
    public void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        if(this.isBurning())
            this.dropItem(BirdwmodItems.HERONCOOCKEDMEAT, 1);
        else
            this.dropItem(BirdwmodItems.HERONRAWMEAT, 1);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityGreenHeron(this.world);
    }
}