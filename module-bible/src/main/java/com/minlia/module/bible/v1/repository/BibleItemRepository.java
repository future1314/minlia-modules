package com.minlia.module.bible.v1.repository;


import com.minlia.cloud.repository.AbstractRepository;
import com.minlia.module.bible.v1.domain.BibleItem;

import java.util.Set;

/**
 * Created by will on 6/21/17.
 */
public interface BibleItemRepository extends AbstractRepository<BibleItem,Long> {

    Set<BibleItem> findByBible_Code(String gender);

    BibleItem findOneByCode(String bibleItemCode);

    BibleItem findOneByBible_idAndCode(Long bibleId,String bibleItemCode);
}
