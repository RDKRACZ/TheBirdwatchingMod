package com.ikerleon.birdwmod.entity;

import com.ikerleon.birdwmod.blocks.InitBlocks;
import com.ikerleon.birdwmod.blocks.RingingNetBlock;
// TODO: re-enable
//import com.ikerleon.birdwmod.entity.goal.BirdSwimGoal;
//import com.ikerleon.birdwmod.entity.goal.EatFromFeedersGoal;
//import com.ikerleon.birdwmod.entity.goal.FollowLeaderGoal;
//import com.ikerleon.birdwmod.entity.move.MoveControlFlying;
import com.ikerleon.birdwmod.entity.europe.EurasianBullfinchEntity;
import com.ikerleon.birdwmod.items.InitItems;
import com.ikerleon.birdwmod.items.ItemBirdSpawnEgg;
import com.ikerleon.birdwmod.util.SoundHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class BirdEntity extends AnimalEntity implements IAnimatable {

    //Variables
    protected static final TrackedData<Integer> GENDER;
    protected static final TrackedData<Integer> VARIANT;
    protected static final TrackedData<Boolean> SLEEPING;
    private static final TrackedData<Integer> RING_COLOR;
    private static final TrackedData<Boolean> RINGED;

    public float timer;
    public int timeUntilNextFeather;
    protected boolean blink = false;
    private byte nextBlink = 0;
    private byte blinkTime = 0;
    private byte blinkSec = 0;
    private Random rando = new Random();
    private BirdEntity leader;
    private int groupSize = 1;

    //Entity constructor and stuff
    public BirdEntity(EntityType type, World worldIn, int dummy) {
        super(type, worldIn);
        this.setGender(getRandom().nextInt(2));
        this.setVariant(getRandom().nextInt(setBirdVariants()));
        this.timeUntilNextFeather = this.rando.nextInt(10000) + 10000;
        //this.moveControl = new MoveControlFlying(this, 30, false);
        //this.goalSelector.add(1, new BirdSwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 2.0D));
        this.goalSelector.add(2, new FleeEntityGoal(this, OcelotEntity.class, 15.0F, 1.5D, 2D));
        this.goalSelector.add(2, new FleeEntityGoal(this, CatEntity.class, 15.0F, 1.5D, 2D));
        //this.goalSelector.add(3, new EatFromFeedersGoal(this));
        this.goalSelector.add(4, new FleeEntityGoal(this, PlayerEntity.class, 15.0F, 1.0D, 1.2D));
        this.goalSelector.add(5, new FlyOntoTreeGoal(this, 1.0D));
        //this.goalSelector.add(5, new FollowLeaderGoal(this));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));

    }

    // Required by GeckoLib
    private AnimationFactory factory = new AnimationFactory(this);

    // TODO: need to pass something in here in place of predicate (https://geckolib.com/en/latest/3.0.0/entity_animations/)
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bat.fly", true));
        return PlayState.CONTINUE;
    }
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }
    @Override
    public AnimationFactory getFactory() { return this.factory; }

    static {
        GENDER = DataTracker.registerData(BirdEntity.class, TrackedDataHandlerRegistry.INTEGER);
        VARIANT = DataTracker.registerData(BirdEntity.class, TrackedDataHandlerRegistry.INTEGER);
        SLEEPING = DataTracker.registerData(BirdEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        RING_COLOR = DataTracker.registerData(BirdEntity.class, TrackedDataHandlerRegistry.INTEGER);
        RINGED = DataTracker.registerData(BirdEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.75F;
    }

    public void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(GENDER, Integer.valueOf(0));
        this.dataTracker.startTracking(VARIANT, Integer.valueOf(0));
        this.dataTracker.startTracking(SLEEPING, Boolean.valueOf(false));
        this.dataTracker.startTracking(RING_COLOR,Integer.valueOf(DyeColor.GRAY.getId()));
        this.dataTracker.startTracking(RINGED, Boolean.valueOf(false));
    }

    //NBT write and read methods
    public void writeCustomDataToTag(NbtCompound tagCompound) {
        super.writeCustomDataToNbt(tagCompound);
        tagCompound.putInt("Gender", this.getGender());
        tagCompound.putInt("Variant", this.getVariant());
        tagCompound.putBoolean("Sleeping", this.isSleeping());
        tagCompound.putInt("CollarColor", (byte)this.getRingColor().getId());
        tagCompound.putBoolean("Ringed", this.hasBeenRinged());
    }

    public void readCustomDataFromTag(NbtCompound tagCompound) {
        super.writeCustomDataToNbt(tagCompound);
        this.setGender(tagCompound.getInt("Gender"));
        this.setVariant(tagCompound.getInt("Variant"));
        this.setSleeping(tagCompound.getBoolean("Sleeping"));
        this.setRinged(tagCompound.getBoolean("Ringed"));
        if (tagCompound.contains("CollarColor", 99))
        {
            this.setRingColor(DyeColor.byId(tagCompound.getInt("CollarColor")));
        }
    }

    public static boolean canSpawnThere(EntityType<? extends HostileEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return canMobSpawn(type, world, spawnReason, pos, random);
    }

    //Flying code
    @Override
    protected EntityNavigation createNavigation(World world) {
        if (this.isBaby()) {
            return super.createNavigation(world);
        }
        BirdNavigation pathnavigateflying = new BirdNavigation(this, world);
        pathnavigateflying.setCanSwim(true);
        pathnavigateflying.setCanEnterOpenDoors(true);
        return pathnavigateflying;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) { }

    @Override
    public void move(MovementType type, Vec3d movement) {
        if(this.isInNet() && (type == MovementType.SELF || type == MovementType.PLAYER)){
            if(this.world.getBlockState(new BlockPos(this.getX(), this.getY(), this.getZ())).get(RingingNetBlock.DIRECTION) == RingingNetBlock.EnumBlockDirection.NORTH) {
                this.setPos(this.getPos().x, this.getPos().y, (this.getPos().z - this.getPos().z % 1) + 0.5);
            }
            else{
                this.setPos((this.getPos().x - this.getPos().x % 1) + 0.5, this.getPos().y, this.getPos().z);
            }
        }
        else{
            super.move(type, movement);
        }
    }

    //Entity stuff
    public int getLimitPerChunk() {
        return this.getMaxGroupSize();
    }

    public int getMaxGroupSize() {
        return super.getLimitPerChunk();
    }

    protected boolean hasSelfControl() {
        return !this.hasLeader();
    }

    public boolean hasLeader() {
        return this.leader != null && this.leader.isAlive();
    }

    public BirdEntity joinGroupOf(BirdEntity groupLeader) {
        this.leader = groupLeader;
        groupLeader.increaseGroupSize();
        return groupLeader;
    }

    public void leaveGroup() {
        this.leader.decreaseGroupSize();
        this.leader = null;
    }

    private void increaseGroupSize() {
        ++this.groupSize;
    }

    private void decreaseGroupSize() {
        --this.groupSize;
    }

    public boolean canHaveMoreBirdInGroup() {
        return this.hasOtherBirdInGroup() && this.groupSize < this.getMaxGroupSize();
    }

    public boolean hasOtherBirdInGroup() {
        return this.groupSize > 1;
    }

    public boolean isCloseEnoughToLeader() {
        return this.squaredDistanceTo(this.leader) <= 121.0D;
    }

    public void moveTowardLeader() {
        if (this.hasLeader()) {
            this.getNavigation().startMovingTo(this.leader, 1.0D);
        }

    }

    public void pullInOtherBird(Stream<BirdEntity> fish) {
        fish.limit((long)(this.getMaxGroupSize() - this.groupSize)).filter((schoolingBirdEntity) -> {
            return schoolingBirdEntity != this;
        }).forEach((schoolingBirdEntity) -> {
            schoolingBirdEntity.joinGroupOf(this);
        });
    }

    @Nullable
    public EntityData initialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
        // TODO: do we need to check if client world? Literally in a folder called "client"
        super.initialize((ServerWorldAccess)world, difficulty, spawnReason, entityData, entityTag);
        if (entityData == null) {
            entityData = new BirdEntity.BirdData(this);
        } else {
            this.joinGroupOf(((BirdEntity.BirdData)entityData).leader);
        }

        return entityData;
    }
    
    @Override
    public void tickMovement() {
        if(this.isInNet()){
            this.fallDistance = 0.0F;
            this.onGround = true;
        }

        if((!onGround && !isTouchingWater()) || (this.isAquatic() && isTouchingWater()) || (isSleeping())) {
            timer+=0.05F;
        }
        else{
            timer = 0.0F;
        }
        if(!this.isSleeping()) {
            if (this.random.nextInt(5) == 3) {
                this.blinkSec = ((byte) (this.rando.nextInt((byte) 50) + 30));
            }
            if (!this.blink) {
                this.nextBlink = ((byte) (this.nextBlink + 1));
            }
            if (this.nextBlink >= this.blinkSec) {
                this.blink = true;
                this.nextBlink = 0;
            }
            if (this.blink) {
                this.blinkTime = ((byte) (this.blinkTime + 1));
            }
            if (this.blinkTime >= 3) {
                this.blink = false;
                this.blinkTime = 0;
            }
        }
        if(this.hasBeenRinged()){
            this.setPersistent();
        }
        super.tickMovement();
    }

    public void tick() {
        super.tick();
        // TODO: Not sure what's going on here
        /*if (this.hasOtherBirdInGroup() && this.world.random.nextInt(200) == 1) {
            List<BirdEntity> list = this.world.getNonSpectatingEntities(this.getClass(), this.getBoundingBox().expand(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.groupSize = 1;
            }
        }*/

    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getStackInHand(hand);

        if (itemstack.getItem() == InitItems.RING)
        {
            if(this.hasBeenRinged() == false){
                this.setRinged(true);
                player.experienceLevel = player.experienceLevel + 1;
                if (!player.isCreative())
                {
                    itemstack.decrement(1);
                }
            }
            return ActionResult.SUCCESS;

        }
        else if (itemstack.getItem() instanceof ItemBirdSpawnEgg)
        {
            ItemBirdSpawnEgg egg = (ItemBirdSpawnEgg)itemstack.getItem();

            if(egg.entityType == this.getType()){
                if (!this.world.isClient())
                {
                    Object mobEntity3;
                    // TODO probably a bad idea making this change blind
                    //mobEntity3 = ((PassiveEntity)this).createChild((PassiveEntity)this);
                    mobEntity3 = this.createChild((ServerWorld)this.world, this);
                    ((MobEntity)mobEntity3).setBaby(true);
                    ((MobEntity)mobEntity3).refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                    world.spawnEntity((Entity)mobEntity3);
                    if (itemstack.hasCustomName()) {
                        ((MobEntity)mobEntity3).setCustomName(itemstack.getName());
                    }

                    if (!player.isCreative()) {
                        itemstack.decrement(1);
                    }
                }
            }
            return ActionResult.SUCCESS;

        }
        else if (itemstack.getItem() instanceof DyeItem && this.hasBeenRinged())
        {
            DyeItem dye = (DyeItem) itemstack.getItem();
            DyeColor enumdyecolor = dye.getColor();

            if (enumdyecolor != this.getRingColor())
            {
                this.setRingColor(enumdyecolor);

                if (!player.isCreative())
                {
                    itemstack.decrement(1);
                }

                return ActionResult.SUCCESS;
            }
        }
        return super.interactMob(player, hand);
    }

    //Sleeping code
    public boolean isSleeping() {
        return this.dataTracker.get(SLEEPING);
    }
    public void setSleeping(boolean value) {
        this.dataTracker.set(SLEEPING, Boolean.valueOf(value));
    }

    public boolean isInNet(){
        Block block = this.world.getBlockState(new BlockPos(this.getPos())).getBlock();
        if(block == InitBlocks.RINGING_NET) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected float getSoundVolume() {
        return 0.5F;
    }

    //Blinking code
    public boolean getBlinking()
    {
        return this.blink;
    }


    //NBT Tags getters and setters
    public int getGender() {
        return this.dataTracker.get(GENDER);
    }

    public void setGender(int value) {
        this.dataTracker.set(GENDER, Integer.valueOf(value));
    }

    public int getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setVariant(int value) {
        this.dataTracker.set(VARIANT, Integer.valueOf(value));
    }

    public DyeColor getRingColor()
    {
        return DyeColor.byId(this.dataTracker.get(RING_COLOR).intValue() & 15);
    }

    public void setRingColor(DyeColor collarcolor)
    {
        this.dataTracker.set(RING_COLOR, Integer.valueOf(collarcolor.getId()));
    }

    public boolean hasBeenRinged() {
        return this.dataTracker.get(RINGED);
    }

    public void setRinged(boolean value) {
        this.dataTracker.set(RINGED, value);
    }

    public static class BirdData extends PassiveData {
        public final BirdEntity leader;

        public BirdData(BirdEntity leader) {
            // TODO: Minecraft demanded this next line, but I don't know where the proper values live...
            super(false);
            this.leader = leader;
        }
    }

    public int setBirdVariants() {
        return 1;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if(this.isOnGround() && !isSleeping()){
            return SoundHandler.BULLFINCH_CALL;
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
                this.dropItem(InitItems.EURASIANBULLFINCHDFEATHER_MALE, 1);
            }
            else{
                this.dropItem(InitItems.EURASIANBULLFINCHDFEATHER_FEMALE, 1);
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

    public boolean goesToFeeders() {
        return true;
    }

    public boolean isAquatic() {
        return false;
    }

    public boolean isGroupBird() {
        return true;
    }

    @Override
    public @org.jetbrains.annotations.Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return (BirdEntity)this.getType().create(world);
    }

}

