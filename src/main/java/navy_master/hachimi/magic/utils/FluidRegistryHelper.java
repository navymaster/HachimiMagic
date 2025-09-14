package navy_master.hachimi.magic.utils;

import navy_master.hachimi.magic.HachimiMagic;
import navy_master.hachimi.magic.registry.ModBlocks;
import navy_master.hachimi.magic.registry.ModFluidTypes;
import navy_master.hachimi.magic.registry.ModFluids;
import navy_master.hachimi.magic.registry.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidRegistryHelper {
    public record FluidRegistration(RegistryObject<FluidType> type, RegistryObject<FlowingFluid> source,
                                    RegistryObject<FlowingFluid> flowing, RegistryObject<LiquidBlock> block,
                                    RegistryObject<Item> bucket) {
    }

    public static FluidRegistration registerFluid(String name, int tintColor) {
        ResourceLocation stillTexture = new ResourceLocation(HachimiMagic.MODID, "block/" + name + "_still");
        ResourceLocation flowingTexture=new ResourceLocation(HachimiMagic.MODID, "block/"+name+"_flowing");
        // 1. 注册流体类型
        RegistryObject<FluidType> type = ModFluidTypes.FLUID_TYPES.register(name + "_type",
                () -> new FluidType(FluidType.Properties.create()
                        .density(1000)
                        .viscosity(1000)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                        .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)) {

                    @Override
                    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                        consumer.accept(new IClientFluidTypeExtensions() {
                            @Override
                            public ResourceLocation getStillTexture() {
                                return stillTexture;
                            }

                            @Override
                            public ResourceLocation getFlowingTexture() {
                                return flowingTexture;
                            }

                            @Override
                            public int getTintColor() {
                                return tintColor;
                            }

                            @Override
                            public int getTintColor(FluidStack stack) {
                                return getTintColor();
                            }
                        });
                    }
                });

        // 2. 创建源流体和流动流体的引用
        final RegistryObject<FlowingFluid>[] sourceRef = new RegistryObject[]{null};
        final RegistryObject<FlowingFluid>[] flowingRef = new RegistryObject[]{null};

        // 3. 创建Properties的Supplier
        Supplier<ForgeFlowingFluid.Properties> propertiesSupplier = () -> {
            // 延迟获取方块和桶
            Supplier<LiquidBlock> blockSupplier = () -> ModBlocks.BLOCKS.getEntries().stream()
                    .filter(e -> e.getId().getPath().equals(name + "_block"))
                    .map(e -> (LiquidBlock) e.get())
                    .findFirst()
                    .orElse(null);

            RegistryObject<Item> bucket = ModItems.ITEMS.getEntries().stream()
                    .filter(e -> e.getId().getPath().equals(name + "_bucket"))
                    .findFirst()
                    .orElse(null);

            return new ForgeFlowingFluid.Properties(
                    type,
                    sourceRef[0], // 使用源流体引用
                    flowingRef[0] // 使用流动流体引用
            )
                    .slopeFindDistance(2)
                    .levelDecreasePerBlock(2)
                    .block(blockSupplier)
                    .bucket(bucket);
        };

        // 4. 注册源流体
        sourceRef[0] = ModFluids.FLUIDS.register(name + "_source",
                () -> new ForgeFlowingFluid.Source(propertiesSupplier.get()));

        // 5. 注册流动流体
        flowingRef[0] = ModFluids.FLUIDS.register(name + "_flowing",
                () -> new ForgeFlowingFluid.Flowing(propertiesSupplier.get()));

        // 6. 注册流体方块
        RegistryObject<LiquidBlock> block = ModBlocks.BLOCKS.register(name + "_block",
                () -> new LiquidBlock(sourceRef[0], BlockBehaviour.Properties.copy(Blocks.WATER)));

        // 7. 注册桶
        RegistryObject<Item> bucket = ModItems.ITEMS.register(name + "_bucket",
                () -> new BucketItem(sourceRef[0], new Item.Properties().stacksTo(1)));

        return new FluidRegistration(type, sourceRef[0], flowingRef[0], block, bucket);
    }
}