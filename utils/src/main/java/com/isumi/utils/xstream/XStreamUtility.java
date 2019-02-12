package com.isumi.utils.xstream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.Xpp3Driver;

public class XStreamUtility {


    public static <T> String toXml(T t, String encoding){
        XStream xStream = XStreamFactory.getXStream ();
    	xStream.autodetectAnnotations(true);
        String headLine = "<?xml version=\"1.0\" encoding=\""+ encoding +"\"?>";
        return headLine + xStream.toXML ( t );
    }

    public static <T> T  fromXml(String xml,Class<T> clazz){
        XStream xtream = new XStream(new Xpp3Driver(new NoNameCoder()));//解决bean中下划线转成双下划线问题
        xtream.processAnnotations(clazz);//开启注解
        //Mapper mapper, String entryName, String keyName, Class keyType, String valueName, Class valueType
        Object result = xtream.fromXML(xml);
        return (T)result;

    }
}
