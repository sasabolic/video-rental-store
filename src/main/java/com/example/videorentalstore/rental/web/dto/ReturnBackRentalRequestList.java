package com.example.videorentalstore.rental.web.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * List of {@link ReturnBackRentalRequest} DTO.
 */
@Getter
@ToString
public class ReturnBackRentalRequestList implements List<ReturnBackRentalRequest> {

    @NotNull(message = "List of return rental requests cannot be null")
    @NotEmpty(message = "List of return rental requests cannot be empty")
    @Valid
    private List<ReturnBackRentalRequest> rentalIds;

    public ReturnBackRentalRequestList() {
        this.rentalIds = new ArrayList<>();
    }

    public ReturnBackRentalRequestList(List<ReturnBackRentalRequest> rentalIds) {
        this.rentalIds = rentalIds;
    }

    @Override
    public int size() {
        return this.rentalIds.size();
    }

    @Override
    public boolean isEmpty() {
        return this.rentalIds.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.rentalIds.contains(o);
    }

    @Override
    public Iterator<ReturnBackRentalRequest> iterator() {
        return this.rentalIds.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.rentalIds.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.rentalIds.toArray(a);
    }

    @Override
    public boolean add(ReturnBackRentalRequest createRentalRequest) {
        return this.rentalIds.add(createRentalRequest);
    }

    @Override
    public boolean remove(Object o) {
        return this.rentalIds.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.rentalIds.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends ReturnBackRentalRequest> c) {
        return this.rentalIds.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends ReturnBackRentalRequest> c) {
        return this.rentalIds.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.rentalIds.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.rentalIds.retainAll(c);
    }

    @Override
    public void clear() {
        this.rentalIds.clear();;
    }

    @Override
    public ReturnBackRentalRequest get(int index) {
        return this.rentalIds.get(index);
    }

    @Override
    public ReturnBackRentalRequest set(int index, ReturnBackRentalRequest element) {
        return this.rentalIds.set(index, element);
    }

    @Override
    public void add(int index, ReturnBackRentalRequest element) {
        this.rentalIds.add(index, element);
    }

    @Override
    public ReturnBackRentalRequest remove(int index) {
        return this.rentalIds.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.rentalIds.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.rentalIds.lastIndexOf(o);
    }

    @Override
    public ListIterator<ReturnBackRentalRequest> listIterator() {
        return this.rentalIds.listIterator();
    }

    @Override
    public ListIterator<ReturnBackRentalRequest> listIterator(int index) {
        return this.rentalIds.listIterator(index);
    }

    @Override
    public List<ReturnBackRentalRequest> subList(int fromIndex, int toIndex) {
        return this.rentalIds.subList(fromIndex, toIndex);
    }
}
