package art.teiwi.singularity.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SingularityConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue MAX_PROGRESS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Singularity Settings");

        MAX_PROGRESS = builder
                .comment("Максимальное значение ItemProgress")
                .defineInRange("max_item_progress", 1000, 1, 1000000);

        builder.pop();
        SPEC = builder.build();
    }
}