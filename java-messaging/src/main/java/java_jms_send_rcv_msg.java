import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.*;

public class java_jms_send_rcv_msg {
	public static void main (String[] args) {

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

		//Ensure that the queue is available before sending message.
		AmazonSQS sqs = AmazonSQSClientBuilder.standard()
	            .withCredentials(credentialsProvider)
	            .withRegion(Regions.AP_SOUTHEAST_2)
	            .build();

		String queueUrl = "https://sqs.ap-southeast-2.amazonaws.com/386070351169/test";
		SendMessageRequest send_msg_request = new SendMessageRequest()
		        .withQueueUrl(queueUrl)
		        .withMessageBody("hello world2")
		        .withDelaySeconds(5);
		sqs.sendMessage(send_msg_request);

		/*
		 * Send Multiple Messages at Once
		 *
		 * Reference: https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-sqs-messages.html
		 *
		 * import com.amazonaws.services.sqs.AmazonSQS;
		 * import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
		 * import com.amazonaws.services.sqs.model.SendMessageRequest;
		 * */
		SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
		        .withQueueUrl(queueUrl)
		        .withEntries(
		                new SendMessageBatchRequestEntry(
		                        "msg_1", "Hello from message 1"),
		                new SendMessageBatchRequestEntry(
		                        "msg_2", "Hello from message 2")
		                        .withDelaySeconds(10));
		sqs.sendMessageBatch(send_batch_request);

		/*
		 * Receive Messages
		 *
		 * import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
		 * import com.amazonaws.services.sqs.model.AmazonSQSException;
		 * import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
		 * */
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
				  .withWaitTimeSeconds(3)
				  .withMaxNumberOfMessages(10);

		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		for(Message s: messages){
			System.out.println(s.getMessageId() + " | " + s.getBody());
		}

		// delete messages from the queue - won't be deleted due to WaitTime
		for (Message message : messages) {
            DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueUrl, message.getReceiptHandle());

            sqs.deleteMessage(deleteMessageRequest);
		}
		//Console output to show that the above codes executed
	    System.out.println("HelloWorld");
	}
}
