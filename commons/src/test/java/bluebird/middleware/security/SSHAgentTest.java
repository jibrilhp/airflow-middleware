package jibs.middleware.security;

//import com.jcraft.jsch.agentproxy.AgentProxy;
//import com.jcraft.jsch.agentproxy.AgentProxyException;
//import com.jcraft.jsch.agentproxy.Identity;
//import com.jcraft.jsch.agentproxy.USocketFactory;
//import com.jcraft.jsch.agentproxy.connector.SSHAgentConnector;
//import com.jcraft.jsch.agentproxy.usocket.JNAUSocketFactory;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author gusto
// */
//public class SSHAgentTest {
//
//    private static final Logger L = LoggerFactory.getLogger(SSHAgentTest.class);
//
//    @Test
//    public void testListKeys() {
//        try {
//            //USocketFactory udsf = new JUnixDomainSocketFactory();
//            //USocketFactory udsf = new NCUSocketFactory();
//            USocketFactory udsf = new JNAUSocketFactory();
//            AgentProxy ap = new AgentProxy(new SSHAgentConnector(udsf));
//
//            Identity[] identities = ap.getIdentities();
//            for (Identity identity : identities) {
//                L.info("  comment: {}", new String(identity.getComment()));
//
//                byte[] blob = identity.getBlob();
//                StringBuilder blobsb = new StringBuilder();
//                for (int j = 0; j < blob.length; j++) {
//                    blobsb.append(Integer.toHexString(blob[j] & 0xff)).append(":");
//                }
//                L.info("  blob: {}", blobsb.toString());
//
//                String data = "foo";
//                byte[] signed = ap.sign(blob, data.getBytes());
//                StringBuilder signedsb = new StringBuilder();
//                for (int j = 0; j < signed.length; j++) {
//                    signedsb.append(Integer.toHexString(signed[j] & 0xff)).append(":");
//                }
//                L.info("  sign: {} -> {}", data, signedsb.toString());
//            }
//        } catch (AgentProxyException e) {
//            System.out.println(e);
//        }
//    }
//}
