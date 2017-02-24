package org.liquidengine.legui.marshal.json.gsonImpl;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.liquidengine.legui.border.Border;
import org.liquidengine.legui.border.SimpleLineBorder;
import org.liquidengine.legui.exception.LeguiException;
import org.liquidengine.legui.exception.LeguiExceptions;
import org.liquidengine.legui.marshal.json.gsonImpl.border.GsonBorderMarshaller;
import org.liquidengine.legui.marshal.json.gsonImpl.border.GsonSimpleLineBorderMarshaller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Aliaksandr_Shcherbin on 2/24/2017.
 */
public class GsonMarshalRegistry {
    private Map<Class<?>, AbstractGsonMarshaller<?>> marshallerMap = new ConcurrentHashMap<>();
    private BidiMap<String, Class<?>>                typeMap       = new DualHashBidiMap<>();
    private Lock                                     lock          = new ReentrantLock(false);

    private GsonMarshalRegistry() {
    }

    public static GsonMarshalRegistry getRegistry() {
        return JSRIH.I;
    }

    public Class<?> getClassByShortType(String shortName) {
        return typeMap.get(shortName);
    }

    public String getShortTypeByClass(Class<?> clazz) {
        return typeMap.getKey(clazz);
    }

    /**
     * Register marshaller for specified class
     *
     * @param typeName   short type name, can be null, in that case {@link GsonMarshalRegistry#getShortTypeByClass(Class)} will return null
     * @param tClass     class
     * @param marshaller marshaller
     * @param <T>        type parameter to prevent error in marshal registry.
     */
    public <T> void registerMarshaller(String typeName, Class<T> tClass, AbstractGsonMarshaller<T> marshaller) {
        lock.lock();
        try {
            if (typeName != null) {
                if (typeMap.containsKey(typeName)) {
                    throw new LeguiException(LeguiExceptions.GSON_REGISTRY_TYPE_EXIST.message(typeName));
                }
                typeMap.put(typeName, tClass);
            }
            marshallerMap.put(tClass, marshaller);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns marshaller for specified class
     *
     * @param tClass class
     * @param <T>    type of marshalled/demarshalled class
     * @return json marshaller for specified class
     */
    public <T> AbstractGsonMarshaller<T> getMarshaller(Class<T> tClass) {
        return treeGetMarshaller(tClass);
    }

    /**
     * Returns marshaller for specified classname
     *
     * @param classname full classname
     * @return json marshaller for specified classname
     */
    public AbstractGsonMarshaller getMarshaller(String classname) {
        try {
            Class tclass = Class.forName(classname);
            return getMarshaller(tclass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns marshaller for specified type
     *
     * @param typeName type name
     * @return json marshaller for specified type
     */
    public AbstractGsonMarshaller getMarshallerByShortType(String typeName) {
        Class<?> tclass = typeMap.get(typeName);
        if (tclass != null) {
            return getMarshaller(tclass);
        } else {
            return null;
        }
    }

    /**
     * Returns marshaller for specified class
     *
     * @param tClass class
     * @return json marshaller for specified class
     */
    private <T> AbstractGsonMarshaller<T> treeGetMarshaller(Class<T> tClass) {
        AbstractGsonMarshaller<T> marshaller = (AbstractGsonMarshaller<T>) marshallerMap.get(tClass);
        Class                     cClass     = tClass.getSuperclass();
        while (marshaller == null) {
            marshaller = (AbstractGsonMarshaller<T>) marshallerMap.get(cClass);
            if (cClass.isAssignableFrom(Object.class)) break;
            cClass = cClass.getSuperclass();
        }
        return marshaller;
    }

    /**
     * Singleton implementation "On demand holder"
     */
    private static class JSRIH {
        private static final GsonMarshalRegistry I = new GsonMarshalRegistry();

        static {
            // I.registerMarshaller("TextState", TextState.class, new GsonTextStateSerializer());

            I.registerMarshaller("Border", Border.class, new GsonBorderMarshaller());
            I.registerMarshaller("SimpleLineBorder", SimpleLineBorder.class, new GsonSimpleLineBorderMarshaller());

            // I.registerMarshaller("Button", Button.class, new GsonButtonSerializer());
            // I.registerMarshaller("CheckBox", CheckBox.class, new GsonCheckboxSerializer());
            // I.registerMarshaller("ComponentContainer", Container.class, new GsonContainerSerializer<>());
            // I.registerMarshaller("Component", Component.class, new GsonComponentSerializer<>());
            // I.registerMarshaller("Controller", Controller.class, new GsonControllerSerializer<>());
            // I.registerMarshaller("ImageView", ImageView.class, new GsonImageViewSerializer());
            // I.registerMarshaller("Label", Label.class, new GsonLabelSerializer());
            // I.registerMarshaller("Panel", Panel.class, new GsonPanelSerializer());
            // I.registerMarshaller("ProgressBar", ProgressBar.class, new GsonProgressBarSerializer());
            // I.registerMarshaller("RadioButton", RadioButton.class, new GsonRadioButtonSerializer());
            // I.registerMarshaller("ScrollablePanel", ScrollablePanel.class, new GsonScrollablePanelSerializer());
            // I.registerMarshaller("ScrollBar", ScrollBar.class, new GsonScrollBarSerializer());
            // I.registerMarshaller("SelectBox", SelectBox.class, new GsonSelectBoxSerializer());
            // I.registerMarshaller("Slider", Slider.class, new GsonSliderSerializer());
            // I.registerMarshaller("TextArea", TextArea.class, new GsonTextAreaSerializer());
            // I.registerMarshaller("TextInput", TextInput.class, new GsonTextInputSerializer());
            // I.registerMarshaller("Widget", Widget.class, new GsonWidgetSerializer());
        }
    }
}