package com.nobodycodewithme.springtransactional.repository;

public interface Repository<T, I> {
    T save(T entity);

    T getById(I id);

    T delete(I id);
}
