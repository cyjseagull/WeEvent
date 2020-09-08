package com.webank.weevent.core.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * FISCO-BCOS Config that support loaded by spring context and pure java
 *
 * @author matthewliu
 * @version 1.0
 * @since 2019/1/28
 */
@Slf4j
@Getter
@Setter
@ToString
@Component
@PropertySource(value = "classpath:fisco.properties", encoding = "UTF-8")
public class FiscoConfig {
    public final static String propertiesFileKey = "block-chain-properties";

    @Value("${version:2.0}")
    private String version;

    @Value("${orgid:fisco}")
    private String orgId;

    @Value("${nodes:}")
    private String nodes;

    @Value("${account:bcec428d5205abe0f0cc8a734083908d9eb8563e31f943d760786edf42ad67dd}")
    private String account;

    @Value("${web3sdk.timeout:10000}")
    private Integer web3sdkTimeout;

    @Value("${web3sdk.core-pool-size:10}")
    private Integer web3sdkCorePoolSize;

    @Value("${web3sdk.max-pool-size:1000}")
    private Integer web3sdkMaxPoolSize;

    @Value("${web3sdk.keep-alive-seconds:10}")
    private Integer web3sdkKeepAliveSeconds;

    @Value("${web3sdk.encrypt-type:ECDSA_TYPE}")
    private String web3sdkEncryptType;

    @Value("${ca-crt-path:ca.crt}")
    private String CaCrtPath;

    @Value("${sdk-crt-path:sdk.crt}")
    private String SdkCrtPath;

    @Value("${sdk-key-path:sdk.key}")
    private String SdkKeyPath;

    @Value("${pem-key-path:privateKey.pem}")
    private String PemKeyPath;

    @Value("${consumer.idle-time:1000}")
    private Integer consumerIdleTime;

    @Value("${consumer.history_merge_block:8}")
    private Integer consumerHistoryMergeBlock;

    /**
     * load configuration without spring
     *
     * @param configFile config file, if empty load from default location
     * @return true if success, else false
     */
    public boolean load(String configFile) {
        return new SmartLoadConfig().load(this, configFile, propertiesFileKey);
    }

    public static String getPropertiesFileKey() {
        return propertiesFileKey;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getWeb3sdkTimeout() {
        return web3sdkTimeout;
    }

    public void setWeb3sdkTimeout(Integer web3sdkTimeout) {
        this.web3sdkTimeout = web3sdkTimeout;
    }

    public Integer getWeb3sdkCorePoolSize() {
        return web3sdkCorePoolSize;
    }

    public void setWeb3sdkCorePoolSize(Integer web3sdkCorePoolSize) {
        this.web3sdkCorePoolSize = web3sdkCorePoolSize;
    }

    public Integer getWeb3sdkMaxPoolSize() {
        return web3sdkMaxPoolSize;
    }

    public void setWeb3sdkMaxPoolSize(Integer web3sdkMaxPoolSize) {
        this.web3sdkMaxPoolSize = web3sdkMaxPoolSize;
    }

    public Integer getWeb3sdkKeepAliveSeconds() {
        return web3sdkKeepAliveSeconds;
    }

    public void setWeb3sdkKeepAliveSeconds(Integer web3sdkKeepAliveSeconds) {
        this.web3sdkKeepAliveSeconds = web3sdkKeepAliveSeconds;
    }

    public String getWeb3sdkEncryptType() {
        return web3sdkEncryptType;
    }

    public void setWeb3sdkEncryptType(String web3sdkEncryptType) {
        this.web3sdkEncryptType = web3sdkEncryptType;
    }

    public String getCaCrtPath() {
        return CaCrtPath;
    }

    public void setCaCrtPath(String caCrtPath) {
        CaCrtPath = caCrtPath;
    }

    public String getSdkCrtPath() {
        return SdkCrtPath;
    }

    public void setSdkCrtPath(String sdkCrtPath) {
        SdkCrtPath = sdkCrtPath;
    }

    public String getSdkKeyPath() {
        return SdkKeyPath;
    }

    public void setSdkKeyPath(String sdkKeyPath) {
        SdkKeyPath = sdkKeyPath;
    }

    public String getPemKeyPath() {
        return PemKeyPath;
    }

    public void setPemKeyPath(String pemKeyPath) {
        PemKeyPath = pemKeyPath;
    }

    public Integer getConsumerIdleTime() {
        return consumerIdleTime;
    }

    public void setConsumerIdleTime(Integer consumerIdleTime) {
        this.consumerIdleTime = consumerIdleTime;
    }

    public Integer getConsumerHistoryMergeBlock() {
        return consumerHistoryMergeBlock;
    }

    public void setConsumerHistoryMergeBlock(Integer consumerHistoryMergeBlock) {
        this.consumerHistoryMergeBlock = consumerHistoryMergeBlock;
    }
}
