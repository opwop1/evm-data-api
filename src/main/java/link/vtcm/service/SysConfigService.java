package link.vtcm.service;

import java.util.function.Supplier;

public interface SysConfigService {
    String getValue(String key, Supplier<String> defaultValue);
    String getValue(String key);
}
