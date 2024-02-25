package com.jo.adapter.in;

import com.jo.application.in.QueryDummyUseCase;
import com.jo.application.in.SaveDummyUseCase;
import com.jo.application.in.request.DummyRequest;
import com.jo.application.in.response.DummyResponse;
import com.jo.common.annotation.WebAdapter;
import com.jo.common.response.RestResponsePagination;
import com.jo.domain.model.Dummy;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@WebAdapter
@RestController
@AllArgsConstructor
@RequestMapping(value = "/dummy")
@SecurityRequirement(name = "Keycloak")
public class DummyController {

    private final QueryDummyUseCase queryDummyUseCase;
    private final SaveDummyUseCase saveDummyUseCase;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT_ADMIN')")
    public ResponseEntity<DummyResponse> find(@PathVariable("id") int id) {
        return ResponseEntity.ok(new DummyResponse(queryDummyUseCase.findById(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENT_ADMIN')")
    public ResponseEntity<RestResponsePagination<Dummy>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        var paginationElements = queryDummyUseCase.findAll(page, size, sortBy, sortOrder);
        var response = new RestResponsePagination<>(paginationElements.left, paginationElements.right);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT_ADMIN')")
    public ResponseEntity<DummyResponse> save(@RequestBody DummyRequest request) {
        var dummy = saveDummyUseCase.save(new Dummy(request.info()));
        return ResponseEntity.ok(new DummyResponse(dummy));
    }
}
