package navy_master.hachimi.magic.screen;


import com.mojang.blaze3d.systems.RenderSystem;
import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.blockentity.BaseTankBlockEntity;
import navy_master.hachimi.magic.menu.BaseTankMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.FluidStack;

public class MusicTankScreen extends BaseTankScreen {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(HachimiMagic.MODID, "textures/gui/mixing_tank_gui.png");
    public MusicTankScreen(BaseTankMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
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

        // 绘制流体槽
        renderFluidTank(guiGraphics, x-imageWidth/2, y-imageHeight/2);
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
            BaseTankBlockEntity blockEntity = menu.getBlockEntity();
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