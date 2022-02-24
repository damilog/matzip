package us.stcorp.team3.hackathonproject.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.stcorp.team3.hackathonproject.domain.Category;
import us.stcorp.team3.hackathonproject.dto.EntireMatzipResponse;
import us.stcorp.team3.hackathonproject.dto.MatzipRequest;
import us.stcorp.team3.hackathonproject.service.MatzipService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matzip")
public class MatzipController {

    private final MatzipService matzipService;

    @GetMapping
    public ResponseEntity<List<EntireMatzipResponse>> allMatzip(
        @PageableDefault(size = 12) Pageable pageable) {
        return ResponseEntity.ok(matzipService.findAllMatzip(pageable));
    }

    @PostMapping
    public ResponseEntity<String> registerNewMatzip(@RequestBody MatzipRequest matzipRequest) {
        matzipService.saveMatzip(matzipRequest);
        return new ResponseEntity<>("Your Request has Succeed", HttpStatus.CREATED);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(List.of(Category.values()));
    }
}