package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.dto.TradeDto;
import com.thoughtworks.rslist.dto.VoteDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TradeRepository extends PagingAndSortingRepository<TradeDto, Integer> {
}
