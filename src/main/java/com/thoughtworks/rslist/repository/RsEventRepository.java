package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.RsEventDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RsEventRepository extends CrudRepository<RsEventDto, Integer> {
    List<RsEventDto> findAll();

    @Transactional
    void deleteAllByUserId(int userId);

    @Query(value = "select ifnull(max(rank), 0) from rs_event", nativeQuery = true)
    int findMaxRank();

    RsEventDto findByRank(int rank);
}
