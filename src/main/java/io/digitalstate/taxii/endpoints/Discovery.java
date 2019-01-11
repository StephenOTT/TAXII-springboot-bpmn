package io.digitalstate.taxii.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.digitalstate.taxii.common.Headers;
import io.digitalstate.taxii.mongo.exception.DiscoveryDoesNotExistException;
import io.digitalstate.taxii.mongo.model.document.DiscoveryDocument;
import io.digitalstate.taxii.mongo.repository.DiscoveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class Discovery{

    @Autowired
    DiscoveryRepository discoveryRepository;

    @GetMapping("/taxii")
    @ResponseBody
    public ResponseEntity<String> getDiscovery(@RequestHeader HttpHeaders headers)
            throws JsonProcessingException {

        DiscoveryDocument discoveryDocument = discoveryRepository.findDiscovery()
                .orElseThrow(DiscoveryDoesNotExistException::new);

        return ResponseEntity.ok()
                .headers(Headers.getSuccessHeaders())
                .body(discoveryDocument.toJson());
    }

}
