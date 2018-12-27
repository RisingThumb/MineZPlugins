package co.odsilvert.dsmz.util;

import co.odsilvert.dsmz.main.DSMZ;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class BinderModule extends AbstractModule {
    
    private final DSMZ plugin;

    public BinderModule(DSMZ plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(DSMZ.class).toInstance(this.plugin);
    }
}
