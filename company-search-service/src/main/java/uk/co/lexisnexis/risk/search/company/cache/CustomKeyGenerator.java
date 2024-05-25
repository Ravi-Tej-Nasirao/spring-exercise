package uk.co.lexisnexis.risk.search.company.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * custom key generator
 *
 * @author ravin
 * @date 2024/05/25
 */
@Component
public class CustomKeyGenerator implements KeyGenerator {

    /**
     * generate
     *
     * @param target the target
     * @param method the method
     * @param params the params
     * @return Object generate
     */
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getSimpleName() + "_" + method.getName() + "_"
                + StringUtils.arrayToDelimitedString(params, "_");
    }

}