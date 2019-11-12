package quiz;

import javax.xml.ws.Endpoint;

public class Publisher {
	public static void main(String[] args) throws Exception {
		Endpoint.publish("http://0.0.0.0:8080/", new Api());
	}
}
