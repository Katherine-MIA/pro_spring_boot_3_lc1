package com.apress.users;

import java.util.Optional;

@SuppressWarnings("unused")
public interface Repository<D, ID> {
    D save(D domain);
    Optional<D> findById(ID id);
    Iterable<D> findAll();
    void deleteById(ID id);
}
