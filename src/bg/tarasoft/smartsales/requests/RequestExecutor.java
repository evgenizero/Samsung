package bg.tarasoft.smartsales.requests;

import java.util.Vector;

public class RequestExecutor {

	private Vector<SamsungRequests> requests;
	
	public RequestExecutor() {
		requests = new Vector<SamsungRequests>();
	}
	
	public void addRequest(SamsungRequests request) {
		requests.add(request);
		System.out.println("ADDED: " + request.toString());
	}
	
	public void execute() {
		try {
			SamsungRequests request;
			if(!requests.isEmpty()) {
				request = requests.firstElement();
				requests.remove(request);
				request.executeRequest();
				System.out.println("EXECUTE: " + request.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clear() {
		System.out.println("CLEARED");
		requests.clear();
	}
	
	public void addInFront(SamsungRequests request) {
		SamsungRequests req = requests.get(requests.size() - 1);
		requests.remove(requests.size() - 1);
		requests.insertElementAt(req, 0);
	}
	
}