package com.ra.DAO.Holiday;

import com.ra.Model.Entity.Holidays;

import java.util.List;
import java.util.Optional;

public interface IHolidayDAO {
    Holidays create(Holidays holiday);
    Holidays update(Holidays holiday);
    boolean deleteFindById(int id);
    List<Holidays> findAll();
    Optional<Holidays> findById(int id);
    Optional<Holidays> findByName(String name);
    List<Holidays> findByDate(String dateHoliday);


}
