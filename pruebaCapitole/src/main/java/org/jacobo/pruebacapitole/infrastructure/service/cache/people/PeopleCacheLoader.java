package org.jacobo.pruebacapitole.infrastructure.service.cache.people;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jacobo.pruebacapitole.domain.model.people.PeopleResultDom;
import org.jacobo.pruebacapitole.domain.service.PeopleSwapiPort;
import org.jacobo.pruebacapitole.domain.service.cache.CacheLoaderPort;
import org.jacobo.pruebacapitole.domain.service.cache.NameCache;
import org.jacobo.pruebacapitole.domain.service.cache.people.PeopleIdCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component("peopleCacheLoader")
@RequiredArgsConstructor
public class PeopleCacheLoader implements CacheLoaderPort {
    private final PeopleSwapiPort peopleSwapiPort;
    private final NameCache<PeopleResultDom> peopleCache;
    @Qualifier("appThreadPool")
    private final ExecutorService executor;
    private final PeopleIdCache peopleIdCache;

    @PostConstruct
    public void loadCache() {
            val firstPage = peopleSwapiPort.getPeople();
            if (firstPage == null || firstPage.results() == null || firstPage.results().isEmpty()) {
                log.error("not found results check with swapi");
                return;
            }
            long count = firstPage.count();
            int pageSize = firstPage.results().size();
            int totalPages = (int) Math.ceil((double) count / pageSize);
            cacheFirstPageResults();
            val poolSize = getPoolSize(executor);
            int threadsToUse = Math.min(totalPages, poolSize);
            log.info("Total pages: {}. Thread pool size: {}. Threads to use : {}", totalPages, poolSize, threadsToUse);
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (int page = 2; page <= totalPages; page++) {
                final int currentPage = page;
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        val dom = peopleSwapiPort.getPeopleByPage(currentPage);
                        if (dom != null && dom.results() != null && !dom.results().isEmpty()) {
                            dom.results().forEach(singleResult ->
                                peopleCache.put(singleResult.getName().toLowerCase(), singleResult)
                            );
                        }
                        return;
                }, executor);
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            peopleIdCache.populateCache();
            log.info("People cache loaded in parallel with {} names", peopleCache.size());
    }

    private int getPoolSize(ExecutorService executor) {
            return ((ThreadPoolExecutor) executor).getMaximumPoolSize();

    }

    private void cacheFirstPageResults() {
        val firstPage = peopleSwapiPort.getPeople();
        if (firstPage != null && firstPage.results() != null && !firstPage.results().isEmpty()) {
            firstPage.results().forEach(singleResult ->
                peopleCache.put(singleResult.getName().toLowerCase(), singleResult)
            );
        }
    }
}
