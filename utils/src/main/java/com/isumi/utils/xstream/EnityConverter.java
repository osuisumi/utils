package com.isumi.utils.xstream;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.HashMap;
import java.util.Map;

//import org.xmlpull.mxp1.MXParser;

public class EnityConverter implements Converter {
    @Override
    //把对象序列化成xml
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {

    }

    /**
     * 如果entity中还有对象属性会报错
     * @param hierarchicalStreamReader
     * @param unmarshallingContext
     * @return
     */
    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
//        System.out.printf(unmarshallingContext.getClass() + "----------");
//        System.out.printf(XStreamUtility.toXml(unmarshallingContext,"utf-8"));
//        XppReader re = (XppReader)(hierarchicalStreamReader.underlyingReader());
        //System.out.printf(XStreamUtility.toXml(hierarchicalStreamReader,"utf-8"));
        EntityBean resultBean = new EntityBean();
        Map<String,String> resultMap = new HashMap<>();
        resultBean.setResult(resultMap);
        while(hierarchicalStreamReader.hasMoreChildren()){
            hierarchicalStreamReader.moveDown();
            resultMap.put(hierarchicalStreamReader.getNodeName(),hierarchicalStreamReader.getValue());
            hierarchicalStreamReader.moveUp();
        }
        return resultBean;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return EntityBean.class.equals(aClass);
    }
}
