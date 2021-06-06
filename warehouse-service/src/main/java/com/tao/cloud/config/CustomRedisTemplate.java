package com.tao.cloud.config;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class CustomRedisTemplate<K, V> extends RedisTemplate<K, V> {

    private boolean enableTransactionSupport = false;

    private static boolean isActualNonReadonlyTransactionActive() {
        return TransactionSynchronizationManager.isActualTransactionActive()
                && !TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    }

    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        if (existingConnection && !Proxy.isProxyClass(connection.getClass()) && isActualNonReadonlyTransactionActive()) {
            RedisConnectionUtils.unbindConnection(getConnectionFactory());
            List<TransactionSynchronization> list = new ArrayList<>(TransactionSynchronizationManager.getSynchronizations());
            TransactionSynchronizationManager.clearSynchronization();
            TransactionSynchronizationManager.initSynchronization();
            list.remove(list.size() - 1);
            list.forEach(TransactionSynchronizationManager::registerSynchronization);
            connection = RedisConnectionUtils.bindConnection(getConnectionFactory(), enableTransactionSupport);
        }
        return connection;
    }

    @Override
    public void setEnableTransactionSupport(boolean enableTransactionSupport) {
        super.setEnableTransactionSupport(enableTransactionSupport);
        this.enableTransactionSupport = enableTransactionSupport;
    }
}
