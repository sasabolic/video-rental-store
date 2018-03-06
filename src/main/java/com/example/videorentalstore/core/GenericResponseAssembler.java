package com.example.videorentalstore.core;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface GenericResponseAssembler<E, D> {

   D of(E entity);

   default List of(final Collection<E> entities) {
       return entities.stream()
               .map(this::of)
               .collect(Collectors.toList());
   }
}