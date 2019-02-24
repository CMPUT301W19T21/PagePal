package ca.team21.pagepal;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestListTest {
    
    private Profile owner = new Profile();
    private Request request1 = new Request();
    private Request request2 = new Request();
    private Request request3 = new Request();
    private Request_List requestList = new Request_List(owner);
    private ArrayList<Request> testArray = new ArrayList<>();
    
    @Test
    public void addRequestTest() {

        requestList.addRequest(request1);
        testArray.add(request1);
        assertEquals(testArray, requestList.getRequestList());

        requestList.addRequest(request2);
        testArray.add(request2);
        assertEquals(testArray, requestList.getRequestList());

        requestList.addRequest(request3);
        testArray.add(request3);
        assertEquals(testArray, requestList.getRequestList());
        
    }
    
    @Test
    public void removeRequestTest() {

        requestList.addRequest(request1);
        requestList.removeRequest(request1);
        assertEquals(testArray, requestList.getRequestList());

        requestList.addRequest(request1);
        requestList.removeRequest(request2); // request doesn't exist in list, list should not be changed
        testArray.add(request1);
        assertEquals(testArray, requestList.getRequestList());
        
    }
    
}
