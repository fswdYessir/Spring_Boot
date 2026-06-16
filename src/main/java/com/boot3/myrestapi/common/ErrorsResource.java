package com.boot3.myrestapi.common;


import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
public class ErrorsResource extends EntityModel<Errors> {
    private Errors errors;

    public ErrorsResource(Errors content) {
        this.errors = content;
        //http://localhost:8080/api 'index'이름으로 링크를 생성
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

}