package com.mgu.photoalbum.adapter.couchdb;

import com.fasterxml.jackson.databind.JsonNode;
import org.ektorp.CouchDbConnector;
import org.ektorp.DbInfo;
import org.ektorp.changes.ChangesCommand;
import org.ektorp.changes.ChangesFeed;
import org.ektorp.changes.DocumentChange;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Set of unit tests for {@link CouchDbChangesWorker}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class CouchDbChangesWorkerTest {

    private static final String TEST_DOCUMENT_TYPE_A = "testDocumentA";

    private static final String TEST_DOCUMENT_TYPE_B = "testDocumentB";

    private static final String TEST_DOCUMENT_CONTENT = "testContent";

    @Test
    public void constructorWithoutSequenceNumberShouldFetchItFromDb() throws Exception {
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(
                createMockConnector(createMockChangesFeed()),
                createMockChangeListeners(TEST_DOCUMENT_TYPE_A));
        final long lastSequenceNumber = Deencapsulation.getFieldValue(worker, "lastSequenceNumber");
        assertThat(lastSequenceNumber, is((long) 4711));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void constructorSetsGivenParametersToInstanceMembers() throws Exception {
        final CouchDbConnector mockConnector = createMockConnector(createMockChangesFeed());
        final Map<String, DocumentChangeListener> mockChangeListener = createMockChangeListeners(TEST_DOCUMENT_TYPE_A);
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(mockConnector,
                mockChangeListener, 1516);
        final long lastSequenceNumber = Deencapsulation.getFieldValue(worker, "lastSequenceNumber");
        assertThat(lastSequenceNumber, is((long) 1516));
        assertThat(Deencapsulation.getFieldValue(worker, "connector"),
                is(mockConnector));
        assertThat(Deencapsulation.getFieldValue(worker,
                "listenerByType"), is(mockChangeListener));
    }

    @Test
    public void workerShouldTerminateAfterRunningIsSetToFalse() throws Exception {
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(
                createMockConnector(createMockChangesFeed()),
                createMockChangeListeners(TEST_DOCUMENT_TYPE_A));
        final Thread workerThread = new Thread(worker);
        try {
            workerThread.start();
            assertThat(Deencapsulation.invoke(worker, "isRunning"), is(true));
            Thread.sleep(100);
            worker.stop();
            workerThread.join(1000);
            assertThat(workerThread.isAlive(), is(false));
            assertThat(Deencapsulation.invoke(worker, "isRunning"), is(false));
            // Make sure that the feed is stable and is only established once
            final CouchDbConnector mockConnector = Deencapsulation.getFieldValue(worker, "connector");
            verify(mockConnector, times(1)).changesFeed(any(ChangesCommand.class));
        } catch (Exception unexpected) {
            fail("Caught unexpected exception, failing test: " + unexpected.getMessage());
        }
    }

    @Test
    public void workerShouldReestablishFeedIfItWentDead() throws Exception {
        final MockChangesFeed changesFeed = createMockChangesFeed();
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(createMockConnector(changesFeed),
                createMockChangeListeners(TEST_DOCUMENT_TYPE_A));
        final Thread workerThread = new Thread(worker);
        try {
            workerThread.start();
            Thread.sleep(100);
            final CouchDbConnector mockConnector = Deencapsulation.getFieldValue(worker, "connector");
            verify(mockConnector, times(1)).changesFeed(any(ChangesCommand.class));
            Thread.sleep(200);
            // ChangesFeed.Alive auf false setzen und dann kurz warten und dem Worker-Thread genug Zeit geben, um die
            // Verbindung neu aufzubauen
            verify(mockConnector, times(1)).changesFeed(any(ChangesCommand.class));
            changesFeed.setAlive(false);
            Thread.sleep(30);
            verify(mockConnector, atLeast(2)).changesFeed(any(ChangesCommand.class));
            worker.stop();
            workerThread.join(1000);
        } catch (Exception unexpected) {
            fail("Caught unexpected exception, failing test: " + unexpected.getMessage());
        }
    }

    @Test
    public void workerShouldDispatchDocumentStringToAppropriateListener() throws Exception {
        final Map<String, DocumentChangeListener> mockChangeListener = createMockChangeListeners(TEST_DOCUMENT_TYPE_A);
        final DocumentChangeListener listener = mockChangeListener.get(TEST_DOCUMENT_TYPE_A);
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(
                createMockConnector(createMockChangesFeed()), mockChangeListener);
        final Thread workerThread = new Thread(worker);
        try {
            workerThread.start();
            Thread.sleep(200);
            verify(listener, atLeast(1)).onDocumentModified(eq(TEST_DOCUMENT_CONTENT), any(String.class), any(Integer.class));
            worker.stop();
            workerThread.join(100);
        } catch (Exception unexpected) {
            fail("Caught unexpected exception, failing test: " + unexpected.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void workerShouldHandleStartupWithoutConnectorProperly() throws Exception {
        final Map<String, DocumentChangeListener> mockChangeListener = createMockChangeListeners(TEST_DOCUMENT_TYPE_A);

        new CouchDbChangesWorker(null, mockChangeListener);
    }

    @Test
    public void workerShouldNotDispatchDocumentStringIfThereIsNoListener() throws Exception {
        final MockChangesFeed changesFeed = createMockChangesFeed(TEST_DOCUMENT_TYPE_B);
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(createMockConnector(changesFeed), new HashMap<>());
        final Thread workerThread = new Thread(worker);
        try {
            workerThread.start();
            Thread.sleep(200);
            assertTrue(changesFeed.getCounter() > 2);
            worker.stop();
            workerThread.join(100);
        } catch (Exception unexpected) {
            fail("Caught unexpected exception, failing test: " + unexpected.getMessage());
        }
    }

    @Test
    public void workerShouldNotDispatchDocumentStringIfThereIsNoAppropriateListener()
            throws Exception {
        final Map<String, DocumentChangeListener> mockChangeListener = createMockChangeListeners(TEST_DOCUMENT_TYPE_A);
        final DocumentChangeListener listener = mockChangeListener.get(TEST_DOCUMENT_TYPE_A);
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(
                createMockConnector(createMockChangesFeed(TEST_DOCUMENT_TYPE_B)), mockChangeListener);
        final Thread workerThread = new Thread(worker);
        try {
            workerThread.start();
            Thread.sleep(200);
            verify(listener, times(0)).onDocumentModified(any(String.class), any(String.class), any(Integer.class));
            worker.stop();
            workerThread.join(100);
        } catch (Exception unexpected) {
            fail("Caught unexpected exception, failing test: " + unexpected.getMessage());
        }
    }

    @Test
    public void workerShouldNotDispatchDocumentStringIfThereIsNoTypeAttached() throws Exception {
        final Map<String, DocumentChangeListener> mockChangeListener = createMockChangeListeners(TEST_DOCUMENT_TYPE_A);
        final DocumentChangeListener listener = mockChangeListener.get(TEST_DOCUMENT_TYPE_A);
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(
                createMockConnector(createMockChangesFeed(null)), mockChangeListener);
        final Thread workerThread = new Thread(worker);
        try {
            workerThread.start();
            Thread.sleep(200);
            verify(listener, times(0)).onDocumentModified(any(String.class), any(String.class), any(Integer.class));
            worker.stop();
            workerThread.join(100);
        } catch (Exception unexpected) {
            fail("Caught unexpected exception, failing test: " + unexpected.getMessage());
        }
    }

    @Test
    public void workerShouldBeResponsiveToCallsToStop() throws Exception {
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(
                createMockConnector(createMockChangesFeed()),
                createMockChangeListeners(TEST_DOCUMENT_TYPE_A));
        final Thread workerThread = new Thread(worker);
        try {
            workerThread.start();
            Thread.sleep(100);
            workerThread.interrupt();
            workerThread.join(500);
            assertThat(workerThread.isAlive(), is(true));
            // Call to WorkerThread.interrupt didn't have effect yet, but this call to worker.stop now does:
            worker.stop();
            workerThread.join(500);
            assertThat(workerThread.isAlive(), is(false));
        } catch (Exception unexpected) {
            fail("Caught unexpected exception, failing test: " + unexpected.getMessage());
        }
    }


    @Test
    public void workerDoesNotReactToThreadInterrupt() throws Exception {
        // The {@link CouchDbChangesWorker} does not react to Thread.interrupt due to its inherent design. This design doesn't
        // really test a "feature", it's here to be able to recognize if a future modification of the code changes this in an
        // way.
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(
                createMockConnector(createMockChangesFeed()),
                createMockChangeListeners(TEST_DOCUMENT_TYPE_A));
        final Thread workerThread = new Thread(worker);
        try {
            workerThread.start();
            Thread.sleep(50);
            workerThread.interrupt();
            workerThread.join(1000);
            assertThat(workerThread.isAlive(), is(true));
        } catch (Exception unexpected) {
            fail("Caught unexpected exception, failing test: " + unexpected.getMessage());
        }
    }

    @Test
    public void canHandleNullDocumentInDocumentChange() throws Exception {
        final CouchDbChangesWorker worker = new CouchDbChangesWorker(
                createMockConnector(createMockChangesFeedWithNullDocument()),
                createMockChangeListeners(TEST_DOCUMENT_TYPE_A));
        final Thread workerThread = new Thread(worker);
        try {
            workerThread.start();
            Thread.sleep(50);
            workerThread.interrupt();
            workerThread.join(1000);
            assertThat(workerThread.isAlive(), is(true));
        } catch (Exception unexpected) {
            fail("Caught unexpected exception, failing test: " + unexpected.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, DocumentChangeListener> createMockChangeListeners(final String documentType) {
        final Map<String, DocumentChangeListener> changeListenerByType = new HashMap<>();
        final DocumentChangeListener mockChangeListener = mock(DocumentChangeListener.class);
        changeListenerByType.put(documentType, mockChangeListener);
        return changeListenerByType;
    }

    private CouchDbConnector createMockConnector(final ChangesFeed changesFeed)
            throws InterruptedException {
        final CouchDbConnector mockConnector = mock(CouchDbConnector.class);
        final DbInfo mockDbInfo = mock(DbInfo.class);

        when(mockConnector.getDbInfo()).thenReturn(mockDbInfo);
        when(mockConnector.changesFeed(any(ChangesCommand.class))).thenReturn(changesFeed);
        when(mockDbInfo.getUpdateSeq()).thenReturn((long) 4711);

        return mockConnector;
    }

    private MockChangesFeed createMockChangesFeed() throws Exception {
        return createMockChangesFeed(TEST_DOCUMENT_TYPE_A);
    }

    private MockChangesFeed createMockChangesFeed(final String documentType) throws Exception {
        final DocumentChange mockDocumentChange = mock(DocumentChange.class);
        final JsonNode mockJsonDocumentNode = mock(JsonNode.class);
        final JsonNode mockJsonTypeNode = mock(JsonNode.class);

        when(mockDocumentChange.getSequence()).thenReturn(12);
        when(mockDocumentChange.getDocAsNode()).thenReturn(mockJsonDocumentNode);
        when(mockDocumentChange.getDoc()).thenReturn(TEST_DOCUMENT_CONTENT);
        if (documentType != null) {
            when(mockJsonDocumentNode.get("type")).thenReturn(mockJsonTypeNode);
        }
        when(mockJsonTypeNode.asText()).thenReturn(documentType);

        return new MockChangesFeed(mockDocumentChange);
    }

    @SuppressWarnings("unchecked")
    private MockChangesFeed createMockChangesFeedWithNullDocument() throws Exception {
        // This creates a DocumentChange that occurs when e.g. the database is being deleted. As far as I can tell right now
        // it is an edge case, but the CouchDbChangesWorker should be able to handle it.
        final DocumentChange mockDocumentChange = mock(DocumentChange.class);
        when(mockDocumentChange.getSequence()).thenThrow(NullPointerException.class);
        when(mockDocumentChange.getDocAsNode()).thenReturn(null);
        when(mockDocumentChange.getDoc()).thenReturn(null);
        return new MockChangesFeed(mockDocumentChange);
    }

    /**
     * Mock implementation of ChangesFeed that allows for a bit more control than Mockito.
     */
    private class MockChangesFeed implements ChangesFeed {

        private DocumentChange mockDocumentChange;

        private boolean canceled = false;

        private int counter = 0;

        private boolean alive = true;

        public MockChangesFeed(DocumentChange mockDocumentChange) {
            this.mockDocumentChange = mockDocumentChange;
        }

        @Override
        public DocumentChange next() throws InterruptedException {
            counter++;
            if (canceled) {
                throw new InterruptedException();
            }
            // slow down a bit to simplify tests where timing matters
            Thread.sleep(10);
            return mockDocumentChange;
        }

        @Override
        public DocumentChange poll() throws InterruptedException {
            throw new RuntimeException("not implemented");
        }

        @Override
        public DocumentChange next(long timeout, TimeUnit unit) throws InterruptedException {
            throw new RuntimeException("not implemented");
        }

        @Override
        public void cancel() {
            this.canceled = true;
        }

        @Override
        public boolean isAlive() {
            return this.alive;
        }

        @Override
        public int queueSize() {
            throw new RuntimeException("not implemented");
        }

        public int getCounter() {
            return counter;
        }

        public void setAlive(boolean alive) {
            this.alive = alive;
        }
    }
}
