package navy_master.hachimi.magic.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.blockentity.MusicAltarBlockEntity;
import navy_master.hachimi.magic.menu.MusicAltarMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.slf4j.Logger;

public class MusicMultiblockScreen extends AbstractContainerScreen<MusicAltarMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HachimiMagic.MODID, "textures/gui/music_multiblock.png");
    //TODO 界面美术资源占位
    private static final ResourceLocation CHEST_TEXTURE =
            new ResourceLocation("textures/gui/container/generic_54.png");
    private static final Logger LOGGER = LogUtils.getLogger();

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
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(CHEST_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        BlockPos pos = menu.getPos();

        if (minecraft.level != null) {
            BlockEntity be = minecraft.level.getBlockEntity(pos);
            if (be instanceof MusicAltarBlockEntity monitor) {
                // 实时获取流体量
                int[] amounts = monitor.monitorTanks();

                // 绘制流体条
                for (int i = 0; i < 4; i++) {
                    int fluidHeight = amounts[i] / 10000; // 假设最大容量10000
                    guiGraphics.blit(CHEST_TEXTURE,
                            leftPos + TANK_X[i],
                            topPos + TANK_Y[i] + (40 - fluidHeight),
                            176,
                            0,
                            8,
                            fluidHeight);
                }
            }
        }

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        BlockPos pos = menu.getPos();

        if (minecraft.level != null) {
            BlockEntity be = minecraft.level.getBlockEntity(pos);
            if (be instanceof MusicAltarBlockEntity monitor) {
                // 实时获取流体量
                int[] amounts = monitor.monitorTanks();
                // 绘制流体条提示
                for (int i = 0; i < 4; i++) {
                    if (isHovering(TANK_X[i], TANK_Y[i], 8, 40, mouseX, mouseY)) {
                        LOGGER.debug("amount:" + amounts[i]);
                        guiGraphics.renderTooltip(this.font,
                                Component.literal("Fluid: " + amounts[i] + "/10000"),
                                mouseX, mouseY);
                    }
                }
            }
        }
    }
}