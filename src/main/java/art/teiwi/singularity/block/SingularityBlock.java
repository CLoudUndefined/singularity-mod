package art.teiwi.singularity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import art.teiwi.singularity.blockentity.SingularityBlockEntity;
import net.minecraft.world.level.block.Block;


import javax.annotation.Nullable;

public class SingularityBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 16, 12); // размер как у Anchor

    public SingularityBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SingularityBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SingularityBlockEntity singularity) {
                if (!heldItem.isEmpty()) {
                    if (singularity.canPlayerInteract(player)) {
                        singularity.addItem(heldItem, player);
                        return InteractionResult.CONSUME;
                    }
                } 
                else {
                    NetworkHooks.openScreen((ServerPlayer) player, singularity, pos);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            var be = level.getBlockEntity(pos);
            if (be instanceof SingularityBlockEntity singularity) {
                singularity.dropItems();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
