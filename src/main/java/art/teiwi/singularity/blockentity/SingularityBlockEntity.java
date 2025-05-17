package art.teiwi.singularity.blockentity;

import art.teiwi.singularity.menu.SingularityMenu;
import art.teiwi.singularity.ModBlocks;
import art.teiwi.singularity.config.SingularityConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraftforge.registries.ForgeRegistries;


import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SingularityBlockEntity extends BlockEntity implements MenuProvider {
    private final Map<String, Integer> items = new HashMap<>();
    private int itemProgress = 0;
    private UUID ownerUUID;
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> itemProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> itemProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public SingularityBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SINGULARITY_BLOCK_ENTITY.get(), pos, state);
        this.ownerUUID = null;
    }

    public boolean canPlayerInteract(Player player) {
        return ownerUUID == null || player.getUUID().equals(ownerUUID);
    }

    public void setOwner(Player player) {
        if (ownerUUID == null) {
            ownerUUID = player.getUUID();
        }
    }

    public void addItem(ItemStack stack, Player player) {
        if (stack.isEmpty() || !canPlayerInteract(player)) return;

        Item item = stack.getItem();
        String itemName = BuiltInRegistries.ITEM.getKey(item).toString();
        int count = stack.getCount();

        items.put(itemName, items.getOrDefault(itemName, 0) + count);
        itemProgress += count;

        if (itemProgress >= SingularityConfig.getMaxProgress()) {
            itemProgress = SingularityConfig.getMaxProgress();
        }

        stack.shrink(count);
        setChanged();
    }

    public Map<String, Integer> getItemsMap() {
        return new HashMap<>(items);
    }

    public int getItemProgress() {
        return itemProgress;
    }

    public void dropItems() {
        for (var entry : items.entrySet()) {
            String itemName = entry.getKey();
            int count = entry.getValue();
            var item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(itemName));
            if (item == null) continue;

            ItemStack stack = new ItemStack(item, count);
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
        }
        items.clear();
        itemProgress = 0;
        setChanged();
    }

    // --- NBT ---
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemProgress = tag.getInt("itemProgress");
        if (tag.contains("owner")) {
            ownerUUID = tag.getUUID("owner");
        }

        CompoundTag itemsTag = tag.getCompound("items");
        for (String key : itemsTag.getAllKeys()) {
            int count = itemsTag.getInt(key);
            items.put(key, count);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("itemProgress", itemProgress);
        if (ownerUUID != null) {
            tag.putUUID("owner", ownerUUID);
        }

        CompoundTag itemsTag = new CompoundTag();
        for (var entry : items.entrySet()) {
            itemsTag.putInt(entry.getKey(), entry.getValue());
        }
        tag.put("items", itemsTag);
    }

    // --- MenuProvider ---
    @Override
    public Component getDisplayName() {
        return Component.literal("Singularity");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SingularityMenu(id, inventory, this, dataAccess);
    }
}