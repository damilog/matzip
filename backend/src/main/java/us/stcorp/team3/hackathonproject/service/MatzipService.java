package us.stcorp.team3.hackathonproject.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.stcorp.team3.hackathonproject.domain.Category;
import us.stcorp.team3.hackathonproject.domain.Matzip;
import us.stcorp.team3.hackathonproject.dto.EntireMatzipResponse;
import us.stcorp.team3.hackathonproject.dto.MatzipRequest;
import us.stcorp.team3.hackathonproject.dto.MatzipResponse;
import us.stcorp.team3.hackathonproject.dto.MatzipReviewResponse;
import us.stcorp.team3.hackathonproject.repository.MatzipRepository;
import us.stcorp.team3.hackathonproject.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class MatzipService {

    private final MatzipRepository matzipRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Page<EntireMatzipResponse> findAllMatzip(final Pageable pageable) {
        final Pageable pageRequest = addSortToPageable(pageable);

        return matzipRepository.findEntireMatzipWithPaging(
            null, pageRequest);
    }

    @Transactional
    public void saveMatzip(final MatzipRequest matzipRequest) {
        matzipRepository.save(MatzipRequest.mapToEntity(matzipRequest));
    }

    @Transactional
    public MatzipResponse findMatzip(final long matzipId) {
        final Optional<Matzip> matzipOptional = matzipRepository.findById(matzipId);
        final List<MatzipReviewResponse> matzipReviewResponse = reviewRepository.findAllByMatzipId(matzipId).stream()
            .map(MatzipReviewResponse::matToReviewResponse)
            .toList();
        if (matzipOptional.isPresent()) {
            final Matzip matzip = matzipOptional.get();
            matzip.increaseViewCount();
            return MatzipResponse.mapToMatzipResponse(matzip, matzipReviewResponse);
        }
        throw new NoSuchElementException("존재하지 않는 맛집입니다.");
    }

    @Transactional(readOnly = true)
    public Page<EntireMatzipResponse> findAllMatzipFilterByCategory(final Category category,
        final Pageable pageable) {
        final PageRequest pageRequest = addSortToPageable(pageable);

        return matzipRepository.findEntireMatzipWithPaging(category, pageRequest);
    }

    @Transactional
    public void modifyMatzip(final long matzipId, final MatzipRequest matzipRequest) {
        Optional<Matzip> matzipOptional = matzipRepository.findById(matzipId);
        if (matzipOptional.isPresent()) {
            Matzip matzip = matzipOptional.get();
            matzip.updateMatzip(matzipRequest);
            return;
        }
        throw new NoSuchElementException("존재하지 않는 맛집입니다.");
    }

    @Transactional
    public void deleteMatzip(final long matzipId) {
        matzipRepository.deleteById(matzipId);
    }

    private PageRequest addSortToPageable(final Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
            Sort.by(Direction.DESC, "rating"));
    }
}
