package com.nnetraga.crypto.settlement.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnetraga.crypto.settlement.application.SettlementService;
import com.nnetraga.crypto.settlement.infrastructure.chain.FakeChainSettlementAdapter;

public class SettlementControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        SettlementService settlementService = new SettlementService(new FakeChainSettlementAdapter());
        mockMvc = MockMvcBuilders.standaloneSetup(new SettlementController(settlementService))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
    }

    @Test
    public void createReturnsSubmittedSettlementIntent() throws Exception {
        MvcResult result = createSettlementIntent("settlement-api-create", "idem-api-create")
                .andExpect(status().isCreated())
                .andReturn();

        Map<String, Object> responseBody = responseBody(result);
        assertEquals("SUBMITTED", responseBody.get("status"));
        assertEquals("fake-tx-settlement-api-create", responseBody.get("chainTransactionHash"));
    }

    @Test
    public void getByIdempotencyKeyReturnsExistingSettlementIntent() throws Exception {
        createSettlementIntent("settlement-api-get", "idem-api-get")
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get("/settlement-intents/idempotency/idem-api-get"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> responseBody = responseBody(result);
        assertEquals("settlement-api-get", responseBody.get("settlementId"));
        assertEquals("SUBMITTED", responseBody.get("status"));
    }

    @Test
    public void getByIdempotencyKeyReturnsNotFoundForMissingKey() throws Exception {
        mockMvc.perform(get("/settlement-intents/idempotency/missing-key"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void markConfirmingReturnsConfirmingSettlementIntent() throws Exception {
        createSettlementIntent("settlement-api-confirming", "idem-api-confirming")
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(post("/settlement-intents/idempotency/idem-api-confirming/confirming"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("CONFIRMING", responseBody(result).get("status"));
    }

    @Test
    public void markSettledReturnsSettledSettlementIntent() throws Exception {
        createSettlementIntent("settlement-api-settled", "idem-api-settled")
                .andExpect(status().isCreated());
        mockMvc.perform(post("/settlement-intents/idempotency/idem-api-settled/confirming"))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(post("/settlement-intents/idempotency/idem-api-settled/settled"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("SETTLED", responseBody(result).get("status"));
    }

    @Test
    public void markNeedsReviewReturnsNeedsReviewSettlementIntent() throws Exception {
        createSettlementIntent("settlement-api-review", "idem-api-review")
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(post("/settlement-intents/idempotency/idem-api-review/needs-review"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("NEEDS_REVIEW", responseBody(result).get("status"));
    }

    @Test
    public void invalidTransitionReturnsConflict() throws Exception {
        createSettlementIntent("settlement-api-conflict", "idem-api-conflict")
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(post("/settlement-intents/idempotency/idem-api-conflict/settled"))
                .andExpect(status().isConflict())
                .andReturn();

        assertEquals(
                "SettlementIntent must be CONFIRMING to mark as settled. Current status: SUBMITTED",
                responseBody(result).get("message"));
    }

    @Test
    public void transitionReturnsNotFoundForMissingKey() throws Exception {
        mockMvc.perform(post("/settlement-intents/idempotency/missing-key/confirming"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createReturnsConflictForSameIdempotencyKeyWithDifferentPayload() throws Exception {
        createSettlementIntent("settlement-api-idem", "idem-api-conflict")
                .andExpect(status().isCreated());

        mockMvc.perform(post("/settlement-intents")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "settlementId": "settlement-api-idem",
                          "idempotencyKey": "idem-api-conflict",
                          "amount": 200.00,
                          "asset": "USDC",
                          "destinationAddress": "destination-address"
                        }
                        """))
                .andExpect(status().isConflict());
    }

    private ResultActions createSettlementIntent(
            String settlementId,
            String idempotencyKey) throws Exception {
        return mockMvc.perform(post("/settlement-intents")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "settlementId": "%s",
                          "idempotencyKey": "%s",
                          "amount": 100.00,
                          "asset": "USDC",
                          "destinationAddress": "destination-address"
                        }
                        """.formatted(settlementId, idempotencyKey)));
    }

    private static Map<String, Object> responseBody(MvcResult result) throws Exception {
        return OBJECT_MAPPER.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
    }
}
