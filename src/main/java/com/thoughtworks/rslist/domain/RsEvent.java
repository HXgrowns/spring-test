package com.thoughtworks.rslist.domain;

import com.thoughtworks.rslist.dto.RsEventDto;
import com.thoughtworks.rslist.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RsEvent implements Serializable {
    private int id;
    @NotNull
    private String eventName;
    @NotNull
    private String keyword;
    private int voteNum;
    @NotNull
    private int userId;
    private int tradeNum;
    private int rank;

    public RsEvent(@NotNull String eventName, @NotNull String keyword, @NotNull int userId) {
        this.eventName = eventName;
        this.keyword = keyword;
        this.userId = userId;
    }

    public RsEvent(RsEventDto rsEventDto) {
        this.id = rsEventDto.getId();
        this.eventName = rsEventDto.getEventName();
        this.keyword = rsEventDto.getKeyword();
        this.voteNum = rsEventDto.getVoteNum();
        this.userId = rsEventDto.getUser().getId();
        this.tradeNum = rsEventDto.getTradeNum();
        this.rank = rsEventDto.getRank();
    }
}
