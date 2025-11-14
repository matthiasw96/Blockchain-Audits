package org.hrw;

import org.hrw.backend.datacollector.DataCollector;
import org.hrw.backend.verifier.BlockchainHandler;
import org.hrw.backend.verifier.Verifier;
import org.hrw.datamodels.ServerRecord;
import org.hrw.hashing.Hasher;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.of(2025,11,14,13,31);
        LocalDateTime end = LocalDateTime.of(2025,11,14,13, 35);

        String collectorUri = "localhost";
        int port = 8080;

        String bcUri = "nodes-testnet.wavesnodes.com";
        String bcAddress = "3MsGAHcmUcWLmRAURWFFkuHxMfFp5gossUH";

        String hasherAlgorithm = "SHA-256";


        DataCollector collector = new DataCollector(collectorUri, port);

        BlockchainHandler bcHandler = new BlockchainHandler(bcUri, bcAddress);

        Hasher hasher = new Hasher(hasherAlgorithm, 30);

        Verifier verifier = new Verifier(hasher, bcHandler);
/*/
        List<String> test = new ArrayList<>();
test.add("f8f28adb7613bf0c2501c2f84ee23a50fac4de9c44cb28989435e3598e248bd9");
test.add("cf98a1904542c919bdfb51b13ec10e40fab9bf7ee78435e7cb26da562a301677");
test.add("38f37b7e62d4b169a5e3f1aae616801c9e8ec2b20c8b29c066d2e32334abbc37");
test.add("d34b2632ab2bd5b8a0c4587259d33ce6b9c4210a909517cd456bf08321b37bb4");
test.add("49e7c869f42da9ec94a1b99a334dea874fcfab3af944378acf201c8d6e1646c9");
test.add("dfd78b4cb26a73003d64272966c1fa10b66f1dba4b4f5fae399e82aaeca4ef28");
test.add("33f6bd50ddd649ad6a6de088d1e02aa454487d3cd9644e81a216958cf413cef4");
test.add("faf201c5f00e31e589bf38b956be5a2ed5f4fe21a9d7e4ed7e3cba6b05e0932d");
test.add("98694f2608f9f1253d6ae1470a0350cbdc69e7b343da59fe7011f5e4b2842e02");
test.add("98694f2608f9f1253d6ae1470a0350cbdc69e7b343da59fe7011f5e4b2842e02");
test.add("2ce9b0dd937599077a3ba3d3e292cb25ed865b01bdc83e7559e0d65f95069e9b");
test.add("581fdfad4355d19878c26c15257efb8501134dfb7336d4e2726d6212bc6877d9");
test.add("6839c4f01e794c7c3e8f8f066ac332f1f798a210ad7f3cd64ab4c4f27da7d6b8");
//test.add("89bbd71b7ae81cace8723acb819003a0daeba5783dfec043d5759b221c0f47fc");
        //Collections.reverse(test);

        hasher.setSecondHashes(test);
        long timestamp = 1762952400;

        try {
            byte[] hourHash = hasher.createMinuteHash(timestamp);
            System.out.println("Hour Hash: " + HexFormat.of().formatHex(hourHash));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
*/


/**/
        try {
            List<ServerRecord> data = collector.getServerData(start, end);

            System.out.println(data.getFirst());

            boolean verified = verifier.verify(data);

            System.out.println(verified);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

/**/
    }
}