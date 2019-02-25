package ca.team21.pagepal;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestListTest {
    private RequestList requestList = new RequestList();
    ArrayList<Request> testList = new ArrayList<Request>();
    Request request1 = new Request();
    Request request2 = new Request();

    @Test
    public void addRequest() {
        testList.add(request1);
        testList.add(request2);

        requestList.addRequest(request1);
        requestList.addRequest(request2);
        assertEquals(testList, requestList.getRequests());
    }

    @Test
    public void delRequest() {
        testList.remove(request1);
        requestList.removeRequest(request1);

        assertEquals(testList, requestList.getRequests());
    }
}
