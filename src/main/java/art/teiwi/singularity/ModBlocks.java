package art.teiwi.singularity;

import art.teiwi.singularity.block.SingularityBlock;
import art.teiwi.singularity.blockentity.SingularityBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Singularity.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Singularity.MODID);

    public static final RegistryObject<Block> SINGULARITY_BLOCK = BLOCKS.register("singularity_block",
            () -> new SingularityBlock(BlockBehaviour.Properties.of()
                    .strength(9.0f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}