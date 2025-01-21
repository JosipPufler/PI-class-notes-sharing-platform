package hr.algebra.pi.observers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class MaterialEventManagerTest {

    private MaterialEventManager eventManager;

    @Mock
    private MaterialObserver observer1;

    @Mock
    private MaterialObserver observer2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventManager = new MaterialEventManager();
    }

    @Test
    void unsubscribe_RemovesObserver() {
        // Add two observers
        eventManager.subscribe(observer1);
        eventManager.subscribe(observer2);

        // Unsubscribe one observer
        eventManager.unsubscribe(observer1);

        // Notify event - only observer2 should receive it
        String testEvent = "TestEvent";
        String testData = "TestData";
        eventManager.notify(testEvent, testData);

        // Verify observer1 didn't receive notification
        verify(observer1, never()).onMaterialEvent(testEvent, testData);
        // Verify observer2 received notification
        verify(observer2, times(1)).onMaterialEvent(testEvent, testData);
    }

    @Test
    void unsubscribe_NonexistentObserver_DoesNotThrowException() {
        // Add one observer
        eventManager.subscribe(observer1);

        // Try to unsubscribe an observer that was never subscribed
        eventManager.unsubscribe(observer2);

        // Verify the system still works
        String testEvent = "TestEvent";
        String testData = "TestData";
        eventManager.notify(testEvent, testData);

        // Original observer should still receive notifications
        verify(observer1, times(1)).onMaterialEvent(testEvent, testData);
    }

    @Test
    void unsubscribe_AllObservers_NoNotifications() {
        // Add observers
        eventManager.subscribe(observer1);
        eventManager.subscribe(observer2);

        // Unsubscribe all
        eventManager.unsubscribe(observer1);
        eventManager.unsubscribe(observer2);

        // Notify event
        String testEvent = "TestEvent";
        String testData = "TestData";
        eventManager.notify(testEvent, testData);

        // Verify no observers received notifications
        verify(observer1, never()).onMaterialEvent(testEvent, testData);
        verify(observer2, never()).onMaterialEvent(testEvent, testData);
    }

    @Test
    void unsubscribe_SameObserverTwice_DoesNotThrowException() {
        // Add observer
        eventManager.subscribe(observer1);

        // Unsubscribe same observer twice
        eventManager.unsubscribe(observer1);
        eventManager.unsubscribe(observer1);

        // Notify event
        String testEvent = "TestEvent";
        String testData = "TestData";
        eventManager.notify(testEvent, testData);

        // Verify observer received no notifications
        verify(observer1, never()).onMaterialEvent(testEvent, testData);
    }
} 