package art.teiwi.singularity.menu;

import art.teiwi.singularity.ModBlocks;
import art.teiwi.singularity.blockentity.SingularityBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class SingularityMenu extends AbstractContainerMenu {
    public final SingularityBlockEntity blockEntity;
    private final Level level;
    public final ContainerData data;

    public SingularityMenu(int containerId, Inventory playerInventory, SingularityBlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.SINGULARITY_MENU.get(), containerId);
        checkContainerSize(playerInventory, 1);
        this.blockEntity = blockEntity;
        this.level = playerInventory.player.level();
        this.data = data;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(data);
    }

    public SingularityMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, extraData), new SimpleContainerData(1));
    }

    private static SingularityBlockEntity getBlockEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        if (data == null) throw new IllegalArgumentException("FriendlyByteBuf data cannot be null!");
        final BlockEntity be = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (be instanceof SingularityBlockEntity) {
            return (SingularityBlockEntity) be;
        }
        throw new IllegalStateException("Block entity is not correct! " + be);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        // Shift‑клик don't support >:[
        return ItemStack.EMPTY;
    }


    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, ModBlocks.SINGULARITY_BLOCK.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}