package org.liquidengine.legui.component.event.checkbox;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.liquidengine.legui.component.CheckBox;
import org.liquidengine.legui.component.Frame;
import org.liquidengine.legui.event.Event;
import org.liquidengine.legui.system.context.Context;

import java.util.Objects;

public class CheckBoxWidthChangeEvent extends Event<CheckBox> {
    private final float width;

    public CheckBoxWidthChangeEvent(CheckBox targetComponent, Context context, Frame frame, float width) {
        super(targetComponent, context, frame);
        this.width = width;
    }

    public float getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("width", width)
            .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CheckBoxWidthChangeEvent that = (CheckBoxWidthChangeEvent) o;
        return Float.compare(that.width, width) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), width);
    }
}
