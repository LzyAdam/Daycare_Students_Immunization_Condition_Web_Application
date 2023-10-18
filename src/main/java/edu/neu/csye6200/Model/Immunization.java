package edu.neu.csye6200.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Immunization
{

    private String vaccineType;
    private int dose;
    private LocalDate date;

    protected Immunization(String vaccineType, int dose, LocalDate date)
    {
        this.vaccineType = vaccineType;
        this.dose = dose;
        this.date = date;
    }

    public String toCSV()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("%s,%s,%s",
                this.vaccineType,
                this.dose,
                this.date.format(formatter));
    }

    // ========= setters and getters ==========

    public String getVaccineType()
    {
        return vaccineType;
    }

    public void setVaccineType(String vaccineType)
    {
        this.vaccineType = vaccineType;
    }

    public int getDose()
    {
        return dose;
    }

    public void setDose(int dose)
    {
        this.dose = dose;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }
}