package test0;

import java.lang.reflect.*;
import java.util.*;

public class GenericTypeTest {
    public static void main(String[] args) throws Exception {
        new GenericTypeTest().start();
    }

    void start() throws Exception {
        Method method = getClass().getDeclaredMethod("generic", List.class, Map.class);
        
        for (Type type : method.getGenericParameterTypes()) {
            ParameterizedType gType = (ParameterizedType) type;
            for (Type pType : gType.getActualTypeArguments()) {
                System.out.println(pType.getTypeName());
            }
        }
    }

    void generic(
        List<String> list,
        Map<String, List<String>> map
    ) {

    }
}
