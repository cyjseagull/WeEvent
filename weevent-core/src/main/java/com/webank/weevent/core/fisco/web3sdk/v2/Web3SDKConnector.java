package com.webank.weevent.core.fisco.web3sdk.v2;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.webank.weevent.client.BrokerException;
import com.webank.weevent.client.ErrorCode;
import com.webank.weevent.core.config.FiscoConfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.exceptions.ClientException;
import org.fisco.bcos.sdk.client.protocol.response.GroupList;
import org.fisco.bcos.sdk.crypto.CryptoInterface;
import org.fisco.bcos.sdk.model.NodeVersion;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Initialize client, connector to FISCO-BCOS.
 *
 * @author matthewliu
 * @since 2019/12/26
 */
@Slf4j
public class Web3SDKConnector {

    // The prefix of FISCO-BCOS version 2.X
    public static final String FISCO_BCOS_2_X_VERSION_PREFIX = "2.";

    // default group in FISCO-BCOS, already exist
    public static final String DEFAULT_GROUP_ID = "1";

    // FISCO-BCOS chain id
    public static String chainID;

    private Web3SDKConnector() {
    }

    /*
     * initialize client handler with given Service
     *
     * @param service web3sdk's service
     * @return Client
     */
    public static Client initClient(BcosSDK sdk, Integer groupId) throws BrokerException {
        // init client with given group id
        try {
            log.info("begin to initialize java-sdk's client, group id: {}", groupId);
            StopWatch sw = StopWatch.createStarted();

            // special thread for TransactionSucCallback.onResponse, callback from IO thread directly if not setting
            //service.setThreadPool(poolTaskExecutor);
            Client client = sdk.getClient(groupId);

            // check connect with getNodeVersion command
            NodeVersion.ClientVersion version = client.getNodeVersion().getNodeVersion();
            String nodeVersion = version.getVersion();
            if (StringUtils.isBlank(nodeVersion)
                    || !nodeVersion.contains(FISCO_BCOS_2_X_VERSION_PREFIX)) {
                log.error("init web3sdk failed, mismatch FISCO-BCOS version in node: {}", nodeVersion);
                throw new BrokerException(ErrorCode.WEB3SDK_INIT_ERROR);
            }
            chainID = version.getChainId();

            sw.stop();
            log.info("initialize web3sdk success, group id: {} cost: {} ms", service.getGroupId(), sw.getTime());
            return client;
        } catch (Exception e) {
            log.error("init web3sdk failed", e);
            throw new BrokerException(ErrorCode.WEB3SDK_INIT_ERROR);
        }
    }

    public static ThreadPoolTaskExecutor initThreadPool(int core, int max, int keepalive) {
        // init thread pool
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setThreadNamePrefix("web3sdk-");
        pool.setCorePoolSize(core);
        pool.setMaxPoolSize(max);
        // queue conflict with thread pool scale up, forbid it
        pool.setQueueCapacity(0);
        pool.setKeepAliveSeconds(keepalive);
        // abort policy
        pool.setRejectedExecutionHandler(null);
        pool.setDaemon(true);
        pool.initialize();

        log.info("init ThreadPoolTaskExecutor");
        return pool;
    }

    public static void getCredentials(CryptoInterface CryptoInterface, FiscoConfig fiscoConfig) {
        log.debug("begin init CryptoInterface");
        String accountFormat = "pem";
        CryptoInterface.loadAccount(accountFormat, fiscoConfig.getPemKeyPath());
        log.info("init CryptoInterface success");
    }

    public static List<String> listGroupId(Client client, int timeout) throws BrokerException {
        try {
            GroupList groupList = client.getGroupList();
            return groupList.getGroupList();
        } catch (ClientException | InterruptedException e) {
            log.error("web3sdk execute failed", e);
            throw new BrokerException(ErrorCode.TRANSACTION_EXECUTE_ERROR);
        } catch (TimeoutException e) {
            log.error("web3sdk execute timeout", e);
            throw new BrokerException(ErrorCode.TRANSACTION_TIMEOUT);
        }
    }
}
