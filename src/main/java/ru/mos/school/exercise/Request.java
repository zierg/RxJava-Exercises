package ru.mos.school.exercise;

import java.util.Collection;
import java.util.HashSet;

public class Request
{
    public void addParam(String param)
    {
        params.add(param);
    }

    public Collection<String> getParams()
    {
        return params;
    }

    private Collection<String> params = new HashSet<>();
}
