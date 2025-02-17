package uz.com.onlineshop.mapper;


public interface EntityMapper<D,E> {

    D toDto(E entity);

}