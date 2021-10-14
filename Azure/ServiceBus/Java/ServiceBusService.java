package com.lr.myoffice.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.lr.myoffice.backend.helper.BaseJsonConverter;

/**
 * Connects to an azure service bus and provides its functionality
 */
@Service
public class ServiceBusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceBusService.class);

    /**
     * Sends a single message to the service bus
     *
     * @param queueConnectionString Service bus resource connection string
     * @param queueName Queue name on the service bus
     * @param message Message to push
     */
    public void push(String queueConnectionString, String queueName, ServiceBusMessage message) {
        LOGGER.info("Sending service bus message");
        ServiceBusSenderClient client = new ServiceBusClientBuilder()
                .connectionString(queueConnectionString)
                .sender()
                .queueName(queueName)
                .buildClient();

        client.sendMessage(message);
        client.close();
    }

    /**
     * Sends the messages in batches. The batch size depends on the individual message size, since the method
     * adds messages to a batch until the max. batch size for the service bus is reached or all messages have been
     * added.
     *
     * @param queueConnectionString Service bus resource connection string
     * @param queueName Queue name on the service bus
     * @param messages Message to push
     */
    public void batchedPush(String queueConnectionString, String queueName, List<ServiceBusMessage> messages) {
        ServiceBusSenderClient client = new ServiceBusClientBuilder()
                .connectionString(queueConnectionString)
                .sender()
                .queueName(queueName)
                .buildClient();
        var messageBatch = client.createMessageBatch();

        for (ServiceBusMessage m : messages) {
            if (messageBatch.tryAddMessage(m)) {
                continue;
            }

            client.sendMessages(messageBatch);
            messageBatch = client.createMessageBatch();

            if (!messageBatch.tryAddMessage(m)) {
                LOGGER.error(
                        "Message size to large for empty batch. Max size is " + messageBatch.getMaxSizeInBytes() + " bytes.");
            }
        }

        if (messageBatch.getCount() > 0) {
            client.sendMessages(messageBatch);
        }

        client.close();
    }

    /**
     * Converts the provided {@code message} into {@link com.azure.messaging.servicebus.ServiceBusMessage}
     * and uses {@link ServiceBusService#push(String, String, ServiceBusMessage)} to send the message.
     * <p></p>
     * <b>
     * Note:
     * </b>
     * Attempts to convert {@code T} into a json string. If serialization fails implement your own conversion and
     * use {@link ServiceBusService#push(String, String, ServiceBusMessage)} directly.
     *
     * @param queueConnectionString Service bus resource connection string
     * @param queueName Queue name on the service bus
     * @param message Message to push
     * @param <T> Message Type
     */
    public <T> void buildAndPush(String queueConnectionString, String queueName, T message) {
        LOGGER.info("Building service bus message...");
        var serializedMessage = this.getMessageBody(message);

        this.push(queueConnectionString, queueName, new ServiceBusMessage(serializedMessage));
    }

    /**
     * Converts the provided {@code messages} into {@link com.azure.messaging.servicebus.ServiceBusMessage}
     * and uses {@link ServiceBusService#batchedPush(String, String, List)} to send the messages.
     * <p></p>
     * <b>
     * Note:
     * </b>
     * Attempts to convert {@code T} into a json string. If serialization fails implement your own conversion and
     * use {@link ServiceBusService#batchedPush(String, String, List)} directly.
     *
     * @param queueConnectionString Service bus resource connection string
     * @param queueName Queue name on the service bus
     * @param messages Message to convert and push
     * @param <T> Message type
     */
    public <T> void buildAndBatchedPush(String queueConnectionString, String queueName, List<T> messages) {
        var builtMessages = this.buildMessages(messages);
        this.batchedPush(queueConnectionString, queueName, builtMessages);
    }

    private <T> List<ServiceBusMessage> buildMessages(List<T> messages) {
        LOGGER.info("Building service bus messages...");
        return messages.stream().map(
                message -> new ServiceBusMessage(this.getMessageBody(message))).collect(
                Collectors.toList());
    }

    private <T> String getMessageBody(T message) {
        BaseJsonConverter<T> x = (BaseJsonConverter<T>) BaseJsonConverter.create(message.getClass());
        return x.toJson(message);
    }
}
