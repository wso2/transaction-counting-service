/*
 *  Copyright (c) 2023, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.integration.transaction.counter.config;

import org.wso2.integration.transaction.counter.TransactionCounterConstants;
import org.wso2.integration.transaction.counter.exception.TransactionCounterConfigurationException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;

public class MIConfigFetcher implements ConfigFetcher {

    private static MIConfigFetcher instance = null;
    private final static HashMap<String, Object> configMap = new HashMap<>();

    private MIConfigFetcher() throws TransactionCounterConfigurationException {
        try {
            Class<?> configClass = Class.forName(TransactionCounterConstants.MI_CONFIG_CLASS);

            @SuppressWarnings("unchecked")
            HashMap<String, Object> configs = (HashMap<String, Object>) configClass
                    .getMethod("getParsedConfigs").invoke(null);

            // Reading the config values
            String  temp;
            Long tempLong;

            temp = (String) configs.get(TransactionCounterConstants.MI_SERVER_ID);
            String SERVER_ID = Objects.requireNonNull( temp, "Server ID cannot be null");

            temp = (String) configs.get(TransactionCounterConstants.MI_STORE_CLASS);
            String TRANSACTION_COUNT_STORE_CLASS = Objects.requireNonNull(temp,
                    "Transaction count store class cannot be null");

            tempLong = (Long) configs.get(TransactionCounterConstants.MI_QUEUE_SIZE);
            Objects.requireNonNull(tempLong, "Transaction record queue size cannot be null");
            Integer TRANSACTION_RECORD_QUEUE_SIZE = Integer.parseInt(tempLong.toString());

            tempLong = (Long) configs.get(TransactionCounterConstants.MI_PRODUCER_THREAD_POOL_SIZE);
            Objects.requireNonNull(tempLong, "Producer thread pool size cannot be null");
            Integer PRODUCER_THREAD_POOL_SIZE = Integer.parseInt(tempLong.toString());

            tempLong = (Long) configs.get(TransactionCounterConstants.MI_RECORD_INTERVAL);
            Objects.requireNonNull(tempLong, "Transaction count record interval cannot be null");
            Integer TRANSACTION_COUNT_RECORD_INTERVAL = Integer.parseInt(tempLong.toString());

            tempLong = (Long) configs.get(TransactionCounterConstants.MI_MAX_TRANSACTION_COUNT);
            Objects.requireNonNull(tempLong, "Max transaction count cannot be null");
            Double MAX_TRANSACTION_COUNT = Double.parseDouble(tempLong.toString());

            tempLong = (Long) configs.get(TransactionCounterConstants.MI_MIN_TRANSACTION_COUNT);
            Objects.requireNonNull(tempLong, "Min transaction count cannot be null");
            Double MIN_TRANSACTION_COUNT = Double.parseDouble(tempLong.toString());

            tempLong = (Long) configs.get(TransactionCounterConstants.MI_CONSUMER_COMMIT_INTERVAL);
            Objects.requireNonNull(tempLong, "Consumer commit interval cannot be null");
            Integer CONSUMER_COMMIT_INTERVAL = Integer.parseInt(tempLong.toString());

            tempLong = (Long) configs.get(TransactionCounterConstants.MI_MAX_TRANSACTION_RECORDS_PER_COMMIT);
            Objects.requireNonNull(tempLong, "Max transaction records per commit cannot be null");
            Integer MAX_TRANSACTION_RECORDS_PER_COMMIT = Integer.parseInt(tempLong.toString());

            tempLong = (Long) configs.get(TransactionCounterConstants.MI_MAX_RETRY_COUNT);
            Objects.requireNonNull(tempLong, "Max retry count cannot be null");
            Integer MAX_RETRY_COUNT = Integer.parseInt(tempLong.toString());

            temp = (String) configs.get(TransactionCounterConstants.MI_SERVICE);
            String TRANSACTION_COUNT_SERVICE = Objects.requireNonNull(temp,
                    "Transaction count service cannot be null");

            temp = (String) configs.get(TransactionCounterConstants.MI_SERVICE_USERNAME);
            String TRANSACTION_COUNT_SERVICE_USERNAME = Objects.requireNonNull(temp,
                    "Transaction count service username cannot be null");

            temp = (String) configs.get(TransactionCounterConstants.MI_SERVICE_PASSWORD);
            String TRANSACTION_COUNT_SERVICE_PASSWORD = Objects.requireNonNull(temp,
                    "Transaction count service password cannot be null");

            configMap.put(TransactionCounterConstants.SERVER_ID, SERVER_ID);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_STORE_CLASS, TRANSACTION_COUNT_STORE_CLASS);
            configMap.put(TransactionCounterConstants.TRANSACTION_RECORD_QUEUE_SIZE, TRANSACTION_RECORD_QUEUE_SIZE);
            configMap.put(TransactionCounterConstants.PRODUCER_THREAD_POOL_SIZE, PRODUCER_THREAD_POOL_SIZE);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_RECORD_INTERVAL , TRANSACTION_COUNT_RECORD_INTERVAL);
            configMap.put(TransactionCounterConstants.MAX_TRANSACTION_COUNT, MAX_TRANSACTION_COUNT);
            configMap.put(TransactionCounterConstants.MIN_TRANSACTION_COUNT, MIN_TRANSACTION_COUNT);
            configMap.put(TransactionCounterConstants.CONSUMER_COMMIT_INTERVAL, CONSUMER_COMMIT_INTERVAL);
            configMap.put(TransactionCounterConstants.MAX_TRANSACTION_RECORDS_PER_COMMIT, MAX_TRANSACTION_RECORDS_PER_COMMIT);
            configMap.put(TransactionCounterConstants.MAX_RETRY_COUNT, MAX_RETRY_COUNT);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_SERVICE, TRANSACTION_COUNT_SERVICE);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_SERVICE_USERNAME, TRANSACTION_COUNT_SERVICE_USERNAME);
            configMap.put(TransactionCounterConstants.TRANSACTION_COUNT_SERVICE_PASSWORD, TRANSACTION_COUNT_SERVICE_PASSWORD);

        } catch (ClassNotFoundException e) {
            // This error won't be thrown here because it is already checked in TransactionCountConfig
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new TransactionCounterConfigurationException(e);
        } catch (ClassCastException e) {
            throw new TransactionCounterConfigurationException("Error while parsing the config", e);
        }
    }

    public static MIConfigFetcher getInstance() throws TransactionCounterConfigurationException {
        if(instance == null) {
            instance = new MIConfigFetcher();
        }
        return instance;
    }

    @Override
    public String getConfigValue(String key) {
        return configMap.get(key).toString();
    }
}
