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
import com.amazonaws.services.sqs.model.ListQueuesRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;

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
	     *
	     * Reference: https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-sqs-message-queues.html
	     *
	     * import com.amazonaws.services.sqs.AmazonSQS;
	     * import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
	     * import com.amazonaws.services.sqs.model.AmazonSQSException;
	     * import com.amazonaws.services.sqs.model.CreateQueueRequest;
	     */
	    AmazonSQS sqs = AmazonSQSClientBuilder.standard()
	            .withCredentials(credentialsProvider)
	            .withRegion(Regions.AP_SOUTHEAST_2)
	            .build();

	    //CREATE A NEW QUEUE
	    String QUEUE_NAME = "Queue_test";
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
	    /*
	     * LISTING QUEUES
	     * Reference: https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-sqs-message-queues.html
	     *
	     * import com.amazonaws.services.sqs.AmazonSQS;
	     * import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
	     * import com.amazonaws.services.sqs.model.ListQueuesResult;
	     * */

	    //AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	    ListQueuesResult lq_result = sqs.listQueues();
	    System.out.println("Your SQS Queue URLs:");
	    for (String url : lq_result.getQueueUrls()) {
	        System.out.println(url);
	    }

	    /*
	     * Using the listQueues overload without any parameters returns all queues.
	     * You can filter the returned results by passing it a ListQueuesRequest object.
	     * */
	    //AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	    String name_prefix = "Queue";
	    lq_result = sqs.listQueues(new ListQueuesRequest(name_prefix));
	    System.out.println("Queue URLs with prefix: " + name_prefix);
	    for (String url : lq_result.getQueueUrls()) {
	        System.out.println(url);
	    }

	    //Console output to show that the above codes executed
	    System.out.println("HelloWorld");

	}
}
