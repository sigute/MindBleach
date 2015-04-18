package com.github.sigute.mindbleach.kittenapi;

import java.util.List;

/**
 * Created by spikereborn on 18/04/2015.
 */
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
