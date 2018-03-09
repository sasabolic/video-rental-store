package com.example.videorentalstore.rental.web.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * List of {@link RentalRequest} DTO.
 */
@Getter
@ToString
public class ReturnBackRentalRequestList implements List<RentalRequest> {

    @NotNull(message = "List of return rental requests cannot be null")
    @NotEmpty(message = "List of return rental requests cannot be empty")
    @Valid
    private List<RentalRequest> rentalIds;

    public ReturnBackRentalRequestList() {
        this.rentalIds = new ArrayList<>();
    }

    public ReturnBackRentalRequestList(List<RentalRequest> rentalIds) {
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
    public Iterator<RentalRequest> iterator() {
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
    public boolean add(RentalRequest createRentalRequest) {
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
    public boolean addAll(Collection<? extends RentalRequest> c) {
        return this.rentalIds.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends RentalRequest> c) {
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
    public RentalRequest get(int index) {
        return this.rentalIds.get(index);
    }

    @Override
    public RentalRequest set(int index, RentalRequest element) {
        return this.rentalIds.set(index, element);
    }

    @Override
    public void add(int index, RentalRequest element) {
        this.rentalIds.add(index, element);
    }

    @Override
    public RentalRequest remove(int index) {
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
    public ListIterator<RentalRequest> listIterator() {
        return this.rentalIds.listIterator();
    }

    @Override
    public ListIterator<RentalRequest> listIterator(int index) {
        return this.rentalIds.listIterator(index);
    }

    @Override
    public List<RentalRequest> subList(int fromIndex, int toIndex) {
        return this.rentalIds.subList(fromIndex, toIndex);
    }
}
