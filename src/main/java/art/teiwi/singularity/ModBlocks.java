package art.teiwi.singularity.registry;

import art.teiwi.singularity.blocks.SingularityBlock;
import art.teiwi.singularity.blocks.SingularityBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Singularity.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Singularity.MODID);

    public static final RegistryObject<Block> SINGULARITY_BLOCK = BLOCKS.register("singularity_block", SingularityBlock::new);

    public static final RegistryObject<BlockEntityType<SingularityBlockEntity>> SINGULARITY_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("singularity_block", () ->
            BlockEntityType.Builder.of(SingularityBlockEntity::new, SINGULARITY_BLOCK.get()).build(null));
}