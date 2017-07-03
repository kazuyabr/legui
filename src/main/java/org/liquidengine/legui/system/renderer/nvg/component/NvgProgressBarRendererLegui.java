package org.liquidengine.legui.system.renderer.nvg.component;

import org.joml.Vector2f;
import org.liquidengine.legui.component.ProgressBar;
import org.liquidengine.legui.system.context.LeguiContext;
import org.liquidengine.legui.system.renderer.nvg.NvgLeguiComponentRenderer;
import org.liquidengine.legui.system.renderer.nvg.util.NVGUtils;
import org.lwjgl.nanovg.NVGColor;

import static org.liquidengine.legui.system.renderer.nvg.NvgLeguiRenderer.renderBorder;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.createScissor;
import static org.liquidengine.legui.system.renderer.nvg.util.NvgRenderUtils.resetScissor;
import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Created by ShchAlexander on 11.02.2017.
 */
public class NvgProgressBarRendererLegui extends NvgLeguiComponentRenderer<ProgressBar> {
    private NVGColor colorA = NVGColor.malloc();

    @Override
    public void renderComponent(ProgressBar progressBar, LeguiContext leguiContext, long context) {
        createScissor(context, progressBar);
        {
            nvgSave(context);
            Vector2f pos  = progressBar.getScreenPosition();
            Vector2f size = progressBar.getSize();

            float cornerRadius = progressBar.getCornerRadius();

            nvgBeginPath(context);
            nvgRoundedRect(context, pos.x, pos.y, size.x, size.y, cornerRadius);
            nvgFillColor(context, NVGUtils.rgba(progressBar.getBackgroundColor(), colorA));
            nvgFill(context);

            nvgBeginPath(context);
            nvgRoundedRect(context, pos.x, pos.y, size.x * progressBar.getValue() / ProgressBar.MAX_VALUE, size.y, cornerRadius);
            nvgFillColor(context, NVGUtils.rgba(progressBar.getProgressColor(), colorA));
            nvgFill(context);

            renderBorder(progressBar, leguiContext);

        }
        resetScissor(context);
    }
}