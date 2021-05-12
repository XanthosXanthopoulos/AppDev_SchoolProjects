package com.example.demoapp.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EnumUtils
{
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, E extends Enum<E>> Function<T, E> lookupMap(Class<E> clazz, Function<E, T> mapper)
    {
        @SuppressWarnings("unchecked")
        E[] emptyArray = (E[]) Array.newInstance(clazz, 0);
        return lookupMap(EnumSet.allOf(clazz).toArray(emptyArray), mapper);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, E extends Enum<E>> Function<T, E> lookupMap(E[] values, Function<E, T> mapper)
    {
        Map<T, E> index = new HashMap<>(values.length);
        for (E value : values)
        {
            index.put(mapper.apply(value), value);
        }
        return index::get;
    }
}
