package com.nnetraga.crypto.settlement.audit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuditIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditEventRepository auditEventRepository;

    @Test
    public void createSettlementRecordsApiAndStateChangeAuditEvents() throws Exception {
        mockMvc.perform(post("/settlement-intents")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "settlementId": "settlement-audit-123",
                          "idempotencyKey": "idem-audit-123",
                          "amount": 100.00,
                          "asset": "USDC",
                          "destinationAddress": "destination-address"
                        }
                        """))
                .andExpect(status().isCreated());

        assertTrue(auditEventRepository.countByEventType(AuditEventType.API_REQUEST) >= 1);
        assertTrue(auditEventRepository.countByEventType(AuditEventType.SETTLEMENT_STATE_CHANGED) >= 1);
    }
}
