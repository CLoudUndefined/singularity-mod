package art.teiwi.singularity.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import art.teiwi.singularity.Singularity;

@Mod.EventBusSubscriber(modid = Singularity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SingularityConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue MAX_PROGRESS;

    private static int defaultMaxProgress = 1000;
    
    private static int currentMaxProgress = defaultMaxProgress;

    static {
        BUILDER.push("Singularity Settings");

        MAX_PROGRESS = BUILDER
                .comment("Максимальное значение ItemProgress")
                .defineInRange("max_item_progress", defaultMaxProgress, 1, 1000000);

        BUILDER.pop();
        COMMON_CONFIG = BUILDER.build();
    }
    
    @SubscribeEvent
    public static void onModConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == COMMON_CONFIG) {
            updateCachedValues();
        }
    }

    @SubscribeEvent
    public static void onModConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == COMMON_CONFIG) {
            updateCachedValues();
        }
    }
    
    private static void updateCachedValues() {
        try {
            currentMaxProgress = MAX_PROGRESS.get();
        } catch (Exception e) {
            currentMaxProgress = defaultMaxProgress;
        }
    }
    
    public static int getMaxProgress() {
        return currentMaxProgress;
    }
}