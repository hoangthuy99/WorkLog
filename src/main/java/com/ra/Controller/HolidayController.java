package com.ra.Controller;

import com.ra.DAO.Holiday.HolidayDAO;
import com.ra.Model.Entity.Holidays;

import java.util.List;

public class HolidayController {
    private HolidayDAO holidayDAO;
    public HolidayController(HolidayDAO holidayDAO) {
        this.holidayDAO = holidayDAO;
    }
    public Holidays createHoliday(Holidays holiday) {
        return holidayDAO.create(holiday);
    }
    public Holidays updateHoliday(Holidays holiday) {
        return holidayDAO.update(holiday);
    }
    public Holidays deleteHoliday(Holidays holiday) {
        holidayDAO.deleteFindById(holiday.getId());
        return holiday;
    }
    public Holidays findById(int id) {
        return holidayDAO.findById(id).orElse(null);
    }
    public List<Holidays> findAll() {
        return holidayDAO.findAll();
    }

    public List<Holidays> findByDateHoliday(String dateHoliday) {
        return holidayDAO.findByDate(dateHoliday);
    }
    public List<Holidays> findByName(String name) {
        return holidayDAO.findByName(name).stream().toList();
    }
}