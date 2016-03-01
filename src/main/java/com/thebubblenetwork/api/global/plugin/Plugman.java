package com.thebubblenetwork.api.global.plugin;

import java.io.File;

public interface Plugman<P> {
    void disable(P p);

    void enable(P p);

    void unload(P p);

    P load(File jar);

    P get(String name);
}
