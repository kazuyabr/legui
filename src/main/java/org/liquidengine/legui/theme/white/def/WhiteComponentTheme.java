package org.liquidengine.legui.theme.white.def;

import org.liquidengine.legui.border.SimpleLineBorder;
import org.liquidengine.legui.color.ColorConstants;
import org.liquidengine.legui.component.Component;
import org.liquidengine.legui.theme.AbstractTheme;

/**
 * White Component Theme for all components. Used to make component white.
 *
 * @param <T> {@link Component} subclasses.
 */
public class WhiteComponentTheme<T extends Component> extends AbstractTheme<T> {

    @Override
    public void apply(T component) {
        component.setBorder(new SimpleLineBorder(ColorConstants.darkGray(), .7f));
        component.setCornerRadius(2);
        component.setBackgroundColor(ColorConstants.white());
    }
}
