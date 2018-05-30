package com.example.videorentalstore.rental.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.*;

/**
 * Batch return rentals request DTO. Contains List of {@link ReturnRentalRequest} DTOs.
 *
 * @author Sasa Bolic
 */
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BatchReturnRentalRequest implements List<ReturnRentalRequest> {

    @NotEmpty(message = "List of return rental requests cannot be empty")
    @Valid
    private List<ReturnRentalRequest> returnRentalRequests = new ArrayList<>();

    public BatchReturnRentalRequest(List<ReturnRentalRequest> returnRentalRequests) {
        this.returnRentalRequests = returnRentalRequests;
    }

    @Override
    public int size() {
        return this.returnRentalRequests.size();
    }

    @Override
    public boolean isEmpty() {
        return this.returnRentalRequests.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.returnRentalRequests.contains(o);
    }

    @Override
    public Iterator<ReturnRentalRequest> iterator() {
        return this.returnRentalRequests.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.returnRentalRequests.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.returnRentalRequests.toArray(a);
    }

    @Override
    public boolean add(ReturnRentalRequest returnRentalRequest) {
        return this.returnRentalRequests.add(returnRentalRequest);
    }

    @Override
    public boolean remove(Object o) {
        return this.returnRentalRequests.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.returnRentalRequests.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends ReturnRentalRequest> c) {
        return this.returnRentalRequests.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends ReturnRentalRequest> c) {
        return this.returnRentalRequests.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.returnRentalRequests.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.returnRentalRequests.retainAll(c);
    }

    @Override
    public void clear() {
        this.returnRentalRequests.clear();
    }

    @Override
    public ReturnRentalRequest get(int index) {
        return this.returnRentalRequests.get(index);
    }

    @Override
    public ReturnRentalRequest set(int index, ReturnRentalRequest element) {
        return this.returnRentalRequests.set(index, element);
    }

    @Override
    public void add(int index, ReturnRentalRequest element) {
        this.returnRentalRequests.add(index, element);
    }

    @Override
    public ReturnRentalRequest remove(int index) {
        return this.returnRentalRequests.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.returnRentalRequests.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.returnRentalRequests.lastIndexOf(o);
    }

    @Override
    public ListIterator<ReturnRentalRequest> listIterator() {
        return this.returnRentalRequests.listIterator();
    }

    @Override
    public ListIterator<ReturnRentalRequest> listIterator(int index) {
        return this.returnRentalRequests.listIterator(index);
    }

    @Override
    public List<ReturnRentalRequest> subList(int fromIndex, int toIndex) {
        return this.returnRentalRequests.subList(fromIndex, toIndex);
    }
}
