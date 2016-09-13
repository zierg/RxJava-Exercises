package ru.mos.school.exercise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Response
{

    public void addParam(String param, String value)
    {
        params.put(param, value);
    }

    public Map<String, String> getParams()
    {
        return params;
    }

    @Override
    public String toString()
    {
        return params.toString();
    }

    private final Map<String, String> params = new ConcurrentHashMap<>();
}
