package com.forgerock.openbanking.exercise.tpp.api.matls;

import com.forgerock.openbanking.exercise.tpp.services.DirectoryService;
import com.forgerock.openbanking.exercise.tpp.services.JwkManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class TestMatlsController implements TestMatls {

    @Autowired
    private JwkManagementService jwkManagementService;
    @Autowired
    private DirectoryService directoryService;
    @Override
    public ResponseEntity<String> testMatlsForJwkMs() {
        return ResponseEntity.ok(jwkManagementService.testmatls());
    }

    @Override
    public ResponseEntity<String> testMatlsForDirectory() {
        return ResponseEntity.ok(directoryService.testmatls());
    }
}
