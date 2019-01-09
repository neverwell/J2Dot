package com.nemo.script;

import java.util.List;

public interface BootstrapScript extends IScript{
    List<Class<? extends IScript>> registerScript();
}
