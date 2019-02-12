package com.isumi.utils.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDomDriver;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;

public class XStreamFactory {
    public static XStream getXStream() {
        final NameCoder nameCoder = new NoNameCoder ();
        XStream xStream = new XStream(new XppDomDriver (nameCoder) {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter (out, nameCoder) {
                    boolean cdata = false;
                    Class<?> targetClass = null;
                    @Override
                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                        if(targetClass == null ||!clazz.equals(String.class)){
                            targetClass = clazz;
                        }
                        cdata = needCDATA(targetClass,name);
                    }

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }


                };
            }

            @Override
            protected synchronized XmlPullParser createParser() throws XmlPullParserException {
                return super.createParser();
            }

            @Override
            public HierarchicalStreamReader createReader(Reader in) {
                return super.createReader(in);
            }

            @Override
            public HierarchicalStreamReader createReader(InputStream in) {
                return super.createReader(in);
            }
        });
        xStream.aliasSystemAttribute(null, "class");//去掉class属性
        return xStream;
    }

    private static boolean needCDATA(Class<?> targetClass, String fieldAlias){
        boolean cdata = false;
        cdata = existsCDATA(targetClass, fieldAlias);
        if(cdata) return cdata;
        Class<?> superClass = targetClass.getSuperclass();
        while(!superClass.equals(Object.class)){
            cdata = existsCDATA(superClass, fieldAlias);
            if(cdata) return cdata;
            superClass = superClass.getSuperclass();
        }
        return false;
    }

    private static boolean existsCDATA(Class<?> clazz, String fieldAlias){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(field.getAnnotation(XStreamCDATA.class) != null ){
                XStreamAlias xStreamAlias = field.getAnnotation(XStreamAlias.class);
                if(null != xStreamAlias){
                    if(fieldAlias.equals(xStreamAlias.value()))//matched
                        return true;
                }else{
                    if(fieldAlias.equals(field.getName()))
                        return true;
                }
            }
        }
        return false;
    }
}
