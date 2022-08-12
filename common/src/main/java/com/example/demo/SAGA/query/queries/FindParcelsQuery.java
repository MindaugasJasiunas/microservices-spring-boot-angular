package com.example.demo.SAGA.query.queries;

import org.springframework.data.domain.PageRequest;

public record FindParcelsQuery(PageRequest pageable) {}
