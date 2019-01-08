package io.digitalstate.taxii.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.digitalstate.taxii.common.json.views.TaxiiSpecView;
import io.digitalstate.taxii.model.status.TaxiiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/taxii/tenant/{tenantSlug}")
public class Status {

//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @GetMapping("/{statusId}")
//    @ResponseBody
//    public ResponseEntity<String> getStatus(@RequestHeader HttpHeaders headers,
//                                                 @PathVariable("tenantId") String tenantId,
//                                                 @PathVariable("statusId") String statusId) throws JsonProcessingException {
//
//        TaxiiStatus taxiiStatus = TaxiiStatus.builder()
//                .id(statusId)
//                .status("pending")
//                .totalCount(2)
//                .successCount(1)
//                .failureCount(0)
//                .pendingCount(1)
//                .build();
//
//        String response  = objectMapper.writerWithView(TaxiiSpecView.class).writeValueAsString(taxiiStatus);
//
//        //@TODO look up data from Camunda
//        //@TODO add caching to reduce repeated requests against Camunda engine
//
//        HttpHeaders successHeaders = new HttpHeaders();
//        successHeaders.add("content-type", "application/vnd.oasis.taxii+json");
//        successHeaders.add("version", "2.0");
//
//        return ResponseEntity.ok()
//                .headers(successHeaders)
//                .body(response);
//    }
}
