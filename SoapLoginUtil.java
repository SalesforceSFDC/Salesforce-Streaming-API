package demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public final class SoapLoginUtil {

    // The enterprise SOAP API endpoint used for the login call in this example.
    private static final String SERVICES_SOAP_PARTNER_ENDPOINT = "/services/Soap/u/22.0/";

    private static final String ENV_START =
            "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' "
                    + "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
                    "xmlns:urn='urn:partner.soap.sforce.com'><soapenv:Body>";

    private static final String ENV_END = "</soapenv:Body></soapenv:Envelope>";

    private static byte[] soapXmlForLogin(String username, String password) 
        throws UnsupportedEncodingException {
        return (ENV_START +
                "  <urn:login>" +
                "    <urn:username>" + username + "</urn:username>" +
                "    <urn:password>" + password + "</urn:password>" +
                "  </urn:login>" +
                ENV_END).getBytes("UTF-8");
    }

    public static String[] login(HttpClient client, String username, String password)
            throws IOException, InterruptedException, SAXException, 
                ParserConfigurationException {

        ContentExchange exchange = new ContentExchange();
        exchange.setMethod("POST");
        exchange.setURL(getSoapURL());
        exchange.setRequestContentSource(new ByteArrayInputStream(soapXmlForLogin(
            username, password)));
        exchange.setRequestHeader("Content-Type", "text/xml");
        exchange.setRequestHeader("SOAPAction", "''");
        exchange.setRequestHeader("PrettyPrint", "Yes");

        client.send(exchange);
        exchange.waitForDone();
        String response = exchange.getResponseContent();

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();

        LoginResponseParser parser = new LoginResponseParser();
        saxParser.parse(new ByteArrayInputStream(
            response.getBytes("UTF-8")), parser);

        if (parser.sessionId == null || parser.serverUrl == null) {
            System.out.println("Login Failed!\n" + response);
            return null;
        }

        URL soapEndpoint = new URL(parser.serverUrl);
        StringBuilder endpoint = new StringBuilder()
            .append(soapEndpoint.getProtocol())
            .append("://")
            .append(soapEndpoint.getHost());

        if (soapEndpoint.getPort() > 0) endpoint.append(":")
            .append(soapEndpoint.getPort());
        return new String[] {parser.sessionId, endpoint.toString()};
    }

    private static String getSoapURL() throws MalformedURLException {
        return new URL(StreamingClientExample.LOGIN_ENDPOINT + 
            getSoapUri()).toExternalForm();
    }

    private static String getSoapUri() {
        return SERVICES_SOAP_PARTNER_ENDPOINT;
    }

    private static class LoginResponseParser extends DefaultHandler {

        private boolean inSessionId;
        private String sessionId;

        private boolean inServerUrl;
        private String serverUrl;

        @Override
        public void characters(char[] ch, int start, int length) {
            if (inSessionId) sessionId = new String(ch, start, length);
            if (inServerUrl) serverUrl = new String(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (localName != null) {
                if (localName.equals("sessionId")) {
                    inSessionId = false;
                }
                
                if (localName.equals("serverUrl")) {
                    inServerUrl = false;
                }
            }
        }

        @Override
        public void startElement(String uri, String localName, 
            String qName, Attributes attributes) {
            if (localName != null) {
                if (localName.equals("sessionId")) {
                    inSessionId = true;
                }

                if (localName.equals("serverUrl")) {
                    inServerUrl = true;
                }
            }
        }
    }
}
