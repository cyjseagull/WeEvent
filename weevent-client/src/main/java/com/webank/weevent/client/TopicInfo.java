package com.webank.weevent.client;

import lombok.Getter;
import lombok.Setter;

/**
 * Publish function result.
 * <p>
 *
 * @author matthewliu
 * @since 2018/11/02
 */
@Getter
@Setter
public class TopicInfo {
    /**
     * Topic name.
     */
    private String topicName;

    /**
     * Contract address in block chain.
     */
    private String topicAddress;

    /**
     * Creator's address.
     */
    private String senderAddress;

    /**
     * Create time.
     */
    private Long createdTimestamp;

    /**
     * Sequence number.
     */
    private Long sequenceNumber;

    /**
     * block number.
     */
    private Long blockNumber;

    /**
     * last publish time.
     */
    private Long lastTimestamp;

    /**
     * last publish sender.
     */
    private String lastSender;

    /**
     * last publish sender.
     */
    private Long lastBlock;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicAddress() {
        return topicAddress;
    }

    public void setTopicAddress(String topicAddress) {
        this.topicAddress = topicAddress;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public String getLastSender() {
        return lastSender;
    }

    public void setLastSender(String lastSender) {
        this.lastSender = lastSender;
    }

    public Long getLastBlock() {
        return lastBlock;
    }

    public void setLastBlock(Long lastBlock) {
        this.lastBlock = lastBlock;
    }
}
