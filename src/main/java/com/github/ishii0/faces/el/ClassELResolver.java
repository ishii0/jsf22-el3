package com.github.ishii0.faces.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import javax.el.ELClass;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ImportHandler;

/**
 * JSF2.2 の仕様バグにより EL3 の static field/method の参照ができない問題を回避するための
 * ELResolver.
 * EL3 から static method や static field を参照できるようになったが、JSF では
 * static method/field を参照するための ELClass にするタイミングが仕様上なくなっている。
 * 具体的には、JSF で追加している一連の ELResolver のうち、最後の faces.ScopedAttributeELResolver
 * が必ず ELContext.setPropertyResolved(true) とする (5.6.2.9)ため、全ての ELResolver
 * が解決できなかった base を ELClass にすることができなくなっている。
 * このクラスでは、ELResolver として ELClass を返すようにすることで、この問題を回避する。
 * 
 * @author Kenji.Ishii
 */
public class ClassELResolver extends ELResolver {

    private Class<?> resolveClass(ELContext context, Object base, Object property) {
        if (base != null || property == null || !(property instanceof String)) {
            return null;
        }
        ImportHandler importHandler = context.getImportHandler();
        String simpleName = property.toString();
        Class<?> klass =  importHandler.resolveClass(simpleName);
        if (klass != null) {
            context.setPropertyResolved(true);
        }
        return klass;
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        Class<?> klass = resolveClass(context, base, property);
        return klass == null ? null : new ELClass(klass);
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return resolveClass(context, base, property) != null ? ELClass.class : null;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        resolveClass(context, base, property);
        return false;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return null;
    }
}
