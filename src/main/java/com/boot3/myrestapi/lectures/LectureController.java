package com.boot3.myrestapi.lectures;

import com.boot3.myrestapi.common.ErrorsResource;
import com.boot3.myrestapi.lectures.dto.LectureReqDto;
import com.boot3.myrestapi.lectures.dto.LectureResDto;
import com.boot3.myrestapi.lectures.dto.LectureResource;
import com.boot3.myrestapi.lectures.validator.LectureValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/lectures", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class LectureController {
//    @Autowired
    private final LectureRepository lectureRepository;
    private final ModelMapper modelMapper;
    private final LectureValidator lectureValidator;


    //constructor injection
//    public LectureController(LectureRepository lectureRepository) {
//        this.lectureRepository = lectureRepository;
//    }



    @PostMapping
    public ResponseEntity createLecture(@RequestBody @Valid
                                        LectureReqDto lectureReqDto, Errors errors) {
        // 입력 항목 오류가 있다면 400 에러 발생
        if(errors.hasErrors()) {

            return getErrors(errors);

        }
        // 입력항목 유효성 체크하고 오류 있다면 400 에러 발생
        this.lectureValidator.validate(lectureReqDto, errors);
        if(errors.hasErrors()) {

            return getErrors(errors);

        }
        //ReqDTO -> entity로 변환
        Lecture lecture = modelMapper.map(lectureReqDto, Lecture.class);

        // free, offline 필드값 업데이트
        lecture.update();
        Lecture addLecture = this.lectureRepository.save(lecture);
        // entity -> ResDTO로 변환
        LectureResDto lectureResDto = modelMapper.map(addLecture, LectureResDto.class);
        // http://localhost:8080/api/lectures/10 링크생성됨
        WebMvcLinkBuilder selfLinkBuilder = linkTo(LectureController.class).slash(lectureResDto.getId());
        URI createUri = selfLinkBuilder.toUri();
        /*
        "_links": {

                "query-lectures": {
                    "href": "http://localhost:8080/api/lectures"
                },
                "self": {
                    "href": "http://localhost:8080/api/lectures/1"
                },
                "update-lecture": {
                    "href": "http://localhost:8080/api/lectures/1"
                }
            }
            */
        LectureResource lectureResource = new LectureResource(lectureResDto);
        lectureResource.add(linkTo(LectureController.class).withRel("query-lectures"));
//        lectureResource.add(selfLinkBuilder.withSelfRel());
        lectureResource.add(selfLinkBuilder.withRel("update-lecture"));
        lectureResource.add(selfLinkBuilder.withRel("update-lecture"));

        return ResponseEntity.created(createUri).body(lectureResource);

    }

    private static ResponseEntity<ErrorsResource> getErrors(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

}