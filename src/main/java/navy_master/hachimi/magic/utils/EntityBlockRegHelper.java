package navy_master.hachimi.magic.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Constructor;
import java.util.function.Supplier;

import static navy_master.hachimi.magic.registry.ModBlockEntities.BLOCK_ENTITIES;
import static navy_master.hachimi.magic.registry.ModBlocks.BLOCKS;
import static navy_master.hachimi.magic.registry.ModItems.ITEMS;

public class EntityBlockRegHelper {

    public record  EntityBlockRegistration<T extends BlockEntity>(RegistryObject<BlockEntityType<T>> block_entity, RegistryObject<Block> block,
                                          RegistryObject<Item> item) {
    }

    public static <E extends BlockEntity,B extends Block> EntityBlockRegistration registerBlockEntities(String name, Class<E> clazz, Class<B> clazzB) {
        //注册Block
        Class<?>[] paramTypesBlock = {BlockBehaviour.Properties.class};
        Supplier<B> supB;
        try {
            Constructor<B> constructor = clazzB.getDeclaredConstructor(paramTypesBlock);
            constructor.setAccessible(true);

            supB = () -> {
                try {
                    return constructor.newInstance(BlockBehaviour.Properties.of());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create instance of " + clazzB.getName(), e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(clazzB.getName() + " has no Properties constructor", e);
        }
        RegistryObject<Block> block = BLOCKS.register(name,supB);


        //注册BlockEntities
        Class<?>[] paramTypesBlockEntities = {BlockPos.class,BlockState.class};
        BlockEntityType.BlockEntitySupplier<E> sup;
        try {
            Constructor<E> constructor = clazz.getDeclaredConstructor(paramTypesBlockEntities);
            constructor.setAccessible(true);

            sup = (pos, state) -> {
                try {
                    return constructor.newInstance(pos, state);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
                }
            };
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(clazz.getName() + " has no BlockEntityType constructor", e);
        }
        RegistryObject<BlockEntityType<E>> block_entity =
                BLOCK_ENTITIES.register(name, () ->
                        BlockEntityType.Builder.of(sup, block.get()).build(null));


        //注册物品
        RegistryObject<Item> item = ITEMS.register(name,
                () -> new BlockItem(block.get(), new Item.Properties()));

        return new EntityBlockRegistration(block_entity,block,item);
    }
}
