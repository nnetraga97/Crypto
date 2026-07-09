package com.nnetraga.crypto.settlement.application;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

import com.nnetraga.crypto.settlement.domain.SettlementIntent;

@Component
public class Sha256SettlementRequestHasher implements SettlementRequestHasher {
    @Override
    public String hash(SettlementIntent intent) {
        String canonicalRequest = canonicalize(intent);
        byte[] digest = sha256().digest(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        return toHex(digest);
    }

    private static String canonicalize(SettlementIntent intent) {
        return field(intent.settlementId())
                + field(intent.amount().stripTrailingZeros().toPlainString())
                + field(intent.asset())
                + field(intent.destinationAddress());
    }

    private static String field(String value) {
        return value.length() + ":" + value + ";";
    }

    private static MessageDigest sha256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte value : bytes) {
            hex.append(String.format("%02x", value));
        }
        return hex.toString();
    }
}
