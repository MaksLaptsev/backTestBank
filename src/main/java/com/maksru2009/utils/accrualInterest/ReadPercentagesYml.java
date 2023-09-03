package com.maksru2009.utils.accrualInterest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Класс для считывания параметра % для {@link AccrualOfInterest} из файла percent.yml
 */
public class ReadPercentagesYml {
    private ObjectMapper mapper;
    private Percentages percentages;

    public ReadPercentagesYml() {
        mapper = new ObjectMapper(new YAMLFactory());
    }

    /**
     * Получение информации об %, в случае неудачного считывания - устанавливается значение по умолчанию - 1%
     * @return - double %
     */
    public double getPercent(){
        readPercent();
        if (percentages != null){
            return percentages.getPercentages();
        }else return 1;
    }

    /**
     * Считывание файла percent.yml и получение из него заданного значения для %
     * в случае неудачи(ошибки считывания) - установка значени % равного 5
     */
    private void readPercent(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("percent.yml")).getFile());
        try {
            percentages = mapper.readValue(file, Percentages.class);
        }catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("Initialize with 5%");
            percentages = new Percentages();
            percentages.setPercentages(5);
        }
    }

    static class Percentages{
        private double percentages;

        public Percentages() {
        }

        public double getPercentages() {
            return percentages;
        }

        public void setPercentages(double percentages) {
            this.percentages = percentages;
        }
    }
}
