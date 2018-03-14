package com.example.videorentalstore.rental.web.dto;

import com.example.videorentalstore.rental.RentalInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Batch pay rentals request DTO. Contains List of {@link CreateRentalRequest} DTOs.
 */
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BatchCreateRentalRequest implements List<CreateRentalRequest> {

    @NotNull(message = "List of pay rental requests cannot be null")
    @NotEmpty(message = "List of pay rental requests cannot be empty")
    @Valid
    private List<CreateRentalRequest> createRentalRequests = new ArrayList<>();

    public BatchCreateRentalRequest(List<CreateRentalRequest> createRentalRequests) {
        this.createRentalRequests = createRentalRequests;
    }
    public List<RentalInfo> toRentalInfoList() {
        return this.createRentalRequests.stream().map(CreateRentalRequest::toRentalInfo).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return this.createRentalRequests.size();
    }

    @Override
    public boolean isEmpty() {
        return this.createRentalRequests.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.createRentalRequests.contains(o);
    }

    @Override
    public Iterator<CreateRentalRequest> iterator() {
        return this.createRentalRequests.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.createRentalRequests.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.createRentalRequests.toArray(a);
    }

    @Override
    public boolean add(CreateRentalRequest createRentalRequest) {
        return this.createRentalRequests.add(createRentalRequest);
    }

    @Override
    public boolean remove(Object o) {
        return this.createRentalRequests.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.createRentalRequests.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends CreateRentalRequest> c) {
        return this.createRentalRequests.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends CreateRentalRequest> c) {
        return this.createRentalRequests.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.createRentalRequests.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.createRentalRequests.retainAll(c);
    }

    @Override
    public void clear() {
        this.createRentalRequests.clear();
    }

    @Override
    public CreateRentalRequest get(int index) {
        return this.createRentalRequests.get(index);
    }

    @Override
    public CreateRentalRequest set(int index, CreateRentalRequest element) {
        return this.createRentalRequests.set(index, element);
    }

    @Override
    public void add(int index, CreateRentalRequest element) {
        this.createRentalRequests.add(index, element);
    }

    @Override
    public CreateRentalRequest remove(int index) {
        return this.createRentalRequests.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.createRentalRequests.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.createRentalRequests.lastIndexOf(o);
    }

    @Override
    public ListIterator<CreateRentalRequest> listIterator() {
        return this.createRentalRequests.listIterator();
    }

    @Override
    public ListIterator<CreateRentalRequest> listIterator(int index) {
        return this.createRentalRequests.listIterator(index);
    }

    @Override
    public List<CreateRentalRequest> subList(int fromIndex, int toIndex) {
        return this.createRentalRequests.subList(fromIndex, toIndex);
    }
}
