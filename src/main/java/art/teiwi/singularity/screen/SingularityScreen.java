package art.teiwi.singularity.screen;

import art.teiwi.singularity.Singularity;
import art.teiwi.singularity.blockentity.SingularityBlockEntity;
import art.teiwi.singularity.config.SingularityConfig;
import art.teiwi.singularity.menu.SingularityMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SingularityScreen extends AbstractContainerScreen<SingularityMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Singularity.MODID, "textures/gui/singularity_block_gui.png");

    public SingularityScreen(SingularityMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.imageHeight = 166 + 50;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, 166);
        
        renderProgressBar(guiGraphics, x, y);
    }

    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        int progress = menu.data.get(0);
        int maxProgress = SingularityConfig.getMaxProgress();
        
        if (maxProgress > 0 && progress > 0) {
            int barX = x + 80;
            int barY = y + 55;
            int barWidth = 90;
            int barHeight = 5;
            
            int progressWidth = (int)((float)progress / maxProgress * barWidth);
            
            guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF555555);
            
            guiGraphics.fill(barX, barY, barX + progressWidth, barY + barHeight, 0xFF00FF00);
            
            String progressText = progress + "/" + maxProgress;
            guiGraphics.drawString(font, progressText, barX + (barWidth / 2) - (font.width(progressText) / 2), barY - 10, 0xFFFFFF);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}