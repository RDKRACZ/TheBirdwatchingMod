package com.ikerleon.birdwmod.client.gui;

import com.ikerleon.birdwmod.Main;
import com.ikerleon.birdwmod.entity.BirdEntity;
import com.ikerleon.birdwmod.entity.BirdSettings;
import com.ikerleon.birdwmod.entity.InitEntities;
import com.ikerleon.birdwmod.items.InitItems;
import com.mojang.blaze3d.systems.RenderSystem;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.fabricmc.fabric.impl.object.builder.FabricEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class GUIBirdGuide extends Screen {

    private final int bookImageHeight = 180;
    private final int bookImageWidth = 292;

    private int currPage = 0;
    private static final int bookTotalPages = 17;

    private ButtonWidget buttonDone;
    private ButtonWidget buttonNextPage;
    private ButtonWidget buttonPreviousPage;
    private static Identifier cover=new Identifier("birdwmod" + ":textures/gui/birdguide/cover.png");
    private static Identifier page=new Identifier("birdwmod" + ":textures/gui/birdguide/page.png");

    private static String Waterfowl = Formatting.GRAY + "Waterfowl";
    private static String Galliformes = Formatting.GRAY + "Galliformes";
    private static String Waders = Formatting.GRAY + "Waders";
    private static String GullsBoobies = Formatting.GRAY + "Gulls & Boobies";
    private static String Auks = Formatting.GRAY + "Auks";
    private static String Owls = Formatting.GRAY + "Owls";
    private static String Nightjars = Formatting.GRAY + "Nightjars";
    private static String Passerines = Formatting.GRAY + "Passerines";
    private static String Herons = Formatting.GRAY + "Herons";
    private static String Coraciiformes = Formatting.GRAY + "Coraciiformes";
    private static String Opisthocomiformes = Formatting.GRAY + "Opisthocomiformes";

    private static String CharacteristicsTitle = Formatting.BOLD + "Characteristics";
    private static String BiomesTitle = Formatting.BOLD + "Vanilla Biomes";

    private static String page9Title = Formatting.BOLD + "Red-necked nightjar";
    private static String page9Subtitle = Formatting.ITALIC + "(Caprimulgus ruficollis)";
    private static String page9Text = "It's the largest of the nightjars occurring in Europe. It breeds in Iberia and north Africa, and winters in tropical west Africa. Open sandy heaths with trees or bushes are the haunts of this crepuscular bird. In flight it presents a characteristic silhouette with silent flight and low altitude.";

    private static String page10Title = Formatting.BOLD + "Northern Mockingbird";
    private static String page10Subtitle = Formatting.ITALIC + "(Mimus polyglottos)";
    private static String page10Text = "It's are best known for the habit of mimicking the songs of other birds and the sounds of insects and amphibians. This bird is mainly a permanent resident, but northern birds may move south during harsh weather. Northern mockingbirds are omnivore. It's often found in open areas and forest edges.";

    private static String page11Title = Formatting.BOLD + "Eastern bluebird";
    private static String page11Subtitle = Formatting.ITALIC + "(Sialia sialis)";
    private static String page11Text = "It's a small thrush found in open woodlands, farmlands, and orchards of North America. The Eastern bluebird is the state bird of New York. About two-thirds of the diet of an adult consists of insects and other invertebrates. Eastern bluebirds tend to live in open country around trees.";

    private static String page12Title = Formatting.BOLD + "Red-flanked bluetail";
    private static String page12Subtitle = Formatting.ITALIC + "(Tarsiger cyanurus)";
    private static String page12Text = "It's a small passerine bird that lives in the coniferous forests of Eurasia. It breeds in upper-middle and marginally in upper continental latitudes, exclusively boreal and mountain. Its diet is based on insects, also fruits and seeds outside breeding season.";

    private static String page14Title1 = Formatting.BOLD + "King-of-Saxony";
    private static String page14Title2 = Formatting.BOLD + "bird of paradise";
    private static String page14Subtitle = Formatting.ITALIC + "(Pteridophora alberti)";
    private static String page14Text = "It's a bird of paradise endemic to montane forest in New Guinea. The most iconic characteristic of this bird are the two remarkably long (up to 50 cm) brow-plumes, which are so bizarre that when the first specimen was brought to Europe, it was thought to be a fake.";

    private static String page15Title1 = Formatting.BOLD + "Turquoise-browed";
    private static String page15Title2 = Formatting.BOLD + "motmot";
    private static String page15Subtitle = Formatting.ITALIC + "(Eumomota superciliosa)";
    private static String page15Text = "It's a colorful bird that lives all across Central America, from south-east Mexico (mostly the Yucatán Peninsula), to Costa Rica. It lives in habitats such as forest edge or gallery forest. it often perches from wires or posts where it scans for prey, such as insects and small reptiles.";

    private static String page16Title = Formatting.BOLD + "Hoatzin";
    private static String page16Subtitle = Formatting.ITALIC + "(Opisthocomus hoazin)";
    private static String page16Text = "It's a tropical, dinosaur-type bird that can be found in swamps, riparian forests, and mangroves of the Amazon and the Orinoco basins in South America. It is notable for having chicks that have claws on two of their wing digits. The hoatzin is a folivore, in other words it eats the leaves";


    public GUIBirdGuide() {
        super(NarratorManager.EMPTY);
    }

    @Override
    protected void init() {
        int offLeft = (int)((this.width - 292) / 2.0F);
        int offTop = (int)((this.height - 225) / 2.0F);

        this.client.keyboard.setRepeatEvents(true);

        buttonDone = new ButtonWidget(offLeft+(bookImageWidth/2)-50, offTop+bookImageHeight+15, 100, 20, ScreenTexts.DONE, (buttonWidget) -> {
            this.client.setScreen(null);
        });

        this.addDrawableChild(buttonDone);
        this.addDrawableChild(buttonNextPage = new ButtonWidget(offLeft+bookImageWidth+15, offTop, 50, 20, new LiteralText("->"), (buttonWidget) -> {
            if (currPage < bookTotalPages - 1)
            {
                ++currPage;
                buttonNextPage.visible = (currPage < bookTotalPages - 1);
                buttonPreviousPage.visible = currPage > 0;
            }
        }));
        this.addDrawableChild(buttonPreviousPage = new ButtonWidget( offLeft-65, offTop, 50, 20, new LiteralText("<-"), (buttonWidget) -> {
            if (currPage > 0)
            {
                --currPage;
                buttonPreviousPage.visible = currPage > 0;
                buttonNextPage.visible = (currPage < bookTotalPages - 1);
            }
        }));
        buttonPreviousPage.visible = false;
    }

    public static MutableText getTranslatedText(@Nullable Formatting format, BirdEntity bird, String section){
        MutableText text = new TranslatableText("gui."+Main.ModID+"."+bird.getPath()+"_" + section);
        if(format!=null){return text.formatted(format);}
        return text;
    }

    public static MutableText getTranslatedText(BirdEntity bird, String section){
        return getTranslatedText(null, bird, section);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int offLeft = (int)((this.width - 292) / 2.0F);
        int offTop = (int)((this.height - 225) / 2.0F);
        int mousePosX = mouseX;
        int mousePosY = mouseY;

        if(currPage==0) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, cover);
            drawTexture(matrices, offLeft, offTop, 0, 0, bookImageWidth, bookImageHeight, bookImageWidth, bookImageHeight);
            super.render(matrices, mouseX, mouseY, delta);
            return;
        }
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, page);
        drawTexture(matrices, offLeft, offTop, 0, 0, bookImageWidth ,bookImageHeight ,bookImageWidth ,bookImageHeight);

        BirdEntity.Settings birdSettings = BirdSettings.bookBirds.get(currPage - 1);
        BirdEntity entity = new BirdEntity(InitEntities.GUI_BIRD_ENTITY, MinecraftClient.getInstance().world, birdSettings);
        this.textRenderer.draw( matrices, getTranslatedText(Formatting.BOLD, entity, "title"), offLeft + 30, 15 + offTop, 0);
        this.textRenderer.draw( matrices, getTranslatedText(Formatting.ITALIC, entity, "subtitle"), offLeft + 25, 25 + offTop, 0);
        this.textRenderer.drawTrimmed(StringVisitable.plain(getTranslatedText(entity, "text").getString()), offLeft + 13, 40 + offTop, 126, 0);
        this.textRenderer.draw( matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
        this.textRenderer.draw( matrices,Waterfowl, offLeft + 195, 25 + offTop, 0);
        this.textRenderer.draw( matrices,Formatting.ITALIC + "Male", offLeft + 175, 80 + offTop, 0);
        this.textRenderer.draw( matrices,Formatting.ITALIC + "Female", offLeft + 232, 80 + offTop, 0);
        this.textRenderer.draw( matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
        this.textRenderer.drawTrimmed(StringVisitable.plain(birdSettings.spawnBiomesAsString()), offLeft + 160, 140 + offTop,110,  0);

        BirdEntity female_entity = new BirdEntity(InitEntities.GUI_BIRD_ENTITY, MinecraftClient.getInstance().world, birdSettings);
        entity.setGender(0);
        entity.setVariant(0);
        entity.setOnGround(true);
        female_entity.setGender(1);
        female_entity.setVariant(0);
        female_entity.setOnGround(true);
        this.itemRenderer.renderGuiItemIcon(new ItemStack(entity.getFeatherItem(), 1), offLeft + 175, 95 + offTop);
        this.itemRenderer.renderInGui(new ItemStack(female_entity.getFeatherItem(), 1), offLeft + 240, 95 + offTop);
        //this.itemRenderer.renderGuiItemIcon(new ItemStack(InitItems.STELLERSEIDERFEATHER_MALE, 1), offLeft + 175, 95 + offTop);
        //this.itemRenderer.renderInGui(new ItemStack(InitItems.STELLERSEIDERFEATHER_FEMALE, 1), offLeft + 240, 95 + offTop);
        int i = (this.width - this.bookImageWidth) / 2;
        int j = (this.height - this.bookImageHeight) / 2;
        InventoryScreen.drawEntity(offLeft + 185, 75 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
        InventoryScreen.drawEntity(offLeft + 250, 75 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);

        /*if(currPage==2){
            this.textRenderer.draw( matrices, page2Title, offLeft + 30, 15 + offTop, 0);
            this.textRenderer.draw( matrices, page2Subtitle, offLeft + 11, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page2Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw( matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw( matrices, Galliformes, offLeft + 190, 25 + offTop, 0);
            this.textRenderer.draw( matrices,Formatting.ITALIC + "Male", offLeft + 175, 80 + offTop, 0);
            this.textRenderer.draw( matrices,Formatting.ITALIC + "Female", offLeft + 232, 80 + offTop, 0);
            this.textRenderer.draw( matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain("- Wooded Mountains"), offLeft + 160, 140 + offTop,110,  0);

            this.itemRenderer.renderGuiItemIcon(new ItemStack(InitItems.HIMALAYANMONALMALEFEATHER, 1), offLeft + 175, 95 + offTop);
            this.itemRenderer.renderInGui(new ItemStack(InitItems.HIMALAYANMONALFEMALEFEATHER, 1), offLeft + 240, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.HIMALAYAN_MONAL_ENTITY, MinecraftClient.getInstance().world, BirdSettings.HIMALAYAN_MONAL_SETTINGS);
            entity.setGender(0);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.HIMALAYAN_MONAL_ENTITY, MinecraftClient.getInstance().world, BirdSettings.HIMALAYAN_MONAL_SETTINGS);
            female_entity.setGender(1);
            female_entity.setOnGround(true);
            InventoryScreen.drawEntity(offLeft + 185, 75 + offTop, 50, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 250, 75 + offTop, 50, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
        }

        else if(currPage==3){
            this.textRenderer.draw( matrices, page3Title, offLeft + 35, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page3Subtitle, offLeft + 20, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page3Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Herons, offLeft + 200, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 1", offLeft + 160, 75 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 2", offLeft + 200, 85 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 3", offLeft + 240, 75 + offTop,0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- River", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.GREENHERONFEATHER, 1), offLeft + 205, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.GREEN_HERON_ENTITY, MinecraftClient.getInstance().world, BirdSettings.GREEN_HERON_SETTINGS);
            entity.setVariant(1);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.GREEN_HERON_ENTITY, MinecraftClient.getInstance().world, BirdSettings.GREEN_HERON_SETTINGS);
            female_entity.setVariant(2);
            female_entity.setOnGround(true);
            BirdEntity entity3 = new BirdEntity(InitEntities.GREEN_HERON_ENTITY, MinecraftClient.getInstance().world, BirdSettings.GREEN_HERON_SETTINGS);
            entity3.setVariant(3);
            entity3.setOnGround(true);
            InventoryScreen.drawEntity(offLeft + 175, 70 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity3);
            InventoryScreen.drawEntity(offLeft + 215, 80 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
            InventoryScreen.drawEntity(offLeft + 255, 70 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
        }

        else if(currPage==4) {
            this.textRenderer.draw(matrices, page4Title, offLeft + 50, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page4Subtitle, offLeft + 15, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page4Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Waders, offLeft + 200, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 1", offLeft + 160, 65 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 2", offLeft + 200, 85 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 3", offLeft + 240, 65 + offTop,0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Plains", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.KILLDEERFEATHER, 1), offLeft + 205, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.KILLDEER_ENTITY, MinecraftClient.getInstance().world,BirdSettings.KILLDEER_SETTINGS);
            entity.setVariant(1);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.KILLDEER_ENTITY, MinecraftClient.getInstance().world,BirdSettings.KILLDEER_SETTINGS);
            female_entity.setVariant(2);
            female_entity.setOnGround(true);
            BirdEntity entity3 = new BirdEntity(InitEntities.KILLDEER_ENTITY, MinecraftClient.getInstance().world,BirdSettings.KILLDEER_SETTINGS);
            entity3.setVariant(3);
            entity3.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 175, 60 + offTop, 80, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 215, 80 + offTop, 80, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
            InventoryScreen.drawEntity(offLeft + 255, 60 + offTop, 80, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity3);
        }
        else if(currPage==5){
            this.textRenderer.draw(matrices, page5Title, offLeft + 40, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page5Subtitle, offLeft + 40, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page5Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, GullsBoobies, offLeft + 180, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 1", offLeft + 160, 65 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 2", offLeft + 200, 85 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 3", offLeft + 240, 65 + offTop,0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Beach and Ocean", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.SABINESGULLFEATHER, 1), offLeft + 205, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.SABINES_GULL_ENTITY, MinecraftClient.getInstance().world, BirdSettings.SABINES_GULL_SETTINGS);
            entity.setVariant(1);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.SABINES_GULL_ENTITY, MinecraftClient.getInstance().world, BirdSettings.SABINES_GULL_SETTINGS);
            female_entity.setVariant(2);
            female_entity.setOnGround(true);
            BirdEntity entity3 = new BirdEntity(InitEntities.SABINES_GULL_ENTITY, MinecraftClient.getInstance().world, BirdSettings.SABINES_GULL_SETTINGS);
            entity3.setVariant(3);
            entity3.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 175, 65 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 215, 85 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
            InventoryScreen.drawEntity(offLeft + 255, 65 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity3);
        }
        else if(currPage==6){
            this.textRenderer.draw(matrices, page6Title, offLeft + 35, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page6Subtitle, offLeft + 25, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page6Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, GullsBoobies, offLeft + 180, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 1", offLeft + 200, 60 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 2", offLeft + 160, 85 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 3", offLeft + 200, 95 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 4", offLeft + 240, 85 + offTop,0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Beach and Ocean", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.BROWNBOOBYFEATHER, 1), offLeft + 205, 102 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.BROWN_BOOBY_ENTITY, MinecraftClient.getInstance().world, BirdSettings.BROWN_BOOBY_SETTINGS);
            entity.setVariant(1);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.BROWN_BOOBY_ENTITY, MinecraftClient.getInstance().world,BirdSettings.BROWN_BOOBY_SETTINGS);
            female_entity.setVariant(2);
            female_entity.setOnGround(true);
            BirdEntity entity3 = new BirdEntity(InitEntities.BROWN_BOOBY_ENTITY, MinecraftClient.getInstance().world,BirdSettings.BROWN_BOOBY_SETTINGS);
            entity3.setVariant(3);
            entity3.setOnGround(true);
            BirdEntity entity4 = new BirdEntity(InitEntities.BROWN_BOOBY_ENTITY, MinecraftClient.getInstance().world,BirdSettings.BROWN_BOOBY_SETTINGS);
            entity4.setVariant(4);
            entity4.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 175, 85 + offTop, 35, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity3);
            InventoryScreen.drawEntity(offLeft + 215, 95 + offTop, 35, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
            InventoryScreen.drawEntity(offLeft + 255, 85 + offTop, 35, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 215, 60 + offTop, 35, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity4);
        }
        else if(currPage==7){
            this.textRenderer.draw(matrices, page7Title, offLeft + 45, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page7Subtitle, offLeft + 40, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page7Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Auks, offLeft + 208, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Summer", offLeft + 170, 80 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Winter", offLeft + 232, 80 + offTop, 0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Beach and Ocean", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.RAZORBILLFEATHER, 1), offLeft + 207, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.RAZORBILL_ENTITY, MinecraftClient.getInstance().world,BirdSettings.RAZORBILL_SETTINGS);
            entity.setGender(0);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.RAZORBILL_ENTITY, MinecraftClient.getInstance().world,BirdSettings.RAZORBILL_SETTINGS);
            female_entity.setGender(1);
            //female_entity.biome = Biomes.SNOWY_BEACH;
            female_entity.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 190, 75 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 250, 75 + offTop, 60, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
        }
        else if(currPage==8){
            this.textRenderer.draw(matrices, page8Title, offLeft + 30, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page8Subtitle, offLeft + 34, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page8Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Owls, offLeft + 208, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 1", offLeft + 160, 75 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 2", offLeft + 200, 85 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 3", offLeft + 240, 75 + offTop,0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Taiga", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.GREATGREYOWLFEATHER, 1), offLeft + 207, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.GREAT_GREY_OWL_ENTITY, MinecraftClient.getInstance().world, BirdSettings.GREAT_GREY_OWL_SETTINGS);
            entity.setVariant(1);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.GREAT_GREY_OWL_ENTITY, MinecraftClient.getInstance().world,BirdSettings.GREAT_GREY_OWL_SETTINGS);
            female_entity.setVariant(2);
            female_entity.setOnGround(true);
            BirdEntity entity3 = new BirdEntity(InitEntities.GREAT_GREY_OWL_ENTITY, MinecraftClient.getInstance().world,BirdSettings.GREAT_GREY_OWL_SETTINGS);
            entity3.setVariant(3);
            entity3.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 175, 70 + offTop, 55, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity3);
            InventoryScreen.drawEntity(offLeft + 215, 80 + offTop, 55, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
            InventoryScreen.drawEntity(offLeft + 255, 70 + offTop, 55, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
        }
        else if(currPage==9){
            this.textRenderer.draw(matrices, page9Title, offLeft + 15, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page9Subtitle, offLeft + 15, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page9Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Nightjars, offLeft + 194, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 1", offLeft + 160, 65 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 2", offLeft + 200, 85 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 3", offLeft + 240, 65 + offTop,0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Savanna, Mesa", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.REDNECKEDNIGHTJARFEATHER, 1), offLeft + 205, 95 + offTop);

            int i = (this.width - this.bookImageWidth)/2;
            int j = (this.height - this.bookImageHeight)/2;
            BirdEntity entity = new BirdEntity(InitEntities.RED_NECKED_NIGHTJAR_ENTITY, MinecraftClient.getInstance().world,BirdSettings.RED_NECKED_NIGHTJAR_SETTINGS);
            entity.setVariant(1);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.RED_NECKED_NIGHTJAR_ENTITY, MinecraftClient.getInstance().world,BirdSettings.RED_NECKED_NIGHTJAR_SETTINGS);
            female_entity.setVariant(2);
            female_entity.setOnGround(true);
            BirdEntity entity3 = new BirdEntity(InitEntities.RED_NECKED_NIGHTJAR_ENTITY, MinecraftClient.getInstance().world,BirdSettings.RED_NECKED_NIGHTJAR_SETTINGS);
            entity3.setVariant(3);
            entity3.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 175, 60 + offTop, 70, (float)(i + 51) - (mousePosX * 2), (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 215, 80 + offTop, 70, (float)(i + 51) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
            InventoryScreen.drawEntity(offLeft + 255, 60 + offTop, 70, (float)(i + 51) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity3);
        }
        else if(currPage==10){
            this.textRenderer.draw(matrices, page10Title, offLeft + 13, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page10Subtitle, offLeft + 30, 25 + offTop, 0);
            this.textRenderer.drawTrimmed( StringVisitable.plain(page10Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Passerines, offLeft + 192, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 1", offLeft + 160, 65 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 2", offLeft + 200, 85 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 3", offLeft + 240, 65 + offTop,0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Plains, Forest", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.NORTHERNMOCKINGBIRDFEATHER, 1), offLeft + 205, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.NORTHERN_MOCKINGBIRD_ENTITY, MinecraftClient.getInstance().world, BirdSettings.NORTHERN_MOCKINGBIRD_SETTINGS);
            entity.setVariant(1);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.NORTHERN_MOCKINGBIRD_ENTITY, MinecraftClient.getInstance().world,BirdSettings.NORTHERN_MOCKINGBIRD_SETTINGS);
            female_entity.setVariant(2);
            female_entity.setOnGround(true);
            BirdEntity entity3 = new BirdEntity(InitEntities.NORTHERN_MOCKINGBIRD_ENTITY, MinecraftClient.getInstance().world,BirdSettings.NORTHERN_MOCKINGBIRD_SETTINGS);
            entity3.setVariant(3);
            entity3.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 175, 60 + offTop, 90, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 215, 80 + offTop, 90, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
            InventoryScreen.drawEntity(offLeft + 255, 60 + offTop, 90, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity3);
        }
        else if(currPage==11){
            this.textRenderer.draw(matrices, page11Title, offLeft + 25, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page11Subtitle, offLeft + 40, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page11Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Passerines, offLeft + 192, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Male", offLeft + 175, 80 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Female", offLeft + 232, 80 + offTop, 0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Plains, oak forest", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.EASTERNBLUEBIRDFEATHER_MALE, 1), offLeft + 175, 95 + offTop);
            this.itemRenderer.renderInGui(new ItemStack(InitItems.EASTERNBLUEBIRDFEATHER_FEMALE, 1), offLeft + 240, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.EASTERN_BLUEBIRD_ENTITY, MinecraftClient.getInstance().world, BirdSettings.EASTERN_BLUEBIRD_SETTINGS);
            entity.setGender(0);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.EASTERN_BLUEBIRD_ENTITY, MinecraftClient.getInstance().world, BirdSettings.EASTERN_BLUEBIRD_SETTINGS);
            female_entity.setGender(1);
            female_entity.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 185, 75 + offTop, 100, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 250, 75 + offTop, 100, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
        }
        else if(currPage==12){
            this.textRenderer.draw(matrices, page12Title, offLeft + 15, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page12Subtitle, offLeft + 25, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page12Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Passerines, offLeft + 192, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Male", offLeft + 175, 80 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Female", offLeft + 232, 80 + offTop, 0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Taiga", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.REDFLANCKEDBLUETAILFEATHER_MALE, 1), offLeft + 175, 95 + offTop);
            this.itemRenderer.renderInGui(new ItemStack(InitItems.REDFLANCKEDBLUETAILFEATHER_FEMALE, 1), offLeft + 240, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.RED_FLANKED_BLUETAIL_ENTITY, MinecraftClient.getInstance().world,BirdSettings.RED_FLANKED_BLUETAIL_SETTINGS);
            entity.setOnGround(true);
            entity.setGender(0);
            BirdEntity female_entity = new BirdEntity(InitEntities.RED_FLANKED_BLUETAIL_ENTITY, MinecraftClient.getInstance().world,BirdSettings.RED_FLANKED_BLUETAIL_SETTINGS);
            female_entity.setOnGround(true);
            female_entity.setGender(1);
            
            InventoryScreen.drawEntity(offLeft + 185, 75 + offTop, 100, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 250, 75 + offTop, 100, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
        }
        else if(currPage==13){
            this.textRenderer.draw(matrices, page13Title, offLeft + 18, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page13Subtitle, offLeft + 20, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page13Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Passerines, offLeft + 192, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Male", offLeft + 175, 80 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Female", offLeft + 232, 80 + offTop, 0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Taiga, all forests", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.EURASIANBULLFINCHDFEATHER_MALE, 1), offLeft + 175, 95 + offTop);
            this.itemRenderer.renderInGui(new ItemStack(InitItems.EURASIANBULLFINCHDFEATHER_FEMALE, 1), offLeft + 240, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.EURASIAN_BULLFINCH_ENTITY, MinecraftClient.getInstance().world,BirdSettings.EURASIAN_BULLFINCH_SETTINGS);
            entity.setOnGround(true);
            entity.setGender(0);
            BirdEntity female_entity = new BirdEntity(InitEntities.EURASIAN_BULLFINCH_ENTITY, MinecraftClient.getInstance().world,BirdSettings.EURASIAN_BULLFINCH_SETTINGS);
            female_entity.setOnGround(true);
            female_entity.setGender(1);
            
            InventoryScreen.drawEntity(offLeft + 185, 75 + offTop, 100, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 250, 75 + offTop, 100, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
        }
        else if(currPage==14){
            this.textRenderer.draw(matrices, page14Title1, offLeft + 28, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page14Title2, offLeft + 25, 25 + offTop, 0);
            this.textRenderer.draw(matrices, page14Subtitle, offLeft + 18, 35 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page14Text), offLeft + 13, 50 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Passerines, offLeft + 192, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Male", offLeft + 175, 80 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Female", offLeft + 232, 80 + offTop, 0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Jungle", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.KINGOFSAXONYFEATHER_MALE, 1), offLeft + 175, 95 + offTop);
            this.itemRenderer.renderInGui(new ItemStack(InitItems.KINGOFSAXONYFEATHER_FEMALE, 1), offLeft + 240, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.KING_OF_SAXONY_ENTITY, MinecraftClient.getInstance().world,BirdSettings.KING_OF_SAXONY_SETTINGS);
            entity.setGender(0);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.KING_OF_SAXONY_ENTITY, MinecraftClient.getInstance().world,BirdSettings.KING_OF_SAXONY_SETTINGS);
            female_entity.setGender(1);
            female_entity.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 185, 75 + offTop, 80, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 250, 75 + offTop, 80, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
        }
        else if(currPage==15){
            this.textRenderer.draw(matrices, page15Title1, offLeft + 19, 10 + offTop, 0);
            this.textRenderer.draw(matrices, page15Title2, offLeft + 55, 20 + offTop, 0);
            this.textRenderer.draw(matrices, page15Subtitle, offLeft + 15, 30 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page15Text), offLeft + 13, 45 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Coraciiformes, offLeft + 182, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 1", offLeft + 160, 65 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 2", offLeft + 200, 85 + offTop,0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "var. 3", offLeft + 240, 65 + offTop,0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Jungle", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.MOTMOTFEATHER, 1), offLeft + 205, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.MOTMOT_ENTITY, MinecraftClient.getInstance().world,BirdSettings.MOTMOT_SETTINGS);
            entity.setVariant(1);
            entity.setOnGround(true);
            BirdEntity female_entity = new BirdEntity(InitEntities.MOTMOT_ENTITY, MinecraftClient.getInstance().world,BirdSettings.MOTMOT_SETTINGS);
            female_entity.setVariant(2);
            female_entity.setOnGround(true);
            BirdEntity entity3 = new BirdEntity(InitEntities.MOTMOT_ENTITY, MinecraftClient.getInstance().world,BirdSettings.MOTMOT_SETTINGS);
            entity3.setVariant(3);
            entity3.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 175, 60 + offTop, 75, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
            InventoryScreen.drawEntity(offLeft + 215, 80 + offTop, 75, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, female_entity);
            InventoryScreen.drawEntity(offLeft + 255, 60 + offTop, 75, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity3);
        }
        else if(currPage==16){
            this.textRenderer.draw(matrices, page16Title, 51 + offLeft, 15 + offTop, 0);
            this.textRenderer.draw(matrices, page16Subtitle, offLeft + 18, 25 + offTop, 0);
            this.textRenderer.drawTrimmed(StringVisitable.plain(page16Text), offLeft + 13, 40 + offTop, 126, 0);
            this.textRenderer.draw(matrices, CharacteristicsTitle, offLeft + 170, 15 + offTop, 0);
            this.textRenderer.draw(matrices, Opisthocomiformes, offLeft + 171, 25 + offTop, 0);
            this.textRenderer.draw(matrices, Formatting.ITALIC + "Single variant", offLeft + 181, 80 + offTop, 0);
            this.textRenderer.draw(matrices, BiomesTitle, offLeft + 175, 125 + offTop, 0);
            this.textRenderer.draw(matrices, "- Jungle, Swamp", offLeft + 160, 140 + offTop, 0);

            this.itemRenderer.renderInGui(new ItemStack(InitItems.HOATZINFEATHER, 1), offLeft + 207, 95 + offTop);

            int i = (this.width - this.bookImageWidth) / 2;
            int j = (this.height - this.bookImageHeight) / 2;
            BirdEntity entity = new BirdEntity(InitEntities.HOATZIN_ENTITY, MinecraftClient.getInstance().world, BirdSettings.HOATZIN_SETTINGS);
            entity.setOnGround(true);
            
            InventoryScreen.drawEntity(offLeft + 217, 75 + offTop, 65, (float)(i) - mousePosX, (float)(j + 75 - 50) - mousePosY, entity);
        }*/

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
