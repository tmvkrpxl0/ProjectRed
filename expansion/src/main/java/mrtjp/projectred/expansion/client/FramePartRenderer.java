package mrtjp.projectred.expansion.client;

import codechicken.lib.render.CCRenderState;
import codechicken.multipart.api.part.render.PartRenderer;
import mrtjp.projectred.expansion.part.FramePart;
import net.minecraft.client.renderer.RenderType;
import javax.annotation.Nullable;

public class FramePartRenderer implements PartRenderer<FramePart> {

    public static final PartRenderer<FramePart> INSTANCE = new FramePartRenderer();

    private FramePartRenderer() {
    }

    @Override
    public void renderStatic(FramePart part, @Nullable RenderType type, CCRenderState ccrs) {
        if (type == null || type == RenderType.cutout()) {
            ccrs.setBrightness(part.level(), part.pos());
            FrameModelRenderer.renderStatic(ccrs, part.getOccludedSideMask());
        }
    }
}
