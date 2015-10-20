package com.github.sigute.mindbleach.kittenapi;

import java.util.List;

public class KittensLoadedEvent
{
    private List<Kitten> kittens;

    KittensLoadedEvent(List<Kitten> kittens)
    {
        this.kittens = kittens;
    }

    public List<Kitten> getKittens()
    {
        return kittens;
    }
}
