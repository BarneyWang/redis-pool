package com.letv.msgpack;

import org.assertj.core.util.Maps;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdi on 16-4-1.
 */
public class TestMsgPack {


    public static void main(String[] args) throws IOException {
        // Create serialize objects.
        List<String> src = new ArrayList<String>();
        src.add("msgpack");
        src.add("kumofs");
        src.add("viver");

        MessagePack msgpack = new MessagePack();
// Serialize
        byte[] raw = msgpack.write(src);

// Deserialize directly using a template
        List<String> dst1 = msgpack.read(raw, Templates.tList(Templates.TString));
        System.out.println(dst1.get(0));
        System.out.println(dst1.get(1));
        System.out.println(dst1.get(2));

// Or, Deserialze to Value then convert type.
        Value dynamic = msgpack.read(raw);
        List<String> dst2 = new Converter(dynamic)
                .read(Templates.tList(Templates.TString));
        System.out.println(dst2.get(0));
        System.out.println(dst2.get(1));
        System.out.println(dst2.get(2));

        Map<String, Object> map = Maps.newHashMap();
        map.put("id",1);
        map.put("name","111");

        byte[] maps = msgpack.write(map);

        Map<String, Object> dstMap = msgpack.read(maps, Templates.tMap(Templates.TString, ObjectTemplate.getInstance()));

        for(Map.Entry<String, Object> entry:dstMap.entrySet()){
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}
