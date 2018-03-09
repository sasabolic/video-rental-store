package com.example.videorentalstore.rental.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * List of {@link CreateRentalRequest} DTOs.
 */
@Getter
@ToString
@EqualsAndHashCode
public class CreateRentalRequestList implements List<CreateRentalRequest> {

    @NotNull(message = "List of create rental requests cannot be null")
    @NotEmpty(message = "List of create rental requests cannot be empty")
    @Valid
    private List<CreateRentalRequest> createRentalRequests;

    public CreateRentalRequestList() {
        this.createRentalRequests = new ArrayList<>();
    }

    public CreateRentalRequestList(List<CreateRentalRequest> createRentalRequests) {
        this.createRentalRequests = createRentalRequests;
    }

    @Override
    public int size() {
        return createRentalRequests.size();
    }

    @Override
    public boolean isEmpty() {
        return createRentalRequests.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return createRentalRequests.contains(o);
    }

    @Override
    public Iterator<CreateRentalRequest> iterator() {
        return createRentalRequests.iterator();
    }

    @Override
    public Object[] toArray() {
        return createRentalRequests.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return createRentalRequests.toArray(a);
    }

    @Override
    public boolean add(CreateRentalRequest createRentalRequest) {
        return createRentalRequests.add(createRentalRequest);
    }

    @Override
    public boolean remove(Object o) {
        return createRentalRequests.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return createRentalRequests.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends CreateRentalRequest> c) {
        return createRentalRequests.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends CreateRentalRequest> c) {
        return createRentalRequests.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return createRentalRequests.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return createRentalRequests.retainAll(c);
    }

    @Override
    public void clear() {
        createRentalRequests.clear();;
    }

    @Override
    public CreateRentalRequest get(int index) {
        return createRentalRequests.get(index);
    }

    @Override
    public CreateRentalRequest set(int index, CreateRentalRequest element) {
        return createRentalRequests.set(index, element);
    }

    @Override
    public void add(int index, CreateRentalRequest element) {
        createRentalRequests.add(index, element);
    }

    @Override
    public CreateRentalRequest remove(int index) {
        return createRentalRequests.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return createRentalRequests.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return createRentalRequests.lastIndexOf(o);
    }

    @Override
    public ListIterator<CreateRentalRequest> listIterator() {
        return createRentalRequests.listIterator();
    }

    @Override
    public ListIterator<CreateRentalRequest> listIterator(int index) {
        return createRentalRequests.listIterator(index);
    }

    @Override
    public List<CreateRentalRequest> subList(int fromIndex, int toIndex) {
        return createRentalRequests.subList(fromIndex, toIndex);
    }
}
