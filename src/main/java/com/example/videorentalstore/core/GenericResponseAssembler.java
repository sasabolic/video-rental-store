package com.example.videorentalstore.core;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The interface Generic response assembler.
 *
 * @param <E> the type parameter for entity object
 * @param <D> the type parameter for DTO object
 */
public interface GenericResponseAssembler<E, D> {

    /**
     * Transforms entity object to DTO object.
     *
     * @param entity the entity
     * @return the DTO
     */
    D of(E entity);

    /**
     * Transforms collection of entity objects to list of DTO objects.
     *
     * @param entities the entities
     * @return the list of DTO objects
     */
    default List<D> of(final Collection<E> entities) {
       return entities.stream()
               .map(this::of)
               .collect(Collectors.toList());
   }
}