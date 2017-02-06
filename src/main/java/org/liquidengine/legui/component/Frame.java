package org.liquidengine.legui.component;

import org.apache.commons.collections4.list.SetUniqueList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Frame {
    protected Layer tooltipLayer;
    protected Layer componentLayer;
    protected List<Layer> layers = SetUniqueList.setUniqueList(new CopyOnWriteArrayList<>());

    public Frame(int width, int height) {
        initialize(width, height);
    }

    private void initialize(int width, int height) {
        tooltipLayer = new Layer();
        componentLayer = new Layer();
        tooltipLayer.getContainer().getSize().set(width, height);
        componentLayer.getContainer().getSize().set(width, height);
    }

    public void addLayer(Layer layer) {
        if (layer == null ||
                layer == tooltipLayer ||
                layer == componentLayer ||
                layer.getFrame() == this) {
            return;
        }

        if (layer.getFrame() != null) {
            layer.getFrame().removeLayer(layer);
        }
        layer.setFrame(this);
        layers.add(layer);
    }

    public void removeLayer(Layer layer) {
        if (layer == null) {
            return;
        }
        layer.setFrame(this);
        if (layers.contains(layer)) {
            layers.remove(layer);
        }
    }

    public Layer getComponentLayer() {
        return componentLayer;
    }

    public Layer getTooltipLayer() {
        return tooltipLayer;
    }

    public List<Layer> getLayers() {
        return new ArrayList<>(layers);
    }

    public List<Layer> getAllLayers() {
        ArrayList<Layer> layers = new ArrayList<>();
        layers.add(componentLayer);
        layers.addAll(this.layers);
        layers.add(tooltipLayer);
        return layers;
    }

    public Container getContainer(){
        return componentLayer.getContainer();
    }
}