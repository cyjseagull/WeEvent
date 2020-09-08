package com.webank.weevent.core.fisco.util;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.webank.weevent.client.BrokerException;
import com.webank.weevent.core.config.FiscoConfig;
import com.webank.weevent.core.fisco.constant.WeEventConstants;
import com.webank.weevent.core.fisco.web3sdk.v2.CRUDAddress;
import com.webank.weevent.core.fisco.web3sdk.v2.SupportedVersion;
import com.webank.weevent.core.fisco.web3sdk.v2.Web3SDK2Wrapper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.exceptions.ClientException;
import org.fisco.bcos.sdk.service.GroupManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Getter
@Setter
@ToString
class EchoAddress {
    private Long version;
    private String address;
    private Boolean isNew;

    EchoAddress(Long version, String address, Boolean isNew) {
        this.version = version;
        this.address = address;
        this.isNew = isNew;
    }
}

/**
 * Utils to deploy FISCO-BCOS solidity contract.
 * It will deploy contract "TopicController", and save address back into block chain.
 * Usage:
 * java -classpath "./lib/*:./conf" com.webank.weevent.core.fisco.util.Web3sdkUtils
 *
 * @author matthewliu
 * @since 2019/02/12
 */
@Slf4j
public class Web3sdkUtils {
    private static Logger logger = LoggerFactory.getLogger(Web3sdkUtils.class);

    public static void main(String[] args) {
        try {
            FiscoConfig fiscoConfig = new FiscoConfig();
            String configFilePath = Web3sdkUtils.class.getClassLoader().getResource("config.toml").getPath();
            fiscoConfig.load("");
            ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
            taskExecutor.initialize();

            if (StringUtils.isBlank(fiscoConfig.getVersion())) {
                log.error("empty FISCO-BCOS version in fisco.properties");
                systemExit(1);
            }

            if (fiscoConfig.getVersion().startsWith(WeEventConstants.FISCO_BCOS_2_X_VERSION_PREFIX)) {    // 2.0x
                if (!deployV2Contract(configFilePath)) {
                    systemExit(1);
                }
            } else {
                log.error("unknown FISCO-BCOS version: {}", fiscoConfig.getVersion());
                systemExit(1);
            }
        } catch (Exception e) {
            log.error("deploy topic control contract failed", e);
            systemExit(1);
        }

        // web3sdk can't exit gracefully
        systemExit(0);
    }

    public static boolean deployV2Contract(String configFilePath) throws BrokerException{
        FiscoConfig fiscoConfig = new FiscoConfig();
        BcosSDK bcosSDK = new BcosSDK(configFilePath);
        GroupManagerService groupManagerService = bcosSDK.getGroupManagerService();
        Set<Integer> groupList = groupManagerService.getGroupList();
        logger.info("all group in nodes: {}", groupList.toString());
        Map<Integer, List<EchoAddress>> echoAddresses = new HashMap<>();
        for(Integer groupId: groupList)
        {
            List<EchoAddress> groupAddress = new ArrayList<>();
            Client client = bcosSDK.getClient(groupId);
            if (!dealOneGroup(client, groupAddress, fiscoConfig.getWeb3sdkTimeout())) {
                return false;
            }
            echoAddresses.put(groupId, groupAddress);
        }
        System.out.println(nowTime() + " topic control address in every group:");
        for(Map.Entry<Integer, List<EchoAddress>> addressInfo: echoAddresses.entrySet())
        {
            System.out.println("topic control address in group: " + addressInfo.getKey());
            for (EchoAddress address : addressInfo.getValue()) {
                System.out.println("\t" + address.toString());
            }
        }
        return true;
    }

    private static boolean dealOneGroup(Client client,
                                        List<EchoAddress> groupAddress,
                                        int timeout) throws BrokerException {
        CRUDAddress crudAddress = new CRUDAddress(client);
        Map<Long, String> original = crudAddress.listAddress();
        log.info("address list in CRUD groupId: {}, {}", client.getGroupId(), original);

        // if nowVersion exist
        boolean exist = false;
        // highest version in CRUD
        Long highestVersion = 0L;
        for (Map.Entry<Long, String> topicControlAddress : original.entrySet()) {
            groupAddress.add(new EchoAddress(topicControlAddress.getKey(), topicControlAddress.getValue(), false));

            if (!SupportedVersion.history.contains(topicControlAddress.getKey())) {
                log.error("unknown solidity version in group: {} CRUD: {}", client.getGroupId(), topicControlAddress.getKey());
                return false;
            }

            if (topicControlAddress.getKey() > highestVersion) {
                highestVersion = topicControlAddress.getKey();
            }

            if (SupportedVersion.nowVersion.equals(topicControlAddress.getKey())) {
                exist = true;
            }
        }

        if (exist) {
            log.info("find topic control address in now version groupId: {}, skip", client.getGroupId());
            return true;
        }

        // deploy topic control
        String topicControlAddress = Web3SDK2Wrapper.deployTopicControl(client, timeout);
        log.info("deploy topic control success, group: {} version: {} address: {}", client.getGroupId(), SupportedVersion.nowVersion, topicControlAddress);

        // flush topic info from low into new version
        if (highestVersion > 0L && highestVersion < SupportedVersion.nowVersion) {
            System.out.println(String.format("flush topic info from low version, %d -> %d", highestVersion, SupportedVersion.nowVersion));
            boolean result = SupportedVersion.flushData(client, original, highestVersion, SupportedVersion.nowVersion, timeout);
            if (!result) {
                log.error("flush topic info data failed, {} -> {}", highestVersion, SupportedVersion.nowVersion);
                return false;
            }
        }

        // save topic control address into CRUD
        boolean result = crudAddress.addAddress(SupportedVersion.nowVersion, topicControlAddress);
        log.info("save topic control address into CRUD, group: {} result: {}", client.getGroupId(), result);
        if (result) {
            groupAddress.add(new EchoAddress(SupportedVersion.nowVersion, topicControlAddress, true));
        }

        return result;
    }

    private static String nowTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private static void systemExit(int code) {
        System.out.flush();
        System.exit(code);
    }
}
