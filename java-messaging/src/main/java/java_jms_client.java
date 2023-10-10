import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;

public class java_jms_client {
	public static void main(String[] args) throws JMSException {

		/*
		 * CHECK AND VALIDATE AWS CREDENTIALS
		 */
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();

	    try {
	        credentialsProvider.getCredentials();
	    } catch (Exception e) {
	        throw new AmazonClientException(
	                "Cannot load the credentials from the credential profiles file. " +
	                        "Please make sure that your credentials file is at the correct " +
	                        "location (~/.aws/credentials), and is in valid format.",
	                e);
	    }

	    //****************************************************************************************
	    /*
	     * CREATE A NEW QUEUE CALLED 'TEST' in the AWS SQS
	     */
	    AmazonSQS sqs = AmazonSQSClientBuilder.standard()
	            .withCredentials(credentialsProvider)
	            .withRegion(Regions.AP_SOUTHEAST_2)
	            .build();

	    //CREATE A NEW QUEUE
	    String QUEUE_NAME = "test";
	    CreateQueueRequest create_request = new CreateQueueRequest(QUEUE_NAME)
	            .addAttributesEntry("DelaySeconds", "60")
	            .addAttributesEntry("MessageRetentionPeriod", "86400");

	    try {
	        sqs.createQueue(create_request);
	    } catch (AmazonSQSException e) {
	        if (!e.getErrorCode().equals("QueueAlreadyExists")) {
	            throw e;
	        }
	    }

	    //****************************************************************************************

	    //Console output to show that the above codes executed
	    System.out.println("HelloWorld");

	}
}
