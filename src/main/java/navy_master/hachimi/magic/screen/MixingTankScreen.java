package navy_master.hachimi.magic.screen;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.blockentity.MixingTankBlockEntity;
import navy_master.hachimi.magic.menu.MixingTankMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.slf4j.Logger;

public class MixingTankScreen extends AbstractContainerScreen<MixingTankMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HachimiMagic.MODID, "textures/gui/mixing_tank_gui.png");
    //TODO 界面美术资源占位
    private static final ResourceLocation CHEST_TEXTURE =
            new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final Logger LOGGER = LogUtils.getLogger();

    public MixingTankScreen(MixingTankMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    private MixingTankBlockEntity getBlockEntity() {
        return (MixingTankBlockEntity) menu.getBlockEntity();
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CHEST_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        // 绘制背景
        guiGraphics.blit(CHEST_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // 绘制进度条
        renderProgressArrow(guiGraphics, x, y);

        // 绘制流体槽
        renderFluidTank(guiGraphics, x-imageWidth/2, y-imageHeight/2);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        // 通过菜单获取方块实体
        MixingTankBlockEntity blockEntity = menu.getBlockEntity();
        if (blockEntity != null && blockEntity.getProgress() > 0) {
            int progress = blockEntity.getProgress();
            int maxProgress = blockEntity.getMaxProgress();
            int progressWidth = (int) (24 * ((float) progress / maxProgress));
            guiGraphics.blit(CHEST_TEXTURE, x + 79, y + 34, 176, 0, progressWidth, 17);
        }
    }

    private void renderFluidTank(GuiGraphics guiGraphics, int x, int y) {
        // 获取同步的流体数据
        FluidStack fluidStack=getBlockEntity().getFluidTank().getFluid();
        IClientFluidTypeExtensions renderProps = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        int fluidcolor = renderProps.getTintColor(fluidStack);
        int fluidAmount = fluidStack.getAmount();

        //LOGGER.debug("fluid color:"+fluidcolor+",fluid amount:"+fluidAmount);
        // 计算渲染高度（按比例）
        int fluidHeight = (int) (52 * ((float) fluidAmount / getBlockEntity().getFluidTank().getCapacity()));

        // 绘制流体背景框
        guiGraphics.fill(
                leftPos + x, topPos + y,
                leftPos + x + 16,
                topPos + y + 52,
                0xFF555555 // 灰色背景
        );

        // 绘制流体填充条
        if (fluidAmount > 0) {
            guiGraphics.fill(
                    leftPos + x,
                    topPos + y + (52 - fluidHeight),
                    leftPos + x + 16,
                    topPos + y + 52,
                    fluidcolor );
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);

        // 在流体槽上显示工具提示
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int fluidX = x + 80;
        int fluidY = y + 16;
        int fluidWidth = 16;
        int fluidHeight = 52;

        if (isHovering(fluidX, fluidY, fluidWidth, fluidHeight, mouseX, mouseY)) {
            MixingTankBlockEntity blockEntity = menu.getBlockEntity();
            if (blockEntity != null) {
                FluidStack fluidStack = blockEntity.getFluidTank().getFluid();
                if (!fluidStack.isEmpty()) {
                    Component text = Component.literal(fluidStack.getAmount() + "mB " +
                            fluidStack.getDisplayName().getString());
                    guiGraphics.renderTooltip(font, text, mouseX, mouseY);
                }
            }
        }
    }
}