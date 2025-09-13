package navy_master.hachimi.magic.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.menu.MusicAltarMenu;
import navy_master.hachimi.magic.music_altar.ToggleTankPacket;
import navy_master.hachimi.magic.registry.PacketHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MusicMultiblockScreen extends AbstractContainerScreen<MusicAltarMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HachimiMagic.MODID, "textures/gui/music_multiblock.png");
    private static final ResourceLocation SLOT_BG =
            new ResourceLocation("textures/gui/container/slot.png");

    // 流体条位置 (北、东、南、西)
    private static final int[] TANK_X = {56, 108, 56, 4};
    private static final int[] TANK_Y = {4, 30, 56, 30};

    public MusicMultiblockScreen(MusicAltarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        // 添加四个方向的激活按钮
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            addRenderableWidget(new Button.Builder(Component.empty(), button -> {
                PacketHandler.INSTANCE.sendToServer(new ToggleTankPacket(finalI));
            })
                    .bounds(leftPos + TANK_X[i] + 4, topPos + TANK_Y[i] + 20, 16, 16)
                    .tooltip(Tooltip.create(Component.translatable("tooltip.multiblock.tank_toggle")))
                    .build());
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // 绘制流体条
        for (int i = 0; i < 4; i++) {
            int fluidHeight = menu.getTankLevel(i) * 40 / 10000; // 假设最大容量10000
            guiGraphics.blit(TEXTURE,
                    leftPos + TANK_X[i],
                    topPos + TANK_Y[i] + (40 - fluidHeight),
                    176,
                    0,
                    8,
                    fluidHeight);
        }

        // 绘制激活状态
        for (int i = 0; i < 4; i++) {
            if (menu.isTankActive(i)) {
                guiGraphics.blit(TEXTURE,
                        leftPos + TANK_X[i] + 4,
                        topPos + TANK_Y[i] + 20,
                        184,
                        0,
                        16,
                        16);
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        // 绘制流体条提示
        for (int i = 0; i < 4; i++) {
            if (isHovering(TANK_X[i], TANK_Y[i], 8, 40, mouseX, mouseY)) {
                guiGraphics.renderTooltip(this.font,
                        Component.literal("Fluid: " + menu.getTankLevel(i) + "/10000"),
                        mouseX, mouseY);
            }
        }
    }
}