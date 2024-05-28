package uk.co.lexisnexis.risk.search.company.cache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.springframework.stereotype.Component;

/**
 * company search cache event logger
 *
 * @author ravin
 * @date 2024/05/25
 */
@Component
@Slf4j
public class CompanySearchCacheEventLogger implements CacheEventListener<Object, Object> {

  /**
   * on event
   *
   * @param cacheEvent the cache event
   */
  @Override
  public void onEvent(CacheEvent cacheEvent) {
    log.info("Cache event = {}, Key = {},  Old value = {}, New value = {}", cacheEvent.getType(),
        cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
  }

}