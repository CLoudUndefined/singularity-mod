package art.teiwi.singularity.blockentity;

import art.teiwi.singularity.Singularity;
import art.teiwi.singularity.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Singularity.MODID);

    public static final RegistryObject<BlockEntityType<SingularityBlockEntity>> SINGULARITY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("singularity_block_entity", () ->
                    BlockEntityType.Builder.of(SingularityBlockEntity::new, ModBlocks.SINGULARITY_BLOCK.get())
                            .build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
