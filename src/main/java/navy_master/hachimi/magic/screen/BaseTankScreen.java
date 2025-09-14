package navy_master.hachimi.magic.screen;

import navy_master.hachimi.magic.blockentity.BaseTankBlockEntity;
import navy_master.hachimi.magic.menu.BaseTankMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public abstract class BaseTankScreen extends AbstractContainerScreen<BaseTankMenu> {
    //TODO 界面美术资源占位
    protected static final ResourceLocation CHEST_TEXTURE =
            new ResourceLocation("textures/gui/container/shulker_box.png");

    public BaseTankScreen(BaseTankMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    protected BaseTankBlockEntity getBlockEntity() {
        return menu.getBlockEntity();
    }

    protected void renderFluidTank(GuiGraphics guiGraphics, int x, int y) {
        // 获取同步的流体数据
        FluidStack fluidStack=getBlockEntity().getFluidTank().getFluid();
        IClientFluidTypeExtensions renderProps = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        int fluidcolor = renderProps.getTintColor(fluidStack);
        int fluidAmount = fluidStack.getAmount();

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
}
