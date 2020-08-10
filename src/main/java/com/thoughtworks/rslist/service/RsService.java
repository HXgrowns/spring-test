package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Trade;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.TradeDto;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.VoteDto;
import com.thoughtworks.rslist.exception.RequestNotValidException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.TradeRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RsService {
    final RsEventRepository rsEventRepository;
    final UserRepository userRepository;
    final VoteRepository voteRepository;
    final TradeRepository tradeRepository;

    public RsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository, TradeRepository tradeRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.tradeRepository = tradeRepository;
    }

    public void vote(Vote vote, int rsEventId) {
        Optional<RsEventDto> rsEventDto = rsEventRepository.findById(rsEventId);
        Optional<UserDto> userDto = userRepository.findById(vote.getUserId());
        if (!rsEventDto.isPresent()
                || !userDto.isPresent()
                || vote.getVoteNum() > userDto.get().getVoteNum()) {
            throw new RuntimeException();
        }
        VoteDto voteDto =
                VoteDto.builder()
                        .localDateTime(vote.getTime())
                        .num(vote.getVoteNum())
                        .rsEvent(rsEventDto.get())
                        .user(userDto.get())
                        .build();
        voteRepository.save(voteDto);
        UserDto user = userDto.get();
        user.setVoteNum(user.getVoteNum() - vote.getVoteNum());
        userRepository.save(user);
        RsEventDto rsEvent = rsEventDto.get();
        rsEvent.setVoteNum(rsEvent.getVoteNum() + vote.getVoteNum());
        rsEventRepository.save(rsEvent);
    }

    /**
     * 1、判断要购买的事件是否存在
     * 2、找出要购买的排名所在的事件是否已经被购买
     * 3、若已被购买则判断金额<现金
     * 4、更改要购买的事件排名，删除旧事件
     * 5、保存在购买表中
     *
     * @param trade
     * @param id
     */
    public void buy(Trade trade, int id) {
        if (trade.getAmount() == 0) {
            throw new RequestNotValidException("trade fail");
        }
        RsEventDto rsEventDto = rsEventRepository.findById(id).orElseThrow(() -> new RequestNotValidException("reEvent is not exists"));
        RsEventDto byRank = rsEventRepository.findByRank(trade.getRank());

        if (byRank != null) {
            if (trade.getAmount() < byRank.getTradeNum()) {
                throw new RequestNotValidException("trade fail");
            } else {
                rsEventRepository.delete(byRank);
            }
        }
        rsEventDto.setRank(trade.getRank());
        rsEventDto.setTradeNum(trade.getAmount());
        rsEventRepository.save(rsEventDto);
        TradeDto build = TradeDto.builder().amount(trade.getAmount())
                .rank(trade.getRank())
                .reEvent(rsEventDto)
                .build();
        tradeRepository.save(build);
    }
}
