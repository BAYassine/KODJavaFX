/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;


import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author MacBook
 * @param <T>
 */
public interface IAllForKids<T>  {
 
    
    public void insert(T t); 
    public List<T> getAll() ;
    public Map<String,T> getAllMap();
    public T search(int id) ; 
    public boolean delete(int id) ; 
    public boolean update(T t) ;
     public T getbyPseudo(String pseudo) ;
    
}
