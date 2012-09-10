package org.switchyard.internal.monitoring;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;
import javax.xml.namespace.QName;

import org.junit.Test;
import org.switchyard.ExchangePhase;
import org.switchyard.ExchangeState;
import org.switchyard.MockExchange;
import org.switchyard.internal.DefaultContext;
import org.switchyard.internal.DefaultMessage;
import org.switchyard.metadata.BaseExchangeContract;
import org.switchyard.metadata.InOnlyOperation;
import org.switchyard.runtime.event.ExchangeSnapshot;
import org.switchyard.runtime.event.ExchangeSnapshotFactory;

public class TestExchangeSnapshot {

	@Test
	public void exchangeSnapshotFactory() {
		ExchangeSnapshotFactory instance = null;
		try {
			instance = ExchangeSnapshotFactory.getInstance();
			assertNotNull(instance);
		} catch (Exception e) {
			fail("Factory instatiation failed");
		}
	}

	@Test
	public void exchangeSnapshotCreate() {
		ExchangeSnapshotFactory instance = ExchangeSnapshotFactory
				.getInstance();
		MockExchange mockExchange = createExchange();
		
		long startedAt = System.currentTimeMillis();
		ExchangeSnapshot snapshot = instance.createSnapshot(mockExchange);
		assertNotNull(snapshot);

		System.out.println("Snapshot created at "
				+ (System.currentTimeMillis() - startedAt) + " ms");
		
		assertNotNull(snapshot.getContext());
		assertNotNull(snapshot.getServiceName());
		assertNotNull(snapshot.getContract());
		assertNotNull(snapshot.getPhase());
		assertNotNull(snapshot.getState());

		assertNotNull(snapshot.getMessageBytes());
		assertNotNull(snapshot.getMessage());
		
		// restore & compare
		
		mockExchange.getMessage().setContent("CHANGED");
		assertEquals("ORIGINAL", snapshot.getMessage().getContent());
	}

	private MockExchange createExchange() {
		MockExchange mockExchange = new MockExchange();
		mockExchange.setContext(new DefaultContext());
		
		InOnlyOperation testOperation = new InOnlyOperation("test");
		testOperation.setInputType(QName.valueOf("test:input"));

		BaseExchangeContract exchangeContract = new BaseExchangeContract(
				testOperation, testOperation);
		
		mockExchange.setContract(exchangeContract);
		mockExchange.setServiceName(QName.valueOf("test:inputService"));
		
		mockExchange.setPhase(ExchangePhase.IN);
		mockExchange.setState(ExchangeState.OK);
		
		mockExchange.setMessage(new DefaultMessage());
		mockExchange.getMessage().setContent("ORIGINAL");
		return mockExchange;
	}
	
	@Test
	public void testSnapshotPerformance() {
		ExchangeSnapshotFactory instance = ExchangeSnapshotFactory
				.getInstance();

		for (int i = 0; i < 20; i++) {
			MockExchange mockExchange = createExchange();

			char[] buf = new char[(int)(Math.random() * 1024)];
			for(int x = buf.length-1; x>=0;x--) {
				buf[x] = (char)(Math.random() * 128);
			}
			
			
			
			mockExchange.getMessage().setContent(new String(buf));
			
			// add an attachment
			byte[] bufAttachment = new byte[(int)(1024 * 1024 * 5)];
			for(int x = buf.length-1; x>=0;x--) {
				bufAttachment[x]=(byte)(Math.random() * 128);
			}
			
			mockExchange.getMessage().addAttachment("file", new MockDataSource("mock", "applicaton/octet-stream", bufAttachment));
			
			long startedAt = System.currentTimeMillis();
			ExchangeSnapshot snapshot = instance.createSnapshot(mockExchange);
			assertNotNull(snapshot);

			System.out.println("Snapshot created at "
					+ (System.currentTimeMillis() - startedAt) + " ms");
		}
	}
	
	private static final class MockDataSource implements DataSource {
        private String _name;
        private String _contentType;
        private byte[] _content;
        private MockDataSource(String name, String contentType, byte[] content) {
            _name = name;
            _contentType = contentType;
            _content = content;
        }
        public String getName() {
            return _name;
        }
        public String getContentType() {
            return _contentType;
        }
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(_content);
        }
        public OutputStream getOutputStream() throws IOException {
            return null;
        }
    }
}
