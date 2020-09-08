package com.webank.weevent.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractContext {

    private String chainId;

    private Long blockNumber;

    private Long blockLimit;

    private String topicAddress;

    public String getChainId() {
        return chainId;
    }

    public void setChainId(String chainId) {
        this.chainId = chainId;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Long getBlockLimit() {
        return blockLimit;
    }

    public void setBlockLimit(Long blockLimit) {
        this.blockLimit = blockLimit;
    }

    public String getTopicAddress() {
        return topicAddress;
    }

    public void setTopicAddress(String topicAddress) {
        this.topicAddress = topicAddress;
    }
}
