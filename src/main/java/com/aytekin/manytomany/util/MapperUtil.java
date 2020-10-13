package com.aytekin.manytomany.util;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

public class MapperUtil {

    public static <D, T> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        ModelMapper modelMapper = new ModelMapper();
        return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
    }
}
